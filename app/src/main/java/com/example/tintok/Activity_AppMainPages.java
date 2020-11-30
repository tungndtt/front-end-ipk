package com.example.tintok;

import android.os.Bundle;
import android.view.LayoutInflater;
<<<<<<< HEAD
import android.view.MenuItem;
=======
>>>>>>> upstream/master
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
<<<<<<< HEAD
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.example.tintok.Communication.Communication;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_AppMainPages extends AppCompatActivity {

    BottomNavigationView navBar;
    Fragment peopleBrowsing, mediaSurfing, notification, messages, myHomepage;
    Fragment current;
=======
import androidx.lifecycle.ViewModelProvider;

import com.example.tintok.Communication.Communication;

public class Activity_AppMainPages extends AppCompatActivity {

    private TextView mTextView;
>>>>>>> upstream/master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_pages);

<<<<<<< HEAD
        initActivity();

=======
        mTextView = (TextView) findViewById(R.id.text);
>>>>>>> upstream/master

        // Enables Always-on
        Communication.getInstance().initScoket();
    }

<<<<<<< HEAD
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

=======
>>>>>>> upstream/master
}