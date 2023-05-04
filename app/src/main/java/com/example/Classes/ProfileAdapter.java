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

public class  ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private Bitmap bitmap1;
    private FirebaseServices fbs;
    private List<Profile> data;

    private Context context;
    public ProfileAdapter(List<Profile> data,Context context) {
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
                        .into(holder.profilepic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur when downloading the image
            }
        });
        holder.nickname.setText(profile.getNickname());
        holder.phone.setText(profile.getPhone());
        holder.name.setText(profile.getName());
        holder.gender.setText(profile.getGender());
        holder.post.setText(String.valueOf(profile.getPosts()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname,name,gender,phone,post;
        ImageView profilepic;

        public ViewHolder(View itemView) {
            super(itemView);
            nickname=itemView.findViewById(R.id.nicknameadap);
            name=itemView.findViewById(R.id.nameadap);
            gender=itemView.findViewById(R.id.genderadap);
            phone=itemView.findViewById(R.id.phoneadap);
            post=itemView.findViewById(R.id.postadap);
            profilepic=itemView.findViewById(R.id.profileadap);
        }
    }
}

