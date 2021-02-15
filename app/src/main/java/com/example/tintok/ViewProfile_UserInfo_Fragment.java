package com.example.tintok;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tintok.DataLayer.DataRepository_Interest;
import com.example.tintok.Model.UserProfile;

import java.time.format.DateTimeFormatter;

public class ViewProfile_UserInfo_Fragment extends Fragment {


    private TextView  mAgeTV, mBirthdayTV, mGenderTV, mInterestsTV, mDescriptionTV;
    private DateTimeFormatter formatter;
    View view;
    private UserProfile user;
    private String interests;
    Activity_ViewProfile_ViewModel mViewModel;

    public ViewProfile_UserInfo_Fragment(Activity_ViewProfile_ViewModel mViewModel) {
            this.mViewModel = mViewModel;
    }

    public static ViewProfile_UserInfo_Fragment getInstance(Activity_ViewProfile_ViewModel mViewModel) {
        ViewProfile_UserInfo_Fragment fragment = new ViewProfile_UserInfo_Fragment(mViewModel);
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
        view = inflater.inflate(R.layout.view_profile_info_fragment, container, false);
        mAgeTV = view.findViewById(R.id.view_profile_age);
        mBirthdayTV = view.findViewById(R.id.view_profile_birthday);
        mGenderTV = view.findViewById(R.id.view_profile_gender);
        mDescriptionTV = view.findViewById(R.id.view_profile_description);
        mInterestsTV = view.findViewById(R.id.view_profile_interest);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mViewModel == null)
            mViewModel = new ViewModelProvider(getActivity()).get(Activity_ViewProfile_ViewModel.class);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        mViewModel.getProfile().observe(getViewLifecycleOwner(), userProfile -> {
            if(userProfile == null)
                return;
            Log.e("UserInfoFrag", "at "+userProfile.getBirthday());
            mAgeTV.setText(String.valueOf(userProfile.getAge()));
            mBirthdayTV.setText(formatter.format(userProfile.getBirthday()));
            mGenderTV.setText(userProfile.getGender().toString().toLowerCase());
            if(userProfile.getDescription() == null)
                mDescriptionTV.setVisibility(View.GONE);
            else mDescriptionTV.setText(userProfile.getDescription());
            interests = "";
            for(int i = 0; i < userProfile.getUserInterests().getValue().size(); i++){
                interests += DataRepository_Interest.interests[userProfile.getUserInterests().getValue().get(i)] + " ";
            }
            mInterestsTV.setText(interests);
        });


    }
}
