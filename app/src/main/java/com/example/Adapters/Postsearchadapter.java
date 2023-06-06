package com.example.Adapters;

import android.content.Context;
import android.net.Uri;
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
import com.example.Classes.FirebaseServices;
import com.example.Classes.Post;
import com.example.Classes.Profile;
import com.example.myapplication.Commentsforpost;
import com.example.myapplication.Profilepage;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Postsearchadapter extends RecyclerView.Adapter<Postsearchadapter.MyViewHolder> {

    Context context;
    private FirebaseServices fbs;
    ArrayList<Post> postArrayList;
    private ArrayList<Profile> profile;
    private ArrayList<String> postref;


    public Postsearchadapter(Context context, ArrayList<Post> postArrayList,ArrayList<Profile> profile,ArrayList<String> postsref) {
        this.context = context;
        this.postArrayList = postArrayList;
        this.profile=profile;
        this.postref=postsref;
    }

    @NonNull
    @Override
    public Postsearchadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.post,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post=postArrayList.get(position);
        fbs =FirebaseServices.getInstance();
        StorageReference storageRef= fbs.getStorage().getInstance().getReference().child(post.getImage());

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur when downloading the image
            }
        });

        StorageReference storageRef1= fbs.getStorage().getInstance().getReference().child(profile.get(position).getImage());

        storageRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        holder.comment.setText(""+ post.getComments().size());
        holder.like.setText(""+post.getLikes().size());
        holder.share.setText(""+post.getShare());
        holder.username.setText(profile.get(position).getName());
        holder.caption.setText(post.getCaption());
        holder.like.setGravity(Gravity.CENTER);
        holder.like.setGravity(Gravity.RIGHT);
        holder.share.setGravity(Gravity.RIGHT);
        holder.comment.setGravity(Gravity.RIGHT);
        if(post.getLikes().size()!=0){
            for (int i=0;i<post.getLikes().size();i=i+1){
                if (post.getLikes().get(i).equals(fbs.getAuth().getCurrentUser().getEmail()))holder.likes.setImageResource(R.drawable.filledheart);
            }
        }
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.likes.getDrawable().getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.filledheart).getConstantState())) {
                    DocumentReference userRef = fbs.getFire().collection("Posts").document(postref.get(position));
                    userRef.get()
                            .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                                if (documentSnapshot.exists()) {
                                    ArrayList<String> likes=postArrayList.get(position).getLikes();
                                    likes.remove(fbs.getAuth().getCurrentUser().getEmail());
                                    documentSnapshot.getReference().update("likes", likes)
                                            .addOnSuccessListener(aVoid -> {
                                                System.out.println("ArrayList updated successfully.");
                                                holder.likes.setImageResource(R.drawable.heart);
                                                holder.like.setText(""+likes.size());
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
                    DocumentReference userRef = fbs.getFire().collection("Posts").document(postref.get(position));
                    userRef.get()
                            .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {

                                if (documentSnapshot.exists()) {
                                    ArrayList<String> likes=postArrayList.get(position).getLikes();
                                    likes.add(fbs.getAuth().getCurrentUser().getEmail());
                                    documentSnapshot.getReference().update("likes", likes)
                                            .addOnSuccessListener(aVoid -> {
                                                System.out.println("ArrayList updated successfully.");
                                                holder.likes.setImageResource(R.drawable.filledheart);
                                                holder.like.setText(""+likes.size());
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
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Commentsforpost(postref.get(position))).addToBackStack(null).commit();
            }
        });
        holder.shares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Profilepage(post.getUser())).addToBackStack(null).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView post;
        private ImageView user,comments,likes,shares;
        private TextView comment,like,share,username,caption;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.ivpost);
            comments=itemView.findViewById(R.id.ivcommentpost);
            user=itemView.findViewById(R.id.ivpostuser);
            likes=itemView.findViewById(R.id.ivlikepost);
            shares=itemView.findViewById(R.id.ivsharepost);
            username=itemView.findViewById(R.id.tvpostuser);
            share=itemView.findViewById(R.id.tvsharepost);
            like=itemView.findViewById(R.id.tvlikespost);
            comment=itemView.findViewById(R.id.tvcommentspost);
            caption=itemView.findViewById(R.id.tvCaption);
        }
    }
}
