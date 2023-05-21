package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.Classes.FirebaseServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    private FrameLayout fm;
    private BottomNavigationView miniicon;

    private FirebaseServices fbs;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        Connectcomp();
    }

    private void Connectcomp() {
        fbs=FirebaseServices.getInstance();
        fm=getActivity().findViewById(R.id.framehome);
        miniicon = getActivity().findViewById(R.id.navbarhome);
        miniicon.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homep:
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Profiles()).commit();
                    return true;
                case R.id.uppostic:
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framehome, new Uploadpost()).commit();
                    return true;
                case R.id.profileminip:
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framehome, new Profilepage(fbs.getAuth().getCurrentUser().getEmail())).commit();
                    return true;
                case R.id.searchp:
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framehome, new Search()).commit();
                    return true;
                default:
                    return false;
            }
        });
    }
}