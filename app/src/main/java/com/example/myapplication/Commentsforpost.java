package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Classes.Comment;
import com.example.Adapters.CommentAdapter;
import com.example.Classes.FirebaseServices;
import com.example.Classes.Post;
import com.example.Classes.Profile;
import com.example.Classes.Reply;
import com.example.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Commentsforpost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Commentsforpost extends Fragment {

    ArrayList<Profile> profilespo;
    private String path;
    String pathcomm;

    ArrayList<User>userArrayList;
    ImageView backbtn;
    FirebaseServices fbs;
    EditText comment;
    TextView share;
    ArrayList<String> compath;
    ArrayList<Comment> comments;
    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Commentsforpost(String path) {
        this.path=path;
    }
    public Commentsforpost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Commentsforpost.
     */
    // TODO: Rename and change types and number of parameters
    public static Commentsforpost newInstance(String param1, String param2) {
        Commentsforpost fragment = new Commentsforpost();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commentsforpost, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        fbs=FirebaseServices.getInstance();
        recyclerView = getActivity().findViewById(R.id.commentrv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        share=getView().findViewById(R.id.shareclick);
        comment=getView().findViewById(R.id.commentet);
        backbtn=getView().findViewById(R.id.backcomm);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        getpost();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment1=comment.getText().toString();
                if (comment1.isEmpty())
                {
                    Toast.makeText(getActivity(), "There isn't a text", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(comment.getHint().equals("Reply to the comment")){
                    Reply comment2 = new Reply(comment1, fbs.getAuth().getCurrentUser().getEmail());
                    fbs.getFire().collection("Reply")
                            .add(comment2)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    comment.setText(""); // Clear the text
                                    Eventonchangereply(documentReference.getId());
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
                else {
                    Comment comment2 = new Comment(comment1, fbs.getAuth().getCurrentUser().getEmail());
                    fbs.getFire().collection("Comments")
                            .add(comment2)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    comment.setText("");
                                    Eventonchange(documentReference.getId());
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
            }
        });
    }
    private void Eventonchangereply(String id) {
        DocumentReference userRef = fbs.getFire().collection("Comments").document(pathcomm);
        userRef.get()
                .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                    if (documentSnapshot.exists()) {
                        Comment post =documentSnapshot.toObject(Comment.class);
                        ArrayList<String> comments=post.getReply();
                        comments.add(id);
                        documentSnapshot.getReference().update("reply", comments)
                                .addOnSuccessListener(aVoid -> {
                                    System.out.println("ArrayList updated successfully.");
                                    getpost();
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
    private void Eventonchange(String id) {
        DocumentReference userRef = fbs.getFire().collection("Posts").document(path);
        userRef.get()
                .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                    if (documentSnapshot.exists()) {
                        Post post =documentSnapshot.toObject(Post.class);
                        ArrayList<String> comments=post.getComments();
                        comments.add(id);
                        documentSnapshot.getReference().update("comments", comments)
                                .addOnSuccessListener(aVoid -> {
                                    System.out.println("ArrayList updated successfully.");
                                    getpost();
                                    comment.setHint("Your Comment...");
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

    private void getcomments() {
        comments=new ArrayList<Comment>();
        int i=0;
        while (i<compath.size()) {
            DocumentReference userRef = fbs.getFire().collection("Comments").document(compath.get(i));
            userRef.get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                        if (documentSnapshot.exists()) {
                            comments.add(documentSnapshot.toObject(Comment.class));
                            GetUser(comments.get(comments.size()-1).getUser());
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

    private void getpost() {
        DocumentReference userRef = fbs.getFire().collection("Posts").document(path);
        userRef.get()
                .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                    if (documentSnapshot.exists()) {
                        Post post=documentSnapshot.toObject(Post.class);
                        compath=post.getComments();
                        getcomments();
                    } else {
                        System.out.println("User document doesn't exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving user: " + e.getMessage());
                });
    }
    private void GetUser(String email) {
        userArrayList=new ArrayList<User>();
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
                        userArrayList.add(doc.toObject(User.class));
                        Getprofile(doc.get("profile").toString());
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
    private void Getprofile(String user){
        profilespo=new ArrayList<Profile>();
        DocumentReference userRef = fbs.getFire().collection("Profiles").document(user);
        userRef.get()
                .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                    if (documentSnapshot.exists()) {
                        Profile profile = documentSnapshot.toObject(Profile.class);
                        profilespo.add(profile);
                        evenonchange();
                    } else {
                        System.out.println("User document doesn't exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving user: " + e.getMessage());
                });
    }

    private void evenonchange() {
        if (comments.size()==profilespo.size()) {
            ArrayList<Profile> profilefinal =new ArrayList<Profile>();
            for (int i=0;i<comments.size();i++){
                boolean n = false;
                for (int j=0;userArrayList.size()>j;j++) {
                    for (int k = 0; k < profilespo.size(); k++) {
                        if (comments.get(i).getUser().equals(userArrayList.get(j).getUser())&&userArrayList.get(j).getUsername().equals(profilespo.get(k).getName()) && !n) {
                            profilefinal.add(profilespo.get(k));
                            n = true;
                        }
                    }
                }
            }
            CommentAdapter commentAdapter = new CommentAdapter(getActivity(),comments, profilefinal,compath,this);
            recyclerView.setAdapter(commentAdapter);
        }
    }
    public void reply(String pathcomm){
        TextView share=getView().findViewById(R.id.shareclick);
        comment.setHint("Reply to the comment");
        this.pathcomm=pathcomm;
    }
}