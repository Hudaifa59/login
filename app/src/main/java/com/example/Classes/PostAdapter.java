package com.example.Classes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context context;
    private FirebaseServices fbs;
    ArrayList<Post> postArrayList;
    ArrayList<String> profile;
    ArrayList<Profile> profiles;

    public PostAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        profile=new ArrayList<String> ();
        profiles=new ArrayList<Profile>() ;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.post,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {

        Post post=postArrayList.get(position);
        fbs =FirebaseServices.getInstance();
        Getprofile(post.getUser());
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

        StorageReference storageRef1= fbs.getStorage().getInstance().getReference().child(profiles.get(0).getImage());

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
        holder.comment.setText(post.getComments().size());
        holder.like.setText(post.getLikes().size());
        holder.share.setText(post.getShare());
        holder.username.setText(profiles.get(0).getNickname());
        holder.caption.setText(post.getCaption());
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
    private void Getprofile(String email) {
        fbs.getFire().collection("Users").whereEqualTo("user", email)
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    if (querySnapshot.isEmpty()) {
                        System.out.println("No users found.");
                        return;
                    }

                    System.out.println("Number of users: " + querySnapshot.size());

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String userId = doc.getId();
                        profile = (ArrayList<String>) doc.get("profile");
                        Postarray();
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }
    private void Postarray(){
        int i=0;
        while (i<profile.size()){
            DocumentReference userRef = fbs.getFire().collection("Posts").document(profile.get(i));
            userRef.get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                        if (documentSnapshot.exists()) {
                            // The document exists, you can access its data
                            Profile profile1 =documentSnapshot.toObject(Profile.class);
                            profiles.add(profile1);
                        } else {
                            System.out.println("User document doesn't exist.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Error retrieving user: " + e.getMessage());
                    });
            i=i+1;
        }
    }
}
