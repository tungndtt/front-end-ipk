package com.example.tintok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.Adapters_ViewHolder.PostAdapter;
import com.example.tintok.CustomView.PostUploadFragment;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class Activity_ViewProfile extends AppCompatActivity{

    private Activity_ViewProfile_ViewModel viewModel;

    private ImageView profile_pic;
    private Fragment infoFragment, imageFragment;
    private int selected;
    TextView username;
    private TextView followingNumber, followerNumber;
    private MaterialButton followBtn, messageBtn;
    BottomNavigationView profile_navigation_bar;
    private View_Profile_Picture_Fragment viewProfilePictureFragment;
    Fragment postFragment;
    MaterialToolbar toolbar;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //this.viewModel.setFragment(this);
        // info of displayed user. Currently just user name for testing
        username = findViewById(R.id.profile_name);
        followingNumber = findViewById(R.id.followingsNumber);
        followerNumber = findViewById(R.id.follwersNumber);
        followBtn = findViewById(R.id.followBtn);
        messageBtn = findViewById(R.id.messageBtn);
        toolbar = findViewById(R.id.view_profile_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        profile_navigation_bar = findViewById(R.id.profile_navigation_bar);
        profile_navigation_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selected = item.getItemId();
                if (item.getItemId() == R.id.profile_info_item)
                    getSupportFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, infoFragment).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
                return true;
            }
        });
       // infoFragment = ViewProfile_UserInfo_Fragment.getInstance();//Info_Profile_Fragment.getInstance();

        this.viewModel = new ViewModelProvider(this).get(Activity_ViewProfile_ViewModel.class);
        String author_id = getIntent().getStringExtra("author_id");
        this.viewModel.getUserProfile(author_id);
        Log.e("Act", viewModel.toString());
        infoFragment = ViewProfile_UserInfo_Fragment.getInstance();//(viewModel);//Info_Profile_Fragment.getInstance();
       // imageFragment = ViewProfile_UserImages_Fragment.getInstance();
        this.selected = R.id.profile_info_item;
        profile_navigation_bar.setSelectedItemId( this.selected);


       // String author_id = getIntent().getStringExtra("author_id");
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.view_profile_activity_toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            toolbar.setTitle("Profile");
        }*/
       // this.viewModel.getUserProfile(author_id);

        profile_pic = findViewById(R.id.profile_picture);
        profile_pic.setOnClickListener(v -> {
            if(viewProfilePictureFragment == null)
                viewProfilePictureFragment = new View_Profile_Picture_Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", viewModel.getProfile().getValue().getUserName());
            String status = "";
            String url = viewModel.getProfile().getValue().getProfilePic().url;
            for(Post p: viewModel.getProfile().getValue().getMyPosts().getValue()){
                if(p.getImage().url.equals(url))
                    status = p.getStatus();
            }
            bundle.putString("status", status);
            bundle.putString("url", url);
            viewProfilePictureFragment.setArguments(bundle);
            viewProfilePictureFragment.show(getSupportFragmentManager(), "VIEW_PROFILE_PICTURE");
        });

        viewModel.getProfile().observe(this, userProfile -> {
            if (userProfile == null)
                return;
            Log.e("ActivityVewProfile","at "+userProfile.getProfilePic().url);
            username.setText(userProfile.getUserName());
            Glide.with(this).load(userProfile.getProfilePic().url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profile_pic);
            imageFragment = ViewProfile_UserImages_Fragment.getInstance();//viewModel.getProfile().getValue().myPosts.getValue());
            initPosts();

            followingNumber.setText(String.valueOf(userProfile.getFollowing().getValue().size()));
            followerNumber.setText(String.valueOf(userProfile.getFollowers().getValue().size()));
            UpdateFollowBtn();

        });

        followBtn.setOnClickListener(v -> {
            viewModel.UserPressFollow();
            UpdateFollowBtn();
        });

        messageBtn.setOnClickListener(v -> {
           App.startActivityChatroom(Activity_ViewProfile.this, viewModel.openChatRoomWithUser().getChatRoomID());
        });
    }

    private void UpdateFollowBtn(){
        if(viewModel.isFollowing()){
            followBtn.setTextColor(Color.parseColor("#03b5fc"));
        }

        else{
            followBtn.setTextColor(Color.BLACK);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void initPosts(){
        postFragment = new MainPages_Posts_Fragment(true, true);
        ((MainPages_Posts_Fragment)postFragment).setViewModel(this.viewModel);
        getSupportFragmentManager().beginTransaction().replace(R.id.my_posts, postFragment).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(infoFragment == null || imageFragment == null)
            return;
        if (profile_navigation_bar.getSelectedItemId() == R.id.profile_info_item)
            getSupportFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, infoFragment).commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("INFO", "Destroying view of profile fragment ...");
    }

}