package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Classes.FirebaseServices;
import com.example.Classes.Minipostadapter;
import com.example.Classes.Profile;
import com.example.Classes.ProfileAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profilepage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profilepage extends Fragment {

    private ArrayList<String> minipost;

    private RecyclerView recyclerViewprofile;
    private Minipostadapter minipostadapter;

    private FirebaseServices fbs;
    private Button signoubtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        recyclerViewprofile = getActivity().findViewById(R.id.minipostrv);
        recyclerViewprofile.setHasFixedSize(true);
        recyclerViewprofile.setLayoutManager(new LinearLayoutManager(getActivity()));
        minipostadapter = new Minipostadapter(new ArrayList<>(),getActivity());
        recyclerViewprofile.setAdapter(minipostadapter);
        fbs=FirebaseServices.getInstance();
        Recyclerview();
    }

    private void EventChangeListener(ArrayList<String>minipost) {

        minipostadapter = new Minipostadapter(minipost,getActivity());
        recyclerViewprofile.setAdapter(minipostadapter);

    }
    private void Recyclerview() {
        String userEmail = fbs.getAuth().getCurrentUser().getEmail();
        fbs.getFire().collection("Users").whereEqualTo("user", userEmail)
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    if (querySnapshot.isEmpty()) {
                        System.out.println("No users found.");
                        return;
                    }

                    System.out.println("Number of users: " + querySnapshot.size());

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String userId = doc.getId();
                        minipost = (ArrayList<String>) doc.get("post");
                        EventChangeListener(minipost);
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }
}