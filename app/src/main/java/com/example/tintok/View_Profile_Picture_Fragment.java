package com.example.tintok;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.CustomView.MyDialogFragment;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;


public class View_Profile_Picture_Fragment extends MyDialogFragment {


    MainPages_MyProfile_ViewModel mViewModel;
    private TextView mName, mStatus;
    private ImageView mImage;
    private View view;

    public View_Profile_Picture_Fragment() {
        // Required empty public constructor
    }


    public static View_Profile_Picture_Fragment newInstance(String param1, String param2) {
        View_Profile_Picture_Fragment fragment = new View_Profile_Picture_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_picture_view_fragment, container, false);
        mName = view.findViewById(R.id.picture_view_nameTV);
        mStatus = view.findViewById(R.id.picture_view_status);
        mImage = view.findViewById(R.id.picture_view_image);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        if(mViewModel == null)
            mViewModel =  new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);

         */
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(this.getArguments() != null){
            Bundle bundle = this.getArguments();
            mName.setText(bundle.getString("name"));
            mStatus.setText(bundle.getString("status"));
            Glide.with(this.getContext()).load(bundle.getString("url"))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(mImage);
        }



        /*
        mViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> {
            mName.setText(userProfile.getUserName());
            String url = userProfile.getProfilePic().url;
            String status = "";
            for(Post p: userProfile.getMyPosts().getValue()){
                if(p.getImage().url.equals(url))
                    status = p.getStatus();
            }
            mStatus.setText(status);
            Glide.with(this.getContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(mImage);

        });

         */



    }
}