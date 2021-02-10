package com.example.tintok;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tintok.Model.UserProfile;

import java.time.format.DateTimeFormatter;

public class ViewProfile_UserInfo_Fragment extends Fragment {

    private TextView mAgeTextView, mBirthdayTextView, mInterestsTextView, mDescriptionTV, mGenderTV;
    private EditText mDescriptionEditText;
    private DateTimeFormatter formatter;
    private int day, year, month;
    private String interests;
    View view;
    private UserProfile user;

    public ViewProfile_UserInfo_Fragment() {
        // Required empty public constructor
    }

    public static ViewProfile_UserInfo_Fragment getInstance() {
        ViewProfile_UserInfo_Fragment fragment = new ViewProfile_UserInfo_Fragment();
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
        Log.i("INFO", "Creating view for info profile ...");
        view = inflater.inflate(R.layout.profile_info_fragment, container, false);
        initViews();
        return view;}

    private void initViews() {
        mAgeTextView = view.findViewById(R.id.profile_age);
        mDescriptionEditText = view.findViewById(R.id.profile_description);
        mBirthdayTextView = view.findViewById(R.id.profile_birthday);
        mInterestsTextView = view.findViewById(R.id.profile_interest);
        mDescriptionEditText.setFocusable(false);
    }


}
