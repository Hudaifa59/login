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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Postsprofile;
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
    ArrayList<String> commentspa;

    public CommentAdapter(Context context, ArrayList<Comment> comments,ArrayList<Profile> profiles,  ArrayList<String> commentss) {
        this.profiles=profiles;
        this.context=context;
        this.comments=comments;
        this.commentspa=commentss;
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

        holder.username.setText(profiles.get(position).getName());


        holder.comment.setText(comment.getComment());
        if(comment.getLike().size()!=0){
            for (int i=0;i<comment.getLike().size();i=i+1){
                if (comment.getLike().get(i).equals(fbs.getAuth().getCurrentUser().getEmail()))holder.like.setImageResource(R.drawable.filledheart);
            }
        }
        holder.likes.setText(""+comments.get(position).getLike().size());
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getDrawable().getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.filledheart).getConstantState())) {
                    DocumentReference userRef = fbs.getFire().collection("Comments").document(commentspa.get(position));
                    userRef.get()
                            .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                                if (documentSnapshot.exists()) {
                                    ArrayList<String> likes=comments.get(position).getLike();
                                    likes.remove(fbs.getAuth().getCurrentUser().getEmail());
                                    documentSnapshot.getReference().update("like", likes)
                                            .addOnSuccessListener(aVoid -> {
                                                System.out.println("ArrayList updated successfully.");
                                                holder.like.setImageResource(R.drawable.heart);
                                                holder.likes.setText(""+likes.size());
                                            })
                                            .addOnFailureListener(e -> {
                                                System.out.println("Error updating ArrayList: " + e.getMessage());
                                            });
                                } else {
                                    System.out.println("User document doesn't exist.");
                                }
                            })
                            .addOnFailureListener(e -> {
                                System.out.println("Error retrieving user: " + e.getMessage());
                            });

                } else {
                    DocumentReference userRef = fbs.getFire().collection("Comments").document(commentspa.get(position));
                    userRef.get()
                            .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {

                                if (documentSnapshot.exists()) {
                                    ArrayList<String> likes=comments.get(position).getLike();
                                    likes.add(fbs.getAuth().getCurrentUser().getEmail());
                                    documentSnapshot.getReference().update("like", likes)
                                            .addOnSuccessListener(aVoid -> {
                                                System.out.println("ArrayList updated successfully.");
                                                holder.like.setImageResource(R.drawable.filledheart);
                                                holder.likes.setText(""+likes.size());
                                            })
                                            .addOnFailureListener(e -> {
                                                System.out.println("Error updating ArrayList: " + e.getMessage());
                                            });
                                } else {
                                    System.out.println("User document doesn't exist.");
                                }
                            })
                            .addOnFailureListener(e -> {
                                System.out.println("Error retrieving user: " + e.getMessage());
                            });


                }
            }
        });
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Profilepage(comment.getUser())).addToBackStack(null).commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView user,like;
        private TextView comment,reply,username,likes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user=itemView.findViewById(R.id.commentprofile);
            like=itemView.findViewById(R.id.commentlike);
            likes=itemView.findViewById(R.id.Likesforcom);
            comment=itemView.findViewById(R.id.commentus);
            username=itemView.findViewById(R.id.commentuser);
        }
    }
}

