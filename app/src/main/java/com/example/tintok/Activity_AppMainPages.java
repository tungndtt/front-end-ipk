package com.example.tintok;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.tintok.DataLayer.DataRepositoryController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_AppMainPages extends AppCompatActivity {


    BottomNavigationView navBar;
    Fragment peopleBrowsing, mediaSurfing, notification, messages, myHomepage;
    Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_pages);

        initActivity();

    }

    private void initActivity() {
        navBar = findViewById(R.id.navBar);
        peopleBrowsing =  new MainPages__PeopleBrowsing__Fragment();
        messages = new MainPages__Chatroom__Fragment();
        notification = new MainPages_Notification_Fragment();

        mediaSurfing = new MainPages_Posts_Fragment();
        myHomepage = new MainPages_MyProfile_Fragment(DataRepositoryController.getInstance().getUser());

        current = mediaSurfing;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainPageContent, current).commit();
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.mainPageContent, peopleBrowsing);
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.peoplebrowsing:
                        current = peopleBrowsing;
                        break;
                    case R.id.mediasurfing:
                        current = mediaSurfing;
                        break;
                    case R.id.notification:
                        current = notification;
                        break;
                    case R.id.messagers:
                        current = messages;
                        break;
                    case R.id.myProfile:
                        current = myHomepage;
                        break;
                }
                FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.animation_in, R.anim.animation_out);
                fragmentTransaction.addToBackStack(current.getTag()).replace(R.id.mainPageContent, current).commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(navBar.getSelectedItemId() == R.id.mediasurfing)
            super.onBackPressed();
        else{
            current = mediaSurfing;
            navBar.setSelectedItemId(R.id.mediasurfing);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("navBarID",navBar.getSelectedItemId());
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navBar.setSelectedItemId(savedInstanceState.getInt("navBarID"));
    }
}