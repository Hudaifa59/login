package com.example.Classes;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Minipostadapter extends RecyclerView.Adapter<Minipostadapter.ViewHolder> {
    private Bitmap bitmap1;
    private FirebaseServices fbs;
    private List<Profile> data;

    private Context context;
    public Minipostadapter(List<Profile> data,Context context) {
        this.data = data;
        this.context=context;
    }

    public void setData(List<Profile> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profile profile=data.get(position);
        fbs =FirebaseServices.getInstance();

        StorageReference storageRef= fbs.getStorage().getInstance().getReference().child(profile.getImage());

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.minipost);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur when downloading the image
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView minipost;

        public ViewHolder(View itemView) {
            super(itemView);
            minipost=itemView.findViewById(R.id.minipostiv);
        }
    }
}

