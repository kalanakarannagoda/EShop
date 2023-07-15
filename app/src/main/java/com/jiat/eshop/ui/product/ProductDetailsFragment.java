package com.jiat.eshop.ui.product;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.jiat.eshop.MainActivity;
import com.jiat.eshop.R;
import com.jiat.eshop.adapter.SingleProductImageAdapter;
import com.jiat.eshop.model.Product;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDetailsFragment extends Fragment {

    private static final String TAG = "eshop";
    private ListView productImageListView;
    private ImageView productImage;
    private TextView nameText, priceText, descriptionText;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;

    private List<String> imagesPathList;
    private Product product;
    private MaterialButtonToggleGroup materialButtonToggleGroup;
    private Button addCartButton, buyNowButton;
    private NumberPicker productQuantityPicker;


    public ProductDetailsFragment(Product product) {
        this.product = product;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        MainActivity activity = (MainActivity) getActivity();
        activity.showBottomNavigationView(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productImageListView = view.findViewById(R.id.single_product_image_list);
        productImage = view.findViewById(R.id.single_product_full_image);
        nameText = view.findViewById(R.id.single_product_name);
        priceText = view.findViewById(R.id.single_product_price);
        descriptionText = view.findViewById(R.id.single_product_description);

        productQuantityPicker = view.findViewById(R.id.single_product_quantity);

        addCartButton = view.findViewById(R.id.single_product_add_to_cart);
        buyNowButton = view.findViewById(R.id.single_product_buy_now);

        materialButtonToggleGroup = view.findViewById(R.id.item_sizes);

        nameText.setText(product.getName());
        priceText.setText("LKR " + String.valueOf(product.getPrice()));
        descriptionText.setText(product.getDescription());


        if (product.getSizes().size() > 0) {

            for (String size : product.getSizes()) {
                Button button = (Button) getLayoutInflater().inflate(R.layout.custom_variation_button, materialButtonToggleGroup, false);
                button.setText(size);
                materialButtonToggleGroup.addView(button);
            }

        }


        imagesPathList = new ArrayList<>();

        SingleProductImageAdapter adapter = new SingleProductImageAdapter(view.getContext(), imagesPathList);

        productImageListView.setAdapter(adapter);

        storage.getReference("product-images/" + product.getImagePath()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference reference : listResult.getItems()) {
                    imagesPathList.add(reference.getPath());
                }

                if (imagesPathList.size() > 0) {
                    storage.getReference(imagesPathList.get(0))
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(view.getContext()).load(uri)
                                            .into(productImage);
                                }
                            });
                }

                adapter.notifyDataSetChanged();
            }
        });


        productImageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                storage.getReference(imagesPathList.get(i))
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(view.getContext()).load(uri)
                                        .into(productImage);
                            }
                        });
            }
        });

        addCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                firebaseFirestore.collection("users").whereEqualTo("id", firebaseAuth.getCurrentUser().getUid())
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {

                                        CollectionReference cart = snapshot.getReference().collection("cart");

                                        cart.document(product.getId()).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot result = task.getResult();
                                                            if (result.exists()) {
                                                                Integer qty = result.get("qty", Integer.class);
                                                                result.getReference().update("qty", (qty + productQuantityPicker.getValue()))
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(view.getContext(), "Product quantity updated", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            } else {
                                                                Map<String, Object> cartData = new HashMap<>();
                                                                cartData.put("product", product);
                                                                cartData.put("qty", productQuantityPicker.getValue());

                                                                cart.document(product.getId()).set(cartData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(view.getContext(), "Product has been added to your cart", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                });

                                    }
                                }

                            }
                        });
            }
        });


        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}