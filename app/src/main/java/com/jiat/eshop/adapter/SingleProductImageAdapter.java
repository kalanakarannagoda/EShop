package com.jiat.eshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jiat.eshop.R;

import java.util.List;

public class SingleProductImageAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> images;
    private FirebaseStorage storage;

    public SingleProductImageAdapter(@NonNull Context context, List<String> images) {
        super(context, R.layout.single_product_list_item, images);
        this.context = context;
        this.images = images;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String imagePath = images.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_product_list_item, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.single_product_item);


        storage.getReference(imagePath)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri)
                                .into(image);
                    }
                });


        return convertView;
    }
}
