package com.example.tintok;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainPages__PeopleBrowsing__Fragment extends Fragment {

    private MainpagesPeoplebrowsingFragmentViewModel mViewModel;

    public static MainPages__PeopleBrowsing__Fragment newInstance() {
        return new MainPages__PeopleBrowsing__Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mainpages__peoplebrowsing__fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainpagesPeoplebrowsingFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}