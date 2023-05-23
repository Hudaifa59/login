package com.example.myapplication;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static com.example.myapplication.MainActivity.resizeImageToAspectRatio;
import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.Classes.FirebaseServices;
import com.example.Classes.Post;
import com.example.Classes.Profile;
import com.example.Classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Uploadpost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Uploadpost extends Fragment {
    private ImageView ivpost;
    private Bitmap Image;
    private FirebaseServices fbs;
    private EditText etcaption;
    private Button uploadbtn;
    private Post pst;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Uploadpost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Uploadpost.
     */
    // TODO: Rename and change types and number of parameters
    public static Uploadpost newInstance(String param1, String param2) {
        Uploadpost fragment = new Uploadpost();
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
        return inflater.inflate(R.layout.fragment_uploadpost, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
         ivpost=getView().findViewById(R.id.Uploadiv);
         fbs=FirebaseServices.getInstance();
         uploadbtn=getView().findViewById(R.id.uploadbtn);
         etcaption=getView().findViewById(R.id.ETcaptionup);
         ivpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
         });
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption=etcaption.getText().toString();
                if (etcaption.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "There are some fields missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                String path=UploadImageToFirebase();
                if(path==null)return;
                pst=new Post(path,caption,fbs.getAuth().getCurrentUser().getEmail());
                fbs.getFire().collection("Posts")
                            .add(pst)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    String postpath=documentReference.getId();
                                    Useradd(postpath);
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
        });
    }

    private void Useradd(String postpath) {
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
                        ArrayList<String> posts = (ArrayList<String>) doc.get("post");
                        posts.add(postpath);
                        doc.getReference().update("post", posts)
                                .addOnSuccessListener(aVoid -> {
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.framMain, new Profilepage(fbs.getAuth().getCurrentUser().getEmail()));
                                    ft.commit();
                                    System.out.println("ArrayList updated successfully.");
                                })
                                .addOnFailureListener(e -> {
                                    System.out.println("Error updating ArrayList: " + e.getMessage());
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });
    }


    private String UploadImageToFirebase(){
        BitmapDrawable drawable = (BitmapDrawable) ivpost.getDrawable();
        Image = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[]data= baos.toByteArray();
        StorageReference ref =fbs.getStorage().getReference("Posts/"+ UUID.randomUUID().toString());
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
            ivpost.setImageURI(selectedImage);
            float aspectRatio = 4.0f / 3.0f;
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
                Bitmap originalImage = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                Bitmap resizedImage = resizeImageToAspectRatio(originalImage, aspectRatio);
                float rotationAngle = 90f;
                Matrix matrix = new Matrix();
                matrix.postRotate(rotationAngle);
                Bitmap rotatedImage = Bitmap.createBitmap(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), matrix, true);
                ivpost.setImageBitmap(rotatedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}