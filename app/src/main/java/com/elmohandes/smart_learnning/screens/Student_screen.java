package com.elmohandes.smart_learnning.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.elmohandes.smart_learnning.helpClasses.PagerAdapter;
import com.elmohandes.smart_learnning.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Student_screen extends AppCompatActivity {

    TabLayout mainTapStudent;
    TabItem studentAttendence , studentBus;
    ViewPager studentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_screen);

        mainTapStudent=findViewById(R.id.main_student_tap);
        studentAttendence=findViewById(R.id.itm_main_student_attendence);
        studentBus=findViewById(R.id.itm_main_student_bus);
        studentPager=findViewById(R.id.Student_pager);

        PagerAdapter pagerAdapter= new PagerAdapter(getSupportFragmentManager(),mainTapStudent.getTabCount());
        studentPager.setAdapter(pagerAdapter);

        mainTapStudent.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                studentPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

//                Toast.makeText(Student_screen.this, "onTabUnselected",
//                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

//                Toast.makeText(Student_screen.this, "onTabReselected",
//                        Toast.LENGTH_SHORT).show();
            }
        });

    }


}