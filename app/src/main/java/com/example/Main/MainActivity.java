package com.example.Main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.Classes.FirebaseServices;
import com.example.Main.LoginFragment;
import com.example.Main.UserFrag;
import com.example.myapplication.Home;
import com.example.myapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fm;
    private FirebaseServices fbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        connectcommponents();
        fbs=FirebaseServices.getInstance();
        if(fbs.getAuth().getCurrentUser()!=null) {
            check();
        }

        else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.framMain, LoginFragment.class, null)
                    .commit();
        }
    }
    private void connectcommponents() {
        fm=findViewById(R.id.framMain);
    }
    public static Bitmap resizeImageToAspectRatio(Bitmap originalImage, float aspectRatio) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int newWidth, newHeight;

        if (originalWidth > originalHeight) {
            newWidth = Math.round(originalHeight * aspectRatio);
            newHeight = originalHeight;
        } else {
            newWidth = originalWidth;
            newHeight = Math.round(originalWidth / aspectRatio);
        }

        return Bitmap.createScaledBitmap(originalImage, newWidth, newHeight, false);
    }
    public void check() {
        fbs = FirebaseServices.getInstance();
        fbs.getFire().collection("Users").whereEqualTo("user", fbs.getAuth().getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    if (querySnapshot.isEmpty()) {
                        System.out.println("No users found.");
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.framMain, UserFrag.class, null)
                                .commit();
                        return;
                    }

                    System.out.println("Number of users: " + querySnapshot.size());

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String userId = doc.getId();
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.framMain, Home.class, null)
                                .commit();

                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving users: " + e.getMessage());
                });

    }
}