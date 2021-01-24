package com.example.tintok;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.tintok.DataLayer.DataRepositoryController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Activity_AppMainPages extends AppCompatActivity {


    BottomNavigationView navBar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    GestureDetector mGestureDetector;
    Fragment peopleBrowsing, mediaSurfing, notification, messages, myHomepage;
    Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_pages);

        initActivity();

    }

    private void initActivity() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navBar = findViewById(R.id.navBar);
        navView = findViewById(R.id.naviagtion_view);

        mGestureDetector = new GestureDetector(this, new SwipeGestureDetectorListener());
        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                Log.e("Activi_MainP1", "I touched");
                return true;
            }
        });

        peopleBrowsing =  new MainPages__PeopleBrowsing__Fragment();
        messages = new MainPages__Chatroom__Fragment();
        notification = new MainPages_Notification_Fragment();

        mediaSurfing = new MainPages_Posts_Fragment();
        myHomepage = new MainPages_MyProfile_Fragment(DataRepositoryController.getInstance().getUser().getValue());

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

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.refresh:

                        break;
                    case R.id.logout:
                        current = mediaSurfing;
                        Intent intent = new Intent(Activity_AppMainPages.this, Activity_Login_Signup.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.animation_in, R.anim.animation_out);
                        finish();
                        finish();
                        break;
                }
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

    public void showNavView(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void hideNavView(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public class SwipeGestureDetectorListener extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_DISTANCE_THRESHOLD = 70;
        private static final int SWIPE_VELOCITY_THRESHOLD = 70;
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(  Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD )
                if( velocityX > 0)
                    showNavView();
                else
                    hideNavView();
            return true;
        }
    }
}