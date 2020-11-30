package com.example.tintok;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.example.tintok.Communication.Communication;
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


        // Enables Always-on
        Communication.getInstance().initScoket();
    }

    private void initActivity() {
        navBar = findViewById(R.id.navBar);
        peopleBrowsing =  new MainPages__PeopleBrowsing__Fragment();
        current = peopleBrowsing;
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
                FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction().replace(R.id.mainPageContent, current);
                fragmentTransaction.setCustomAnimations(R.anim.animation_in, R.anim.animation_out);
                fragmentTransaction.addToBackStack(current.getTag()).commit();
                return true;
            }
        });
    }

}