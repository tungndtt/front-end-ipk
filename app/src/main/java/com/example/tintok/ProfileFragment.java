package com.android.testsocketclientio;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileFragment extends Fragment {

    private Fragment infoFragment, imageFragment;
    private int selected;

    public ProfileFragment() {
        // Required empty public constructor
        Log.i("Init", "Initialize profile fragment...");
        this.infoFragment = ProfileInfoFragment.newInstance();
        this.imageFragment = ProfileImageFragment.newInstance();
        this.selected = R.id.profile_info_item;
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "Creating new fragment for profile...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("INFO", "Creating view for profile fragment ...");
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        BottomNavigationView profile_navigation_bar = view.findViewById(R.id.profile_navigation_bar);
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
        if(profile_navigation_bar.getSelectedItemId() == R.id.profile_info_item)
            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, infoFragment).commit();
        else
            getChildFragmentManager().beginTransaction().replace(R.id.profile_sub_fragment, imageFragment).commit();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("INFO", "Destroying view of profile fragment ...");
    }
}