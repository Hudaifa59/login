package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import users.FirebaseServices;
import users.Profile;
import users.ProfileAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profiles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profiles extends Fragment {
    private RecyclerView recyclerViewprofile;
    ArrayList<Profile> profileArrayList;
    ProfileAdapter profileAdapter;
    FirebaseServices fbs;
    private ProgressDialog progressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profiles() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profiles.
     */
    // TODO: Rename and change types and number of parameters
    public static Profiles newInstance(String param1, String param2) {
        Profiles fragment = new Profiles();
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
        return inflater.inflate(R.layout.fragment_profiles, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
/*
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data....");
        progressDialog.show();
*/
        recyclerViewprofile=getActivity().findViewById(R.id.profiles);
        recyclerViewprofile.setHasFixedSize(true);
        recyclerViewprofile.setLayoutManager(new LinearLayoutManager(getContext()));

        fbs=FirebaseServices.getInstance();
        profileArrayList=new ArrayList<Profile>();
        profileAdapter=new ProfileAdapter(getActivity(),profileArrayList);
        recyclerViewprofile.setAdapter(profileAdapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        fbs.getFire().collection("profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Profile profile = document.toObject(Profile.class);
                        profileArrayList.add(profile);
                    }

                    profileAdapter = new ProfileAdapter(getActivity(), profileArrayList);
                    recyclerViewprofile.setAdapter(profileAdapter);

                // TODO: fill data in recycler
            }
        }});


    }
}

/*
        fbs.getFire().collection("profile").orderBy("name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                //progressDialog = new ProgressDialog(getActivity());
                //progressDialog.setCancelable(false);
                //progressDialog.setMessage("Fetching data....");
                //progressDialog.show();

                if (error!=null){

                    //if (progressDialog.isShowing())
                        //progressDialog.dismiss();

                    Log.e("FireStore error",error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()){

                    if (dc.getType()==DocumentChange.Type.ADDED){

                        profileArrayList.add(dc.getDocument().toObject(Profile.class));

                    }

                    profileAdapter.notifyDataSetChanged();

                    //if (progressDialog.isShowing())
                        //progressDialog.dismiss();



                }
            }
        });*/