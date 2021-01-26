package com.example.tintok;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.Adapters_ViewHolder.PostAdapter;
import com.example.tintok.CustomView.PostUploadFragment;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MainPages_MyProfile_Fragment extends Fragment implements PostUploadFragment.onNewPostListener {

    private Fragment infoFragment, imageFragment, postFragment;
    private int selected;

    private UserProfile user;
    private ImageView profilePic;
    private View newPostBtn;
    private TextView follwingNumber, followerNumber, username;
    ShapeableImageView menuBtn;
    BottomNavigationView profile_navigation_bar;

    MainPages_MyProfile_ViewModel mViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MainPages_MyProfile_Fragment(UserProfile user) { // and other data of user to display (location, gender, images, ...)
        // Required empty public constructor
        Log.i("Init", "Initialize profile fragment...");
        this.user = user;

        this.selected = R.id.profile_info_item;
        this.infoFragment = Info_Profile_Fragment.getInstance();
        this.imageFragment = Image_Profile_Fragment.getInstance(this.user.getMyPosts().getValue());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static MainPages_MyProfile_Fragment getInstance(UserProfile user) {
        MainPages_MyProfile_Fragment fragment = new MainPages_MyProfile_Fragment(user);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "Creating new fragment for profile...");
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("INFO", "Creating view for profile fragment ...");
        View view = inflater.inflate(R.layout.mainpages_myprofile_fragment, container, false);
        //this.viewModel.setFragment(this);
        // info of displayed user. Currently just user name for testing
        username = view.findViewById(R.id.profile_name);
        newPostBtn = view.findViewById(R.id.newPostBtn);
        profilePic = view.findViewById(R.id.post_profile);
        follwingNumber = view.findViewById(R.id.followingsNumber);
        followerNumber = view.findViewById(R.id.follwersNumber);
        menuBtn = view.findViewById(R.id.openMenuBtn);

        profile_navigation_bar = view.findViewById(R.id.profile_navigation_bar);
        profile_navigation_bar.setSelectedItemId(this.selected);
        profile_navigation_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selected = item.getItemId();
                if (item.getItemId() == R.id.profile_info_item)
                    getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, infoFragment).commit();
                else
                    getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
                return true;
            }
        });

        menuBtn.setOnClickListener(v -> {
            ((Activity_AppMainPages)this.getActivity()).showNavView();
        });
        return view;
    }

    PostUploadFragment post = null;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);
        initPosts();
        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(post == null){
                    post = new PostUploadFragment(MainPages_MyProfile_Fragment.this::onNewPost);
                    post.show(getChildFragmentManager(), "New Post");
                }

            }
        });

        username.setText(user.getUserName());
        Glide.with(this.getContext()).load(user.getProfilePic().url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profilePic);
    }

    void initPosts(){
        postFragment = new MainPages_Posts_Fragment();
        ((MainPages_Posts_Fragment)postFragment).setViewModel(this.mViewModel);
        getChildFragmentManager().beginTransaction().replace(R.id.my_posts, postFragment).commit();

    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile == null)
                return;
            username.setText(mViewModel.getUserProfile().getValue().getUserName());
            Glide.with(this.getContext()).load(mViewModel.getUserProfile().getValue().getProfilePic().url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profilePic);
        });
        if (profile_navigation_bar.getSelectedItemId() == R.id.profile_info_item)
            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, infoFragment).commit();
        else
            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("INFO", "Destroying view of profile fragment ...");
    }

    @Override
    public void onNewPost(Post newPost) {
        post = null;
        if(newPost != null)
            mViewModel.submitNewPost(newPost);
    }


}