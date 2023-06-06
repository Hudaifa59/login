package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Classes.FirebaseServices;
import com.example.Classes.Post;
import com.example.Adapters.PostAdapter;
import com.example.Classes.Profile;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Postsprofile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Postsprofile extends Fragment {

    private RecyclerView recyclerViewpost;
    private PostAdapter postAdapter;
    private String email;
    FirebaseServices fbs;
    ImageView btn;
    private Profile profile;
    private ArrayList<String> postref;
    private ArrayList<Post> postArrayList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Postsprofile() {
    }

    public Postsprofile(ArrayList<Post> postArrayList,String email,ArrayList<String> postsref) {
        this.postArrayList = postArrayList;
        this.email=email;
        GetUser(email);
        this.postref=postsref;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Postsprofile.
     */
    // TODO: Rename and change types and number of parameters
    public static Postsprofile newInstance(String param1, String param2) {
        Postsprofile fragment = new Postsprofile();
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
        return inflater.inflate(R.layout.fragment_postsprofile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        btn=getView().findViewById(R.id.backbtnposts);
        recyclerViewpost = getActivity().findViewById(R.id.recycleviewposts);
        recyclerViewpost.setHasFixedSize(true);
        recyclerViewpost.setLayoutManager(new LinearLayoutManager(getActivity()));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

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
                        eventonchange();
                    } else {
                        System.out.println("User document doesn't exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving user: " + e.getMessage());
                });

    }

    private void eventonchange() {
        postAdapter = new PostAdapter(getActivity(),postArrayList,profile,postref);
        recyclerViewpost.setAdapter(postAdapter);
    }
}