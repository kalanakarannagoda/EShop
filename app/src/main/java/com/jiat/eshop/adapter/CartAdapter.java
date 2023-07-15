package com.jiat.eshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.jiat.eshop.R;
import com.jiat.eshop.model.CartItem;
import com.jiat.eshop.model.Category;
import com.jiat.eshop.model.Product;
import com.jiat.eshop.ui.cart.CartFragment;
import com.jiat.eshop.ui.product.ProductFragment;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.ListIterator;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private double cartTotal;
    private FirebaseStorage storage;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private CartFragment cartFragment;
    private NumberFormat formatter = new DecimalFormat("LKR #,###.##");

    public CartAdapter(List<CartItem> cartItems, Context context, CartFragment fragment) {
        this.cartItems = cartItems;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.cartFragment = fragment;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        CartItem cartItem = cartItems.get(position);

        holder.name.setText(cartItem.getProduct().getName());

        double total = cartItem.getProduct().getPrice();

        holder.price.setText(formatter.format(total));
        holder.quantityPicker.setValue(cartItem.getQty());
        holder.product = cartItem.getProduct();


        storage.getReference("product-images/" + cartItem.getProduct().getImagePath()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference reference : listResult.getItems()) {

                    reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Glide.with(context)
                                        .load(task.getResult())
                                        .override(100, 100)
                                        .into(holder.image);
                            }
                        }
                    });

                    break;
                }

            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
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

                                        snapshot.getReference().collection("cart").document(holder.product.getId()).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "Product has been removed from your cart", Toast.LENGTH_SHORT).show();

                                                        ListIterator<CartItem> iterator = cartItems.listIterator();
                                                        while (iterator.hasNext()){
                                                            CartItem next = iterator.next();
                                                            if (next.getProduct().getId().equals(holder.product.getId())){
                                                                iterator.remove();
                                                                notifyDataSetChanged();
                                                                cartFragment.calculateTotal();
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







    }




    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView image;
        TextView name, price;
        Button delete;
        NumberPicker quantityPicker;
        Product product;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cart_list_item_image);
            name = itemView.findViewById(R.id.cart_list_item_name);
            price = itemView.findViewById(R.id.cart_list_item_price);
            delete = itemView.findViewById(R.id.cart_list_item_delete);
            quantityPicker = itemView.findViewById(R.id.cart_list_item_quantity_update);
            this.itemView = itemView;
        }
    }




}
