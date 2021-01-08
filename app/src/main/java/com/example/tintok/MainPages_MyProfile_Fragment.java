package com.example.tintok;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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
import android.widget.TextView;

import com.example.tintok.Adapters_ViewHolder.PostAdapter;
import com.example.tintok.CustomView.PostUploadFragment;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainPages_MyProfile_Fragment extends Fragment implements PostUploadFragment.onNewPostListener {

    private Fragment infoFragment, imageFragment;
    private int selected;

    private UserProfile user;

    private View newPostBtn;
    private RecyclerView myPosts;
    private TextView follwingNumber, followerNumber;
    BottomNavigationView profile_navigation_bar;

    PostAdapter mAdapter;
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
        TextView username = view.findViewById(R.id.profile_name);
        newPostBtn = view.findViewById(R.id.newPostBtn);
        myPosts = view.findViewById(R.id.my_posts);
        follwingNumber = view.findViewById(R.id.followingsNumber);
        followerNumber = view.findViewById(R.id.follwersNumber);

        username.setText(this.user.getUserName());
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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);
        initPosts();
        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostUploadFragment post = new PostUploadFragment(MainPages_MyProfile_Fragment.this::onNewPost);
                post.show(getChildFragmentManager(), "New Post");
            }
        });
    }

    void initPosts(){
        mAdapter = new PostAdapter(this.getContext(), mViewModel.getUserProfile().getMyPosts().getValue());
        myPosts.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        myPosts.setAdapter(mAdapter);
        myPosts.setNestedScrollingEnabled(false);
        mViewModel.getUserProfile().getMyPosts().observe(this.getViewLifecycleOwner(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                mAdapter.setItems(posts);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
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
        mViewModel.submitNewPost(newPost);
    }
}