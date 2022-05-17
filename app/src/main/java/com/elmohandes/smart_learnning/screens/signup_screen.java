package com.elmohandes.smart_learnning.screens;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.elmohandes.smart_learnning.R;
import com.elmohandes.smart_learnning.helpClasses.MainActivity;
import com.elmohandes.smart_learnning.helpClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class signup_screen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth mAuth;
    Button register;
    EditText email,username,mobile,password,fname;
    ProgressDialog signUpDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signup_screen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signup_screen.
     */
    // TODO: Rename and change types and number of parameters
    public static signup_screen newInstance(String param1, String param2) {
        signup_screen fragment = new signup_screen();
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
        View v =inflater.inflate(R.layout.fragment_signup_screen, container, false);
        username=v.findViewById(R.id.et_signup_frag_username);
        fname=v.findViewById(R.id.et_signup_fullName);
        mobile=v.findViewById(R.id.et_signup_frag_mobilePhone);
        email=v.findViewById(R.id.et_signup_frag_email);
        password=v.findViewById(R.id.et_signup_frag_password);
        mAuth=FirebaseAuth.getInstance();
        register=v.findViewById(R.id.btn_signup_frag_register);

        signUpDialog = new ProgressDialog(getContext());
        signUpDialog.setMessage("saving information...");
        signUpDialog.setCancelable(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerMethod();
            }
        });
        return v;
    }

    private void registerMethod(){
        final String str_fname=fname.getText().toString(),
                str_email=email.getText().toString()
                ,str_mobile=mobile.getText().toString(),
                str_pass=password.getText().toString()
                        ,str_username=username.getText().toString();

        if(str_username.isEmpty()){
            username.setError("username required");
            username.requestFocus();
            return;
        }

        if(str_fname.isEmpty()){
            fname.setError("full name is required");
            fname.requestFocus();
            return;
        }

        if(str_mobile.isEmpty()){
            mobile.setError("age is required");
            mobile.requestFocus();
            return;
        }

        if(str_email.isEmpty()){
            email.setError("email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            email.setError("please enter a valid email");
            email.requestFocus();
            return;
        }

        if(str_pass.isEmpty()){
            password.setError("password is required");
            password.requestFocus();
            return;
        }
        if(str_pass.length()<6){
            password.setError("minimum charcters is 6");
            password.requestFocus();
            return;
        }

        signUpDialog.show();
        mAuth.createUserWithEmailAndPassword(str_email,str_pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(str_username,str_email,str_mobile,str_fname);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(getContext(), "you are registered successfully",
                                                Toast.LENGTH_SHORT).show();
                                        signUpDialog.dismiss();
                                        startActivity(new Intent(getContext(),MainActivity.class));
                                    }else{
                                        signUpDialog.dismiss();
                                        Toast.makeText(getContext(), "failed registered to database",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }else {
                            Toast.makeText(getContext(), "failed registered to add Account",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}