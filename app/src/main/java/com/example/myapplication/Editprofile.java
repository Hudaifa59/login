package com.example.myapplication;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.Classes.FirebaseServices;
import com.example.Classes.Profile;
import com.example.Classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Editprofile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Editprofile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseServices fbs;
    private User user;
    private Profile profile;

    boolean pick=false;
    EditText username,nickname;
    ImageView profilepic,backbt;
    Button btnedit;
    private Bitmap Image;
    String email;
    String userid,profileid;


    public Editprofile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Editprofile.
     */
    // TODO: Rename and change types and number of parameters
    public static Editprofile newInstance(String param1, String param2) {
        Editprofile fragment = new Editprofile();
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
        return inflater.inflate(R.layout.fragment_editprofile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        profilepic=getView().findViewById(R.id.proedit);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
        fbs=FirebaseServices.getInstance();
        btnedit=getView().findViewById(R.id.editco);
        username=getView().findViewById(R.id.Usernameed);
        nickname=getView().findViewById(R.id.enicknameed);
        backbt=getView().findViewById(R.id.backbtnsett);
        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pick||!username.getText().toString().equals(profile.getName())||!nickname.getText().toString().equals(profile.getNickname())){
                    if (username.getText().toString().equals(profile.getName())) {
                       areyousure();
                    }else
                    {
                        Check(username.getText().toString());
                    }
                }
                else Toast.makeText(getActivity(), "You didn't change anything", Toast.LENGTH_SHORT).show();
            }
        });
        email=fbs.getAuth().getCurrentUser().getEmail();
        GetUser(email);
    }

    void areyousure(){
        new AlertDialog.Builder(getContext()).setMessage("Are you sure want to Edit your profile?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Edit();
                    }
                }).setNegativeButton("No", null)
                .show();
    }


    private void Edit() {
        String user=username.getText().toString();
        String nick=nickname.getText().toString();
        if(pick){
            StorageReference photoRef = fbs.getStorage().getReference().child(profile.getImage());
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    profile.setImage(UploadImageToFirebase());
                    profile.setName(user);
                    profile.setNickname(nick);
                    DocumentReference userRef = fbs.getFire().collection("Profiles").document(profileid);
                    userRef.set(profile)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Profilepage()).commit();
                                }
                            })
                            .addOnFailureListener(e -> {
                                System.out.println("Error retrieving user: " + e.getMessage());
                            });
                }}).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any errors that occurred during deletion
                }
            });
        }else {
            profile.setName(user);
            profile.setNickname(nick);
            DocumentReference userRef = fbs.getFire().collection("Profiles").document(profileid);
            userRef.set(profile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framehome,new Profilepage()).commit();
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Error retrieving user: " + e.getMessage());
                    });
        }
    }
    private void Check(String name) {
        fbs.getFire().collection("Users").whereEqualTo("username", name)
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    if (querySnapshot.isEmpty()) {
                        System.out.println("No users found.");
                        areyousure();
                    }
                    else {
                        Toast.makeText(getActivity(), "This username is already used", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println("Number of users: " + querySnapshot.size());
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }
    private String UploadImageToFirebase(){
        BitmapDrawable drawable = (BitmapDrawable) profilepic.getDrawable();
        Image = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[]data= baos.toByteArray();
        StorageReference ref =fbs.getStorage().getReference("listingPictures/"+ UUID.randomUUID().toString());
        UploadTask uploadTask =ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error with the picture", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
        return ref.getPath();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&& data != null){
            Uri selectedImage= data.getData();
            profilepic.setImageURI(selectedImage);
            pick=true;
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
                        userid = doc.getId();
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
                        profileid=documentSnapshot.getId();
                        connct();
                    } else {
                        System.out.println("User document doesn't exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving user: " + e.getMessage());
                });
    }

    private void connct() {
        StorageReference storageRef= fbs.getStorage().getInstance().getReference().child(profile.getImage());
        if (!pick){
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getContext())
                            .load(uri)
                            .into(profilepic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any errors that occur when downloading the image
                }
            });
        }

        username=getView().findViewById(R.id.Usernameed);
        nickname=getView().findViewById(R.id.enicknameed);
        username.setText(profile.getName());
        nickname.setText(profile.getNickname());
    }

}