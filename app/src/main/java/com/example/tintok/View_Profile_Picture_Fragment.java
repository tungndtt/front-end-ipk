package com.example.tintok;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class View_Profile_Picture_Fragment extends Fragment {


    public View_Profile_Picture_Fragment() {
        // Required empty public constructor
    }


    public static View_Profile_Picture_Fragment newInstance(String param1, String param2) {
        View_Profile_Picture_Fragment fragment = new View_Profile_Picture_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.profile_picture_view_fragment, container, false);
    }
}