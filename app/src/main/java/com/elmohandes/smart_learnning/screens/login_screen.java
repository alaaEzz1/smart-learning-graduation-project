package com.elmohandes.smart_learnning.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elmohandes.smart_learnning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link login_screen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class login_screen extends Fragment {

    Button btnRegister;
    Button btn_login;
    EditText etEmail,etPassword;
    FirebaseAuth auth;
    ProgressDialog loginDialog;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public login_screen() {
        // Required empty public constructor
    }

    public static login_screen newInstance(String param1, String param2) {
        login_screen fragment = new login_screen();
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
        View v=inflater.inflate(R.layout.fragment_login_screen, container, false);
        btnRegister=v.findViewById(R.id.btn_login_frag_rsgister);
        btn_login=v.findViewById(R.id.btn_login_frag_login);
        etEmail=v.findViewById(R.id.et_login_frag_email);
        etPassword=v.findViewById(R.id.et_login_frag_password);

        loginDialog = new ProgressDialog(getContext());
        loginDialog.setMessage("loading...");
        loginDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_screen fragScreen= new signup_screen();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.mainFrag,fragScreen);
                ft.commit();
            }
        });

        auth=FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInUser();
            }
        });
        return v;
    }

    //method for signin Validation data, Empty fields and validation number of characters
    public void SignInUser(){
        String str_email=etEmail.getText().toString();
        String str_pass=etPassword.getText().toString().trim();

        if(str_email.isEmpty()){
            etEmail.setError("email is required");
            etEmail.requestFocus();
            return;
        }

        if(str_pass.isEmpty()){
            etPassword.setError("password is required");
            etPassword.requestFocus();
            return;
        }

        if(str_pass.length()<6){
            etPassword.setError("minimum charcters is 6");
        }
        loginDialog.show();
        auth.signInWithEmailAndPassword(str_email,str_pass).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "successfully", Toast.LENGTH_SHORT)
                                    .show();
                            startActivity(new Intent(getContext(),Student_screen.class));
                            //important to finish login screen and don't return to it again
                            loginDialog.dismiss();
                            getActivity().finish();

                        }else{
                            loginDialog.dismiss();
                            Toast.makeText(getContext(), "not accurate try again",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    }

