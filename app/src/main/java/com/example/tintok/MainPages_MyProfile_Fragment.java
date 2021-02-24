package com.example.tintok;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.DataLayer.ResponseEvent;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainPages_MyProfile_Fragment extends MyDialogFragment implements PostUploadFragment.onNewPostListener {

    private Fragment infoFragment, imageFragment, postFragment;
    private int selected;


    private ImageView profilePic, miniProfilePic;
    private View newPostBtn;
    private TextView followingNumber, followerNumber;
    private EditText username, location;
    private View view;
    private MaterialToolbar toolbar;
    ShapeableImageView backBtn;
    BottomNavigationView profile_navigation_bar;
    Profile_Picture_UploadFragment profilePictureUploadFragment;
    View_Profile_Picture_Fragment viewProfilePictureFragment;
    PostUploadFragment post = null;
    MainPages_MyProfile_ViewModel mViewModel;

    private final static String NEW_POST = "New Post";
    private final static String NEW_PROFILE_PICTURE = "New Profile Picture";
    private final static String BOTTOM_SHEET = "profile picture bottom sheet";
    private final static String VIEW_PROFILE_PICTURE = "view profile picture";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MainPages_MyProfile_Fragment() { // and other data of user to display (location, gender, images, ...)
        // Required empty public constructor
        Log.i("Init", "Initialize profile fragment...");
        this.selected = R.id.profile_info_item;
        this.infoFragment = Info_Profile_Fragment.getInstance();
        this.imageFragment = Image_Profile_Fragment.getInstance();//this.user.getMyPosts().getValue());
       // getChildFragmentManager().beginTransaction().add(R.id.profile_sub_fragment, infoFragment, null)

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static MainPages_MyProfile_Fragment getInstance() {
        MainPages_MyProfile_Fragment fragment = new MainPages_MyProfile_Fragment();
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
      //  backBtn = view.findViewById(R.id.backBtn);
        location = view.findViewById(R.id.profile_location);
        toolbar = view.findViewById(R.id.myProfile_toolbar);
     //   toolbar.setNavigationIcon(R.drawable.ic_backspace);
        miniProfilePic = view.findViewById(R.id.mini_post_profile_picture);
        //toolbar.setTitle("");


        profile_navigation_bar = view.findViewById(R.id.profile_navigation_bar);
        profile_navigation_bar.setSelectedItemId(this.selected);

        profile_navigation_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selected = item.getItemId();
                if (item.getItemId() == R.id.profile_info_item){
                    getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, infoFragment).commit();
                    username.setEnabled(true);
                    location.setEnabled(true);
                }
                else if(isUserEdited() && item.getItemId() == R.id.profile_photo_item)
                    getFragmentChangeAlertBuilder().show();
                else{
                    location.setEnabled(false);
                    username.setEnabled(false);
                    getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();

                }
                return true;
            }
        });
        setupFullscreen();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mViewModel == null)
            mViewModel = new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);
        Log.e("MyProfile", mViewModel.toString());
        initPosts();

        toolbar.setNavigationOnClickListener(v -> {
            if(isUserEdited()){
                getBackButtonAlertBuilder().show();
            }
            else getDialog().dismiss();
        });

        if (profile_navigation_bar.getSelectedItemId() == R.id.profile_info_item){
            location.setEnabled(true);
            username.setEnabled(true);
            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, infoFragment).commit();}
        else{
            location.setEnabled(false);
            username.setEnabled(false);
            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();}

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
            }
        });

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(post == null){
                    post = new PostUploadFragment(MainPages_MyProfile_Fragment.this::onNewPost);
                    post.show(getChildFragmentManager(), NEW_POST);
                }

            }
        });

        /*
        backBtn.setOnClickListener(v -> {
            if(isUserEdited()){
                getBackButtonAlertBuilder().show();
            }
            else getDialog().dismiss();
        });

         */
        profilePic.setOnClickListener(v -> {
            Profile_Picture_BottomSheet profilePictureBottomSheet = new Profile_Picture_BottomSheet();
            profilePictureBottomSheet.show(getActivity().getSupportFragmentManager(), BOTTOM_SHEET);
            profilePictureBottomSheet.setOnTextViewClickListener(position -> {
                switch (position){
                    case 0: // view profile picture
                        profilePictureBottomSheet.dismiss();
                        if(viewProfilePictureFragment == null)
                            viewProfilePictureFragment = new View_Profile_Picture_Fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", mViewModel.getUserProfile().getValue().getUserName());
                        String status = "";
                        String url = mViewModel.getUserProfile().getValue().getProfilePic().url;
                        for(Post p: mViewModel.getUserProfile().getValue().getMyPosts().getValue()){
                            if(p.getImage().url.equals(url))
                                status = p.getStatus();
                        }
                        bundle.putString("status", status);
                        bundle.putString("url", url);
                        viewProfilePictureFragment.setArguments(bundle);
                        viewProfilePictureFragment.show(getChildFragmentManager(), VIEW_PROFILE_PICTURE);
                            break;
                    case 1: // select profile picture
                        profilePictureBottomSheet.dismiss();
                        if(isUserEdited() && profile_navigation_bar.getSelectedItemId() == R.id.profile_info_item){
                            getFragmentChangeAlertBuilder().show();
                        }else if (profile_navigation_bar.getSelectedItemId() == R.id.profile_info_item) {
                            profile_navigation_bar.setSelectedItemId(R.id.profile_photo_item);
                            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
                        }else
                            Snackbar.make(getView(), "Click on your picture", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2: // add profile picture
                        profilePictureBottomSheet.dismiss();
                        if(isUserEdited())
                            getBackButtonAlertBuilder().show();
                        if(!isUserEdited() && profilePictureUploadFragment == null){
                            profilePictureUploadFragment = new Profile_Picture_UploadFragment();
                            profilePictureUploadFragment.show(getChildFragmentManager(), NEW_PROFILE_PICTURE);
                            profilePictureUploadFragment.setOnNewProfilePictureListener(newPost -> {
                                mViewModel.submitNewProfilePicture(newPost);
                            });
                        }profilePictureUploadFragment = null;
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
    public void onActivityCreated(Bundle savedInstanceState ) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Info", "MyProfile onActivityCreated");
        mViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile == null)
                return;
            username.setText(userProfile.getUserName().toUpperCase());
            if(userProfile.getLocation() == null || userProfile.getLocation().isEmpty())
                location.setHint(getResources().getString(R.string.location_hint).toUpperCase());
            else location.setText(userProfile.getLocation().toUpperCase());
            Glide.with(this.getContext()).load(userProfile.getProfilePic().url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profilePic);
            Glide.with(this.getContext()).load(userProfile.getProfilePic().url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(miniProfilePic);
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
    }

   /*
    @Override
    public void onStart() {
        super.onStart();

      //  if(mViewModel == null)
      //      mViewModel = new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);


        Log.e("MyProfile", mViewModel.toString());
        initPosts();

        mViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> {
            Log.e("userProfile", "observed");
            if (userProfile == null)
                return;
            username.setText(userProfile.getUserName().toUpperCase());
            if(userProfile.getLocation().isEmpty())
                location.setHint("Your location".toUpperCase());
            else location.setText(userProfile.getLocation().toUpperCase());
            Glide.with(this.getContext()).load(userProfile.getProfilePic().url)
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

       //-> comment
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
                Log.e("text", "isedited");
                mViewModel.setInfoIsEdited(true);
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


        backBtn.setOnClickListener(v -> {
            if(mViewModel.getInfoIsEdited().getValue()){
                MaterialAlertDialogBuilder dialogBuilder = getAlertBuilder();
                dialogBuilder.show();
            }
            else getDialog().dismiss();
        });

        profilePic.setOnClickListener(v -> {
            Profile_Picture_BottomSheet profilePictureBottomSheet = new Profile_Picture_BottomSheet();
            profilePictureBottomSheet.show(getActivity().getSupportFragmentManager(), BOTTOM_SHEET);
            profilePictureBottomSheet.setOnTextViewClickListener(position -> {
                switch (position){
                    case 0:
                        profilePictureBottomSheet.dismiss();
                        if(viewProfilePictureFragment == null)
                            viewProfilePictureFragment = new View_Profile_Picture_Fragment();
                        viewProfilePictureFragment.show(getChildFragmentManager(), VIEW_PROFILE_PICTURE);
                        break;
                    case 1:
                        profilePictureBottomSheet.dismiss();
                        if(profile_navigation_bar.getSelectedItemId() == R.id.profile_info_item){
                            selected = R.id.profile_photo_item;
                            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
                        }else Snackbar.make(getView(), "Click on your picture", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        profilePictureBottomSheet.dismiss();
                        if(profilePictureUploadFragment == null){
                            profilePictureUploadFragment = new Profile_Picture_UploadFragment();
                            profilePictureUploadFragment.show(getChildFragmentManager(), NEW_PROFILE_PICTURE);
                            profilePictureUploadFragment.setOnNewProfilePictureListener(newPost -> {
                                mViewModel.submitNewProfilePicture(newPost);
                            });
                        }profilePictureUploadFragment = null;
                        break;
                }
            });
        });

        Log.e("onViewCreated", "__");
        mViewModel.setInfoIsEdited(false);
    }
    */
        /*
        mViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile == null)
                return;
            username.setText(userProfile.getUserName().toUpperCase());
            if(userProfile.getLocation().isEmpty())
                location.setHint("Your location".toUpperCase());
            else location.setText(userProfile.getLocation().toUpperCase());
            Glide.with(this.getContext()).load(userProfile.getProfilePic().url)
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

      // if(mViewModel.getInfoIsEdited().getValue())
        Log.e("onStart", "???");
        mViewModel.setInfoIsEdited(false);
    }
    */

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

    private MaterialAlertDialogBuilder getBackButtonAlertBuilder(){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setCancelable(true)
                .setMessage("Your changes will be lost. Do you want to continue?")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();})
                .setPositiveButton("Continue", (dialog, which) -> {
                   mViewModel.resetLiveData();
                   getDialog().dismiss();})
                .create();
        return dialogBuilder;
    }

    private MaterialAlertDialogBuilder getFragmentChangeAlertBuilder(){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setCancelable(true)
                .setMessage("Your changes will be lost. Do you want to continue?")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    selected = R.id.profile_info_item;
                    profile_navigation_bar.setSelectedItemId(selected);
                    username.setEnabled(true);
                    location.setEnabled(true);
                    dialog.cancel();})
                .setPositiveButton("Continue", (dialog, which) -> {
                    mViewModel.resetLiveData();
                    UserProfile user = mViewModel.getUserProfile().getValue();
                    mViewModel.getUserProfile().setValue(user);
                    if(selected == R.id.profile_photo_item)
                        getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
                    username.setEnabled(false);
                    location.setEnabled(false);
                  ;})
                .create();
        return dialogBuilder;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {

                if(isUserEdited()){
                    MaterialAlertDialogBuilder dialogBuilder = getBackButtonAlertBuilder();
                    dialogBuilder.show();
                }else super.onBackPressed();
            }
        };
    }

    private boolean isUserEdited(){
        UserProfile user = mViewModel.getUserProfile().getValue();
        if(username.getText().toString().toUpperCase().equals(user.getUserName().toUpperCase()) &&
                location.getText().toString().toUpperCase().equals(user.getLocation().toUpperCase()) &&
                mViewModel.getBirthday().getValue().isEqual(user.getBirthday()) &&
                mViewModel.getGender().getValue() == user.getGender().getI() &&
                mViewModel.getDescription().getValue().equals(user.getDescription()))
            return false;
        return true;
    }
}