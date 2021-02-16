package com.example.tintok;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.Adapters_ViewHolder.PostAdapter;
import com.example.tintok.CustomView.MyDialogFragment;
import com.example.tintok.CustomView.PostUploadFragment;
import com.example.tintok.CustomView.Profile_Picture_BottomSheet;
import com.example.tintok.CustomView.Profile_Picture_UploadFragment;
import com.example.tintok.DataLayer.ResponseEvent;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainPages_MyProfile_Fragment extends MyDialogFragment implements PostUploadFragment.onNewPostListener {

    private Fragment infoFragment, imageFragment, postFragment;
    private int selected;

    private UserProfile user;
    private ImageView profilePic;
    private View newPostBtn;
    private TextView followingNumber, followerNumber;
    private EditText username, location;
    private View view;
    ShapeableImageView backBtn;
    BottomNavigationView profile_navigation_bar;
    Profile_Picture_UploadFragment profilePictureUploadFragment;

    MainPages_MyProfile_ViewModel mViewModel;

    private final static String NEW_POST = "New Post";
    private final static String NEW_PROFILE_PICTURE = "New Profile Picture";
    private final static String BOTTOM_SHEET = "profile picture bottom sheet";

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
        view = inflater.inflate(R.layout.mainpages_myprofile_fragment, container, false);
        //this.viewModel.setFragment(this);

        username = view.findViewById(R.id.profile_name);
        newPostBtn = view.findViewById(R.id.newPostBtn);
        profilePic = view.findViewById(R.id.post_profile);
        followingNumber = view.findViewById(R.id.followingsNumber);
        followerNumber = view.findViewById(R.id.follwersNumber);
        backBtn = view.findViewById(R.id.backBtn);
        location = view.findViewById(R.id.profile_location);

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

        backBtn.setOnClickListener(v -> {
            getDialog().dismiss();
        });
        setupFullscreen();
        return view;
    }

    PostUploadFragment post = null;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);
        initPosts();
        mViewModel.setUsername(mViewModel.getUserProfile().getValue().getUserName());
        mViewModel.setLocation(mViewModel.getUserProfile().getValue().getLocation());
        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(post == null){
                    post = new PostUploadFragment(MainPages_MyProfile_Fragment.this::onNewPost);
                    post.show(getChildFragmentManager(), NEW_POST);
                }

            }
        });
        location.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                mViewModel.setLocation(input);
                mViewModel.setInfoIsEdited(true);
                //   Log.e("afterTC", s.toString());
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setUsername(s.toString());
                mViewModel.setInfoIsEdited(true);
            }
        });
        /*
        username.setText(user.getUserName());
        Glide.with(this.getContext()).load(user.getProfilePic().url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profilePic);

        */
        profilePic.setOnClickListener(v -> {
            Profile_Picture_BottomSheet profilePictureBottomSheet = new Profile_Picture_BottomSheet();
            profilePictureBottomSheet.show(getActivity().getSupportFragmentManager(), BOTTOM_SHEET);
            profilePictureBottomSheet.setOnTextViewClickListener(position -> {
                switch (position){
                    case 0: //TODO: show post
                            break;// viewPhoto
                    case 1: Log.e("item", String.valueOf(profile_navigation_bar.getSelectedItemId()));
                        if(profile_navigation_bar.getSelectedItemId() == R.id.profile_info_item){//selectPhoto
                           // profilePictureBottomSheet.dismiss();
                            selected = R.id.profile_photo_item;
                            profilePictureBottomSheet.dismiss();
                            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
                        }else{
                            profilePictureBottomSheet.dismiss();
                            Snackbar.make(getView(), "Click on your picture", Snackbar.LENGTH_SHORT).show();
                        }
                        break;
                    case 2: profilePictureBottomSheet.dismiss();
                            if(profilePictureUploadFragment == null){
                                profilePictureUploadFragment = new Profile_Picture_UploadFragment();
                                profilePictureUploadFragment.show(getChildFragmentManager(), NEW_PROFILE_PICTURE);
                                profilePictureUploadFragment.setOnNewProfilePictureListener(newPost -> {
                                    mViewModel.submitNewProfilePicture(newPost);
                                });
                            }
                            break;
                }
            });
        });


    }

    void initPosts(){
        postFragment = new MainPages_Posts_Fragment(true,false);
        ((MainPages_Posts_Fragment)postFragment).setViewModel(this.mViewModel);
        getChildFragmentManager().beginTransaction().replace(R.id.my_posts, postFragment).commit();

    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile == null)
                return;
            username.setText(mViewModel.getUserProfile().getValue().getUserName().toUpperCase());
            location.setText(mViewModel.getUserProfile().getValue().getLocation().toUpperCase());
            Glide.with(this.getContext()).load(mViewModel.getUserProfile().getValue().getProfilePic().url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profilePic);
            followerNumber.setText(String.valueOf(userProfile.getFollowers().getValue().size()));
            followingNumber.setText(String.valueOf(userProfile.getFollowing().getValue().size()));
        });
        mViewModel.getNetworkResponse().observe(getViewLifecycleOwner(), responseEvent -> {
            if(responseEvent.getType() == ResponseEvent.Type.PROFILE_PICTURE_UPDATE || responseEvent.getType() == ResponseEvent.Type.PROFILE_PICTURE_UPLOAD){
                String response = responseEvent.getContentIfNotHandled();
                if(response != null && response.equals("Created"))
                    Snackbar.make(getView(), "Profile Picture Updated", Snackbar.LENGTH_LONG).show();
                if(response != null && response.equals("Ok"))
                    Snackbar.make(getView(), "Profile Picture Saved", Snackbar.LENGTH_LONG).show();
            }
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