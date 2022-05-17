package com.elmohandes.smart_learnning.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elmohandes.smart_learnning.R;
import com.elmohandes.smart_learnning.helpClasses.DeviceList;
import com.elmohandes.smart_learnning.helpClasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Attendence_screen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Attendence_screen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseUser auth;
    DatabaseReference reference;
    TextView txtCode;
    Button btnTakeAttendece;
    Button biometricDialog;
    TextView textHash;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Attendence_screen() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Attendence_screen newInstance(String param1, String param2) {
        Attendence_screen fragment = new Attendence_screen();
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
        View v = inflater.inflate(R.layout.fragment_attendence_screen, container,
                false);

        executor= ContextCompat.getMainExecutor(getContext());
        //fingerprint authentication
        biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @org.jetbrains.annotations.NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @org.jetbrains.annotations.NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
//                int hashcode = BiometricManager.from(getContext()).hashCode();
                startActivity(new Intent(getContext(), DeviceList.class));

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo=new BiometricPrompt.PromptInfo.Builder()
                .setTitle("test fingerprint")
                .setNegativeButtonText("cancel")
                .setConfirmationRequired(false)
                .build();


        txtCode=v.findViewById(R.id.txt_attendence_frag_code);

        auth= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        String id = auth.getUid();
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(user!=null){
                    String fullname=user.getFname();
                    String username=user.getUsername();
                    txtCode.setText("welcome "+fullname+"\ncode : "+username);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "something happening wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnTakeAttendece=v.findViewById(R.id.btn_attendence_take);
        btnTakeAttendece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BiometricManager biometricManager= BiometricManager.from(getContext());
                if(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                        != BiometricManager.BIOMETRIC_SUCCESS){
                    Toast.makeText(getContext(), "Biometric is not supported",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                biometricPrompt.authenticate(promptInfo);
            }
        });
        return v;
    }

}