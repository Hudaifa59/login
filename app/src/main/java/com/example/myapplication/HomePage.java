package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Classes.FirebaseServices;
import com.example.Classes.Post;
import com.example.Adapters.Postsearchadapter;
import com.example.Classes.Profile;
import com.example.Classes.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {
    RecyclerView postsfollowing;
    ArrayList<String>following;
    FirebaseServices fbs;
    ArrayList<Profile>profiles;
    ArrayList<String> postpathforuser;
    ArrayList<Post> postforuser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
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
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        postsfollowing = getActivity().findViewById(R.id.PostsHome);
        postsfollowing.setHasFixedSize(true);
        postsfollowing.setLayoutManager(new LinearLayoutManager(getActivity()));
        profiles=new ArrayList<Profile>();
        postpathforuser=new ArrayList<String> ();
        postforuser=new ArrayList<Post> ();
        GetFollowings();
    }
    private void GetFollowings() {
        fbs= FirebaseServices.getInstance();
        fbs.getFire().collection("Users").whereEqualTo("user", fbs.getAuth().getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    if (querySnapshot.isEmpty()) {
                        System.out.println("No users found.");
                        return;
                    }

                    System.out.println("Number of users: " + querySnapshot.size());
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String userId = doc.getId();
                        following = (ArrayList<String>) doc.get("following");
                        Postarray();
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
                        Profile profile =documentSnapshot.toObject(Profile.class);
                        this.profiles.add(profile);
                    } else {
                        System.out.println("User document doesn't exist.");
                    }
                    evenonchange();
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving user: " + e.getMessage());
                });

    }
    private void GetUser(String users) {
        fbs.getFire().collection("Users").whereEqualTo("user", users)
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    if (querySnapshot.isEmpty()) {
                        System.out.println("No users found.");
                        return;
                    }
                    System.out.println("Number of users: " + querySnapshot.size());
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String userId = doc.getId();
                        User user = doc.toObject(User.class);
                        Getprofile(doc.get("profile").toString());
                    }
                }).addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }
    private void Postarray(){
        int i=0;
        while (i<following.size()) {
            fbs.getFire().collection("Posts").whereEqualTo("user", following.get(i))
                    .get()
                    .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                        if (querySnapshot.isEmpty()) {
                            System.out.println("No users found.");
                            return;
                        }
                        System.out.println("Number of users: " + querySnapshot.size());
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            String userId = doc.getId();
                            postforuser.add(doc.toObject(Post.class));
                            postpathforuser.add(doc.getId());
                            GetUser(doc.toObject(Post.class).getUser());
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Error retrieving users: " + e.getMessage());
                    });
            i++;
        }
    }
    private void evenonchange() {
        if (postforuser.size()==profiles.size()) {
            Postsearchadapter postAdapter = new Postsearchadapter(getActivity(), postforuser, profiles, postpathforuser);
            postsfollowing.setAdapter(postAdapter);
        }
    }
}