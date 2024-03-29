package com.example.Adapters;

import android.content.Context;

import com.bumptech.glide.Glide;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Classes.FirebaseServices;
import com.example.Classes.Post;
import com.example.myapplication.Postsprofile;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class  Minipostadapter extends RecyclerView.Adapter<Minipostadapter.ViewHolder> {
    private FirebaseServices fbs;
    private List<String> data;
    private List<Post> posts;
    private Context context;
    private String email;
    private ArrayList<String> postsref;
    public Minipostadapter(List<String> data,Context context,List<Post> posts,String email,ArrayList<String> postsref) {
        this.data = data;
        this.context=context;
        this.posts=posts;
        this.email=email;
        this.postsref=postsref;
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.minipost, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String s=data.get(position);
        fbs =FirebaseServices.getInstance();
        StorageReference storageRef= fbs.getStorage().getInstance().getReference().child(s);
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
        holder.minipost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Postsprofile((ArrayList<Post>) posts,email,postsref)).addToBackStack(null).commit();
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

