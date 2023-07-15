package com.jiat.eshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.jiat.eshop.R;
import com.jiat.eshop.model.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;
    private FirebaseStorage storage;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, viewGroup, false);
        }
        ImageView image = view.findViewById(R.id.product_item_image);
        TextView name = view.findViewById(R.id.product_item_name);
        TextView price = view.findViewById(R.id.product_item_price);
        storage = FirebaseStorage.getInstance();


        Product product = products.get(i);


        storage.getReference("product-images/" + product.getImagePath()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                List<StorageReference> items = listResult.getItems();
                if (items.size() > 0) {
                    StorageReference reference = listResult.getItems().get(0);

                    storage.getReference(reference.getPath())
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(context).load(uri)
                                            .centerCrop()
                                            .into(image);
                                }
                            });
                }
            }
        });


        name.setText(product.getName());
        price.setText("LKR " + String.valueOf(product.getPrice()));

        return view;
    }
}
