package com.jiat.eshop.ui.categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jiat.eshop.MainActivity;
import com.jiat.eshop.R;
import com.jiat.eshop.adapter.CategoryAdapter;
import com.jiat.eshop.model.Category;

import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private List<Category> categoryList;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        categoryList = new ArrayList<>();

        //insertSampleData();

    }

    private void insertSampleData() {

        CollectionReference reference = firebaseFirestore.collection("categories");

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("1", "Fashion", ""));
        categories.add(new Category("2", "Mobiles and Tablets", ""));
        categories.add(new Category("3", "Consumer Electronics", ""));
        categories.add(new Category("4", "Books", ""));
        categories.add(new Category("5", "Baby Products", ""));
        categories.add(new Category("6", "Groceries", ""));

        for(Category c : categories){
            reference.add(c);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MainActivity activity =(MainActivity) getActivity();
        activity.showBottomNavigationView(true);

        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.categories_categories_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        CategoryAdapter adapter = new CategoryAdapter(categoryList, view.getContext());
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    categoryList.clear();
                    for(QueryDocumentSnapshot snapshot : task.getResult()){
                        Category category = snapshot.toObject(Category.class);
                        categoryList.add(category);
                    }

                    adapter.notifyDataSetChanged();

                }
            }
        });





    }
}