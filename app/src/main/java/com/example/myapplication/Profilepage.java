package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.Classes.FirebaseServices;
import com.example.Classes.Minipostadapter;
import com.example.Classes.Post;
import com.example.Classes.Profile;
import com.example.Classes.ProfileAdapter;
import com.example.Classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profilepage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profilepage extends Fragment {

    ImageView imgpro;
    TextView postsnum,following,followers,username,nickname;
    private ArrayList<String> minipost,posts;
    private List<Post> postsp;
    private User user;
    private RecyclerView recyclerViewminipost;
    private Minipostadapter minipostadapter;
    private Profile profile;
    private String email;
    private FirebaseServices fbs;
    private Button signoubtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profilepage(String email) {
        this.email = email;
    }

    public Profilepage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profilepage.
     */
    // TODO: Rename and change types and number of parameters
    public static Profilepage newInstance(String param1, String param2) {
        Profilepage fragment = new Profilepage();
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
        return inflater.inflate(R.layout.fragment_profilepage, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        signoubtn=getView().findViewById(R.id.signoutbtn);
        signoubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbs.getAuth().signOut();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framMain,new LoginFragment()).commit();
            }
        });
        recyclerViewminipost = getActivity().findViewById(R.id.minipostrv);
        recyclerViewminipost.setHasFixedSize(true);
        recyclerViewminipost.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        minipostadapter = new Minipostadapter(new ArrayList<String>(),getActivity(),new ArrayList<>(),"",new ArrayList<>());
        recyclerViewminipost.setAdapter(minipostadapter);
        fbs=FirebaseServices.getInstance();
        minipost=new ArrayList<String>();
        postsp=new ArrayList<Post>();
        Recyclerview();
        GetUser(email);
    }

    private void EventChangeListener(ArrayList<String> minipost) {
        minipostadapter = new Minipostadapter(minipost,getActivity(),postsp,email,posts);
        recyclerViewminipost.setAdapter(minipostadapter);
    }
    private void Recyclerview() {
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
                        posts = (ArrayList<String>) doc.get("post");
                        Postarray();
                        setMinipost(posts);
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }
    private void setMinipost(ArrayList<String> posts) {
        int i=0;
        while (i<posts.size()){
            DocumentReference userRef = fbs.getFire().collection("Posts").document(posts.get(i));
            userRef.get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                        if (documentSnapshot.exists()) {
                            // The document exists, you can access its data
                            minipost.add( documentSnapshot.getString("image"));
                            EventChangeListener(minipost);
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
    private void Postarray(){
        int i=0;
        while (i<posts.size()){
            DocumentReference userRef = fbs.getFire().collection("Posts").document(posts.get(i));
            userRef.get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {

                        if (documentSnapshot.exists()) {
                                Post post=documentSnapshot.toObject(Post.class);
                                postsp.add(post);
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
    private void GetUser(String email) {
        fbs=FirebaseServices.getInstance();
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
                        user=doc.toObject(User.class);
                        Getprofile(doc.get("profile").toString());
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }
    private void Getprofile(String profiles){
        DocumentReference userRef = fbs.getFire().collection("Profiles").document(profiles);
        userRef.get()
                .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                    if (documentSnapshot.exists()) {
                        profile =documentSnapshot.toObject(Profile.class);
                        connect();
                    } else {
                        System.out.println("User document doesn't exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving user: " + e.getMessage());
                });

    }

    private void connect() {
        postsnum=getView().findViewById(R.id.postsprofilenum);
        following=getView().findViewById(R.id.followingprofilenum);
        followers=getView().findViewById(R.id.followersprofilenum);
        imgpro=getView().findViewById(R.id.profilepic);
        nickname=getView().findViewById(R.id.nicknameprofile);
        username=getView().findViewById(R.id.usernameprofile);
        StorageReference storageRef= fbs.getStorage().getInstance().getReference().child(profile.getImage());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity())
                        .load(uri)
                        .into(imgpro);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur when downloading the image
            }
        });
        postsnum.setText(user.getPost().size()+"");
        followers.setText(user.getFollowers().size()+"");
        following.setText(user.getFollowing().size()+"");
        nickname.setText(profile.getNickname());
        username.setText(profile.getName());
    }
}