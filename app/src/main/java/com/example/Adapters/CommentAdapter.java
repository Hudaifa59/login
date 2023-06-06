package com.example.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Classes.Comment;
import com.example.Classes.FirebaseServices;
import com.example.Classes.Profile;
import com.example.Classes.Reply;
import com.example.myapplication.Commentsforpost;
import com.example.myapplication.Profilepage;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    Context context;
    FirebaseServices fbs;
    ArrayList<Profile> profiles,profilespo;
    ArrayList<Comment> comments;
    ArrayList<String> commentspa,repliespa;

    Commentsforpost commentsforpost;
    ArrayList<Reply> reply;

    public CommentAdapter(Context context, ArrayList<Comment> comments, ArrayList<Profile> profiles, ArrayList<String> commentss, Commentsforpost commentsforpost) {
        this.profiles=profiles;
        this.context=context;
        this.comments=comments;
        this.commentspa=commentss;
        this.commentsforpost=commentsforpost;
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
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentsforpost.reply(commentspa.get(position));
            }
        });
        if (comment.getReply().size()==0)
        {
            ViewGroup parentView = (ViewGroup) holder.replies.getParent();
            parentView.removeView(holder.replies);
            holder.replies.setVisibility(View.GONE);
        }
        else {
            holder.replies.setText("view replies");
        }
        holder.replies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repliespa=comment.getReply();
                getreplies(holder.recyclerView);
            }
        });
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void eventonchangelis(RecyclerView recyclerView) {
        if (reply.size()==profilespo.size()) {
            ReplyAdapter replyAdapter = new ReplyAdapter(context,reply, repliespa,profilespo);
            recyclerView.setAdapter(replyAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerView;
        private ImageView user,like;
        private TextView comment,reply,username,likes,replies;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user=itemView.findViewById(R.id.commentprofile);
            like=itemView.findViewById(R.id.commentlike);
            likes=itemView.findViewById(R.id.Likesforcom);
            comment=itemView.findViewById(R.id.commentus);
            username=itemView.findViewById(R.id.commentuser);
            reply=itemView.findViewById(R.id.Reply);
            replies=itemView.findViewById(R.id.showreply);
            recyclerView=itemView.findViewById(R.id.replyrv);
        }
    }
    private void GetUser(String email,RecyclerView recyclerView) {
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
                        Getprofile(doc.get("profile").toString(),recyclerView);
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    }
                });
    }
    private void getreplies(RecyclerView recyclerView) {
        reply = new ArrayList<Reply>();
        int i=0;
        while (i<repliespa.size()) {
            DocumentReference userRef = fbs.getFire().collection("Reply").document(repliespa.get(i));
            userRef.get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                        if (documentSnapshot.exists()) {
                            reply.add(documentSnapshot.toObject(Reply.class));
                            GetUser(reply.get(reply.size()-1).getUser(),recyclerView);
                        } else {
                            System.out.println("User document doesn't exist.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Error retrieving user: " + e.getMessage());
                    });
            i++;
        }
    }
    private void Getprofile(String user,RecyclerView recyclerView){
        profilespo=new ArrayList<Profile>();
        DocumentReference userRef = fbs.getFire().collection("Profiles").document(user);
        userRef.get()
                .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                    if (documentSnapshot.exists()) {
                        Profile profile = documentSnapshot.toObject(Profile.class);
                        profilespo.add(profile);
                        eventonchangelis(recyclerView);
                    } else {
                        System.out.println("User document doesn't exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving user: " + e.getMessage());
                });
    }

}

