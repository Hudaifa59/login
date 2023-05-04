package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.Classes.FirebaseServices;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fm;
    private FirebaseServices fbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectcommponents();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.framMain, LoginFragment.class, null)
                    .commit();
        }
    }
    private void connectcommponents() {
        fm=findViewById(R.id.framMain);
    }

}