package com.jiat.eshop.ui.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jiat.eshop.MainActivity;
import com.jiat.eshop.R;
import com.jiat.eshop.adapter.ProductAdapter;
import com.jiat.eshop.model.Category;
import com.jiat.eshop.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductFragment extends Fragment {
    private static final String TAG = "eshop";
    private FirebaseFirestore firebaseFirestore;
    private GridView productView;
    private Category category;
    private List<Product> products;

    public ProductFragment(Category category) {
        this.category = category;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        products = new ArrayList<>();


        //insertSampleData();

    }


    private void insertSampleData() {
        CollectionReference products = firebaseFirestore.collection("products");

        List<Product> productsList = new ArrayList<>();
        productsList.add(new Product("P1", "T Shirt", "asfadasd", "", 2200, "1"));
        productsList.add(new Product("P2", "Trouser", "asfadasd", "", 4000, "1"));
        productsList.add(new Product("P3", "Jeans", "asfadasd", "", 3200, "1"));
        productsList.add(new Product("P4", "Belt", "asfadasd", "", 1500, "1"));
        productsList.add(new Product("P5", "Short", "asfadasd", "", 2200, "1"));


        for (Product p : productsList) {
            products.add(p);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MainActivity activity = (MainActivity) getActivity();
        activity.showBottomNavigationView(false);

        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productView = view.findViewById(R.id.product_product_list);

        ProductAdapter adapter = new ProductAdapter(view.getContext(), products);
        productView.setAdapter(adapter);


        if (this.category != null) {
            firebaseFirestore.collection("products").whereEqualTo("category", this.category.getId()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                products.clear();
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    Product product = snapshot.toObject(Product.class);
                                    products.add(product);
                                }
                                adapter.notifyDataSetChanged();

                                if (task.getResult().isEmpty()){
                                    productView.setVisibility(View.GONE);
                                    view.findViewById(R.id.product_no_product_text).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
        }


        productView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Product product = products.get(i);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, new ProductDetailsFragment(product));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        productView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {

//                    // load more products like pagination

//                    view.findViewById(R.id.product_message).setVisibility(View.VISIBLE);
//
//                    products.add(new Product("P1", "T Shirt", "asfadasd", "", 2200, "1"));
//                    products.add(new Product("P2", "Trouser", "asfadasd", "", 4000, "1"));
//                    products.add(new Product("P3", "Jeans", "asfadasd", "", 3200, "1"));
//                    products.add(new Product("P4", "Belt", "asfadasd", "", 1500, "1"));
//                    products.add(new Product("P5", "Short", "asfadasd", "", 2200, "1"));
//                    adapter.notifyDataSetChanged();
//
//
//                    view.findViewById(R.id.product_message).setVisibility(View.INVISIBLE);
                }

            }
        });


    }
}