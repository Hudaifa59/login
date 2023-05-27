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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Profilepage;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class  ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
    private Context context;
    private FirebaseServices fbs;
    private ArrayList<Profile> profiles;
    private ArrayList<Reply> replies;
    private ArrayList<String> repliespath;

    public ReplyAdapter(Context context, ArrayList<Reply> comments, ArrayList<String> repliespath, ArrayList<Profile> profiles) {
        this.context=context;
        this.replies=comments;
        this.repliespath=repliespath;
        this.profiles=profiles;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reply reply=replies.get(position);
        fbs=FirebaseServices.getInstance();
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
        holder.reply.setText(reply.getComment());
        holder.username.setText(profiles.get(position).getName());
        if(reply.getLikes().size()!=0){
            for (int i=0;i<reply.getLikes().size();i=i+1){
                if (reply.getLikes().get(i).equals(fbs.getAuth().getCurrentUser().getEmail()))holder.like.setImageResource(R.drawable.filledheart);
            }
        }
        holder.likes.setText(""+reply.getLikes().size());
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getDrawable().getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.filledheart).getConstantState())) {
                    DocumentReference userRef = fbs.getFire().collection("Reply").document(repliespath.get(position));
                    userRef.get()
                            .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                                if (documentSnapshot.exists()) {
                                    ArrayList<String> likes=replies.get(position).getLikes();
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
                    DocumentReference userRef = fbs.getFire().collection("Reply").document(repliespath.get(position));
                    userRef.get()
                            .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {

                                if (documentSnapshot.exists()) {
                                    ArrayList<String> likes=replies.get(position).getLikes();
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
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Profilepage(reply.getUser())).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

         ImageView user,like;
         TextView reply,username,likes;

        public ViewHolder(View itemView) {
            super(itemView);
            user=itemView.findViewById(R.id.replyprofile);
            like=itemView.findViewById(R.id.replylike);
            likes=itemView.findViewById(R.id.replylikes);
            reply=itemView.findViewById(R.id.reply);
            username=itemView.findViewById(R.id.replyuser);
        }
    }
}

