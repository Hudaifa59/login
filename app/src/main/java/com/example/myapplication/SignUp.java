package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.users.FirebaseServices;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp extends Fragment {
    private ImageView img;
    private FirebaseServices fbs;
    private EditText pass1,pass2,user;
    private Button btn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp newInstance(String param1, String param2) {
        SignUp fragment = new SignUp();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Override
    public void onStart() {
        super.onStart();
        Connectcom();
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    private void Connectcom() {
        img=getView().findViewById(R.id.bactoLogin);
        fbs=FirebaseServices.getInstance();
        btn=getView().findViewById(R.id.btnsign);
        pass1=getView().findViewById(R.id.supassword);
        pass2=getView().findViewById(R.id.Confirmsu);
        user=getView().findViewById(R.id.EtEmail);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framMain, new LoginFragment());
                ft.commit();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr=user.getText().toString();
                String pass=pass1.getText().toString();
                String confirmpass=pass2.getText().toString();
                if(usr.isEmpty()||pass.isEmpty()||confirmpass.isEmpty()){
                    Toast.makeText(getActivity(), "There is something missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isEmailValid(usr))
                {
                    Toast.makeText(getActivity(), "The Email is not valid!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidPassword(pass)) {
                    Toast.makeText(getActivity(), "The passwords is not valid!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check with confirm
                if(!confirmpass.equals(pass)){
                    Toast.makeText(getActivity(), "The passwords not matching!!", Toast.LENGTH_SHORT).show();
                }
                // login
                fbs.getAuth().createUserWithEmailAndPassword(usr, pass)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.framMain, new UserFrag());
                                    ft.commit();
                                } else {
                                    Toast.makeText(getActivity(), "The Email is already used!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }

        });
    }
}