package com.example.Classes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Profilepage;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    Context context;
    FirebaseServices fbs;
    ArrayList<Profile> profiles;
    ArrayList<Comment> comments;
    public CommentAdapter(Context context, ArrayList<Comment> comments,ArrayList<Profile> profiles) {
        this.profiles=profiles;
        this.context=context;
        this.comments=comments;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.comment,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment comment=comments.get(position);
        fbs =FirebaseServices.getInstance();
        StorageReference storageRef= fbs.getStorage().getInstance().getReference().child(profiles.get(position).getImage());

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
        String s=profiles.get(position).getName();
        try {
            holder.username.setText(s);
        }catch (Exception e){
            Log.e("context",e.getMessage());
        }

        holder.comment.setText(comment.getComment());
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView user,like;
        private TextView comment,reply,username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user=itemView.findViewById(R.id.commentprofile);
            like=itemView.findViewById(R.id.commentlike);
            comment=itemView.findViewById(R.id.commentus);
            username=itemView.findViewById(R.id.commentuser);
        }
    }
}

