package com.example.tintok;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.CommunicationEvent;
import com.example.tintok.CustomView.AfterRefreshCallBack;
import com.example.tintok.CustomView.Refreshable;
import com.example.tintok.DataLayer.DataRepositiory_Chatrooms;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.DataLayer.DataRepository_Notificaitons;
import com.example.tintok.Model.ChatRoom;
import com.example.tintok.Model.MessageEntity;
import com.example.tintok.Model.Notification;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Activity_AppMainPages extends AppCompatActivity implements DataRepositiory_Chatrooms.OnNewMessagesListener, DataRepository_Notificaitons.OnNewNotificationListener,
        SwipeRefreshLayout.OnRefreshListener, AfterRefreshCallBack {


    BottomNavigationView navBar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    GestureDetector mGestureDetector;
    Fragment peopleBrowsing, mediaSurfing, notification, messages, myHomepage;
    Fragment current;

    SwipeRefreshLayout refreshLayout ;
    //GUI state
    int unseenNotifications;
    int unseenChatrooms;

    public static final String ITEM_MATCHING_PEOPLE = "matching_people";
    public static final String ITEM_POSTS = "posts";
    public static final String ITEM_NOTIFICATIONS = "notifications";
    public static final String ITEM_MESSENGER = "messages";
    public static final String ITEM_MYPROFILE = "profile";

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
        refreshLayout = findViewById(R.id.refreshLayout);

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

        mediaSurfing = new MainPages_Posts_Fragment(true, true);
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
                        ShowBadgeForNavBar(ITEM_NOTIFICATIONS, 0);
                        break;
                    case R.id.messagers:
                        current = messages;
                        ShowBadgeForNavBar(ITEM_MESSENGER, 0);
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
                        App.Logout(Activity_AppMainPages.this);
                        finish();
                        break;
                }
                return true;
            }
        });

        refreshLayout.setOnRefreshListener(this);
    }

    public void ShowBadgeForNavBar(String navBarItem, int number){
        int menuItemID;
        switch (navBarItem){
            case ITEM_MATCHING_PEOPLE:
                menuItemID = R.id.peoplebrowsing;
                break;
            case ITEM_POSTS:
                menuItemID = R.id.mediasurfing;
                break;
            case ITEM_NOTIFICATIONS:
                menuItemID = R.id.notification;
                break;
            case ITEM_MESSENGER:
                menuItemID = R.id.messagers;
                break;
            case ITEM_MYPROFILE:
                menuItemID = R.id.myProfile;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + navBarItem);
        }
        BadgeDrawable badge = navBar.getOrCreateBadge(menuItemID);
        if(number == 0){
            badge.setVisible(false);
            badge.clearNumber();
        }
        else if(number == -1){
            badge.setVisible(true);
            badge.clearNumber();
        }
        else badge.setNumber(number);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){
            int currentTab = intent.getIntExtra("currentTab",-1);
            if(currentTab!=-1)
                this.navBar.setSelectedItemId(currentTab);
        }
    }


    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        navBar.setSelectedItemId(savedInstanceState.getInt("navBarID"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Ac_mainpage", "called "+this.messages + this.notification);
        DataRepositoryController.getInstance().addNewMessageListener(this);
        DataRepositoryController.getInstance().addNotificationListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataRepositoryController.getInstance().removeNewMessageListener(this);
        DataRepositoryController.getInstance().removeNotificationListener(this);
    }

    public void showNavView(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void hideNavView(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void OnNotificationClicked(Notification noti) {

    }

    public void OnChatromClicked(ChatRoom r) {
    }


    @Override
    public void onNewMessage(String roomID, MessageEntity msg) {
        unseenChatrooms++;
        this.ShowBadgeForNavBar(ITEM_MESSENGER, unseenChatrooms);
        Log.e("Ac_mainpage", "new msg");
    }

    @Override
    public void onNewNotification(Notification newNoti) {
        unseenNotifications++;
        this.ShowBadgeForNavBar(ITEM_NOTIFICATIONS, unseenNotifications);
    }

    @Override
    public void onRefresh() {
        try{
            ((Refreshable)current).onRefresh(this);
            refreshLayout.setRefreshing(true);
        }catch (Exception e){
            Log.e("MainPage", "current Fragment cant be refresh");
        }
    }

    @Override
    public void onRefreshingDone() {
        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
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