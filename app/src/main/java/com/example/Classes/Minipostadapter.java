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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.LoginFragment;
import com.example.myapplication.R;
import com.example.myapplication.posts;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class  Minipostadapter extends RecyclerView.Adapter<Minipostadapter.ViewHolder> {
    private Bitmap bitmap1;
    private FirebaseServices fbs;
    private List<String> data;

    private Context context;
    public Minipostadapter(List<String> data,Context context) {
        this.data = data;
        this.context=context;
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
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new posts()).commit();
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

    public void gotoProfileFragment()
    {
        //FragmentTransaction ft = .getSupportFragmentManager().beginTransaction();
        //ft.replace(R.id.framMain, new LoginFragment());
        //ft.commit();
    }
}

