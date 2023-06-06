package com.example.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Classes.FirebaseServices;
import com.example.Classes.Profile;
import com.example.Classes.User;
import com.example.myapplication.Profilepage;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Searchadapter extends RecyclerView.Adapter<Searchadapter.MyViewHolder> {

    Context context;
    private FirebaseServices fbs;
    private ArrayList<User> userArrayList;
    private ArrayList<Profile> profiles;



    public Searchadapter(Context context, ArrayList<User> userArrayList,ArrayList<Profile> profiles) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.profiles=profiles;
    }

    @NonNull
    @Override
    public Searchadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.searchprofiles,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Profile profile=profiles.get(position);
        fbs=FirebaseServices.getInstance();
        holder.nickname.setText(profile.getNickname());
        holder.username.setText(profile.getName());
        StorageReference storageRef= fbs.getStorage().getInstance().getReference().child(profile.getImage());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur when downloading the image
            }
        });
        holder.userclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Profilepage(userArrayList.get(position).getUser())).addToBackStack(null).commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout userclick;
        private ImageView user;
        private TextView username,nickname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user=itemView.findViewById(R.id.profilese);
            username=itemView.findViewById(R.id.usernamese);
            nickname=itemView.findViewById(R.id.nicknamese);
            userclick=itemView.findViewById(R.id.profileclick);
        }
    }
}
