package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.Classes.FirebaseServices;
import com.example.Classes.Post;
import com.example.Classes.PostAdapter;
import com.example.Classes.Postsearchadapter;
import com.example.Classes.Profile;
import com.example.Classes.ProfileAdapter;
import com.example.Classes.Searchadapter;
import com.example.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {
    FirebaseServices fbs;
    ArrayList<User> userArrayList;
    ArrayList<Profile> profiles;
    ArrayList<Profile> profilespo;
    ArrayList<User>userArrayList1;
    ArrayList<Profile> searchArraylistprofiles;

    private Postsearchadapter postAdapter;
    ArrayList<Post> posts;
    ArrayList<String> postsref;

    private Searchadapter searchadapter;
    private RecyclerView recyclerViewprofile;

    ArrayList<User> searchArraylist;
    SearchView searchView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerViewprofile = getActivity().findViewById(R.id.recyclerviewsearch);
        recyclerViewprofile.setHasFixedSize(true);
        recyclerViewprofile.setLayoutManager(new LinearLayoutManager(getActivity()));
        fbs=FirebaseServices.getInstance();
        posts=new ArrayList<Post>();
        postsref=new ArrayList<String>();
        profiles=new ArrayList<Profile>();
        profilespo=new ArrayList<Profile>();
        GetUsers();
        GetPosts();
        searchView=getView().findViewById(R.id.searchviewhome);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length()>0){
                    searchArraylist=new ArrayList<User>();
                    searchArraylistprofiles=new ArrayList<Profile>();
                    for (int i=0;i<profiles.size();i++){
                        boolean n=false;
                        if (profiles.get(i).getName().contains(s)&&!n){
                            searchArraylistprofiles.add(profiles.get(i));
                            for (int j=0;j<userArrayList.size();j++)
                                if (userArrayList.get(j).getUsername().equals(profiles.get(i).getName())&&!n) {
                                    searchArraylist.add(userArrayList.get(j));
                                    n=true;
                                }
                        }
                    }
                    searchadapter = new Searchadapter(getActivity(),searchArraylist,searchArraylistprofiles);
                    recyclerViewprofile.setAdapter(searchadapter);
                }
                else {
                    GetPosts();
                }
                return false;
            }
        });
    }
    private void GetPosts() {
        fbs= FirebaseServices.getInstance();
        fbs.getFire().collection("Posts")
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    if (querySnapshot.isEmpty()) {
                        System.out.println("No users found.");
                        return;
                    }

                    System.out.println("Number of users: " + querySnapshot.size());

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String userId = doc.getId();
                        Post post=doc.toObject(Post.class);
                        posts.add(post);
                        postsref.add(doc.getId());
                        GetUser(post.getUser());
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }
    private void GetUsers() {
        userArrayList=new ArrayList<User>();
        fbs= FirebaseServices.getInstance();
        fbs.getFire().collection("Users")
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    if (querySnapshot.isEmpty()) {
                        System.out.println("No users found.");
                        return;
                    }
                    System.out.println("Number of users: " + querySnapshot.size());

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String userId = doc.getId();
                        User user=doc.toObject(User.class);
                        userArrayList.add(user);
                    }
                    Getprofiles();
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }
    private void Getprofiles(){
        int i=0;
        while (i<userArrayList.size()) {
            DocumentReference userRef = fbs.getFire().collection("Profiles").document(userArrayList.get(i).getProfile());
            userRef.get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                        if (documentSnapshot.exists()) {
                            Profile profile = documentSnapshot.toObject(Profile.class);
                            profiles.add(profile);
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
    private void Getprofile(String user){
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
    private void GetUser(String email) {
        userArrayList1=new ArrayList<User>();
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
                        userArrayList1.add(doc.toObject(User.class));
                        Getprofile(doc.toObject(User.class).getProfile());

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

    private void evenonchange() {
        if (postsref.size()==profilespo.size()) {
            ArrayList<Profile> profilefinal =new ArrayList<Profile>();
            for (int i=0;i<posts.size();i++){
                boolean n = false;
                for (int j=0;userArrayList.size()>j;j++) {
                        for (int k = 0; k < profiles.size(); k++) {
                            if (posts.get(i).getUser().equals(userArrayList.get(j).getUser())&&userArrayList.get(j).getUsername().equals(profiles.get(k).getName()) && !n) {
                                profilefinal.add(profiles.get(k));
                                n = true;
                            }
                        }
                }
            }
            postAdapter = new Postsearchadapter(getActivity(), posts, profilefinal, postsref);
            recyclerViewprofile.setAdapter(postAdapter);
        }
    }
}