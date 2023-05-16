package com.example.myapplication;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import com.example.Classes.FirebaseServices;
import com.example.Classes.Profile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFrag extends Fragment {

    private String pfpath;
    private User us;
    private Bitmap Image;
    private Profile pf;
    private FirebaseServices fbs;
    private Button btndone;
    private ImageView imgp;
    private EditText ename,enick,ephone;
    private Spinner gender;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFrag newInstance(String param1, String param2) {
        UserFrag fragment = new UserFrag();
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connectcomp();
    }

    private void connectcomp() {
        fbs=FirebaseServices.getInstance();
        btndone=getView().findViewById(R.id.Done);
        imgp = getView().findViewById(R.id.profileimage);
        ename = getView().findViewById(R.id.Username);
        enick = getView().findViewById(R.id.enickname);
        ephone = getView().findViewById(R.id.ephone);
        gender = getView().findViewById(R.id.gendersp);
        imgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=ephone.getText().toString();
                if (enick.getText().toString().isEmpty() || ename.getText().toString().isEmpty() || gender.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "There are some fields missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ephone.getText().toString().isEmpty())
                    phone="";
                String path=UploadImageToFirebase();
                if(path==null)return;
                pf=new Profile(enick.getText().toString(),ename.getText().toString(),gender.getSelectedItem().toString(),phone,path,0);
                fbs.getFire().collection("Profiles")
                        .add(pf)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                pfpath=documentReference.getId();
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Useradd(pfpath);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

            }
        });
    }

    private String UploadImageToFirebase(){
        BitmapDrawable drawable = (BitmapDrawable) imgp.getDrawable();
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
            imgp.setImageURI(selectedImage);
        }
    }
    public void Useradd(String ref){
        ArrayList<String> s=new ArrayList<String>();
        us=new User(pf.getName(), s,s,pfpath,fbs.getAuth().getCurrentUser().getEmail().toString(),s);
        fbs.getFire().collection("Users")
                .add(us)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.framMain, new Home());
                        ft.commit();
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