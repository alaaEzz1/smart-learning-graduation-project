package com.elmohandes.smart_learnning.helpClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.elmohandes.smart_learnning.R;
import com.elmohandes.smart_learnning.screens.Student_screen;
import com.elmohandes.smart_learnning.screens.login_screen;
import com.elmohandes.smart_learnning.screens.splash_screen;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                login_screen screen= new login_screen();
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.mainFrag,screen);
                ft.commit();

            }
        },3000);

        splash_screen fragScreen= new splash_screen();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFrag,fragScreen);
        ft.commit();


    }
}