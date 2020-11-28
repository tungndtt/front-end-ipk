package com.android.testsocketclientio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.testsocketclientio.entity.ProfileImage;

import java.util.ArrayList;

public class ProfileImageFragment extends Fragment {
    private ImagePageAdapter adapter;
    public ProfileImageFragment() {
        ArrayList<ProfileImage> list = new ArrayList<>();
        list.add(new ProfileImage(R.drawable.pepecry));
        list.add(new ProfileImage(R.drawable.pig));
        list.add(new ProfileImage(R.drawable.pepecry));
        this.adapter = new ImagePageAdapter(list);
    }

    public static ProfileImageFragment newInstance() {
        ProfileImageFragment fragment = new ProfileImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "Creating new profile image...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("INFO", "Creating view for displaying photo profile ...");
        View view = inflater.inflate(R.layout.profile_image_fragment, container, false);
        ViewPager2 vp2 = view.findViewById(R.id.profile_image_list_page);
        vp2.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("INFO", "Destroying view of displaying photo profile ...");
    }
}