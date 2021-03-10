package com.example.tintok;

import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tintok.Adapters_ViewHolder.FollowersAdapter;
import com.example.tintok.CustomView.MyDialogFragment;
import com.example.tintok.CustomView.NoSpaceRecyclerViewDecoration;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.UserSimple;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

/**
 *  Class to view either user's followers or persons that the user is following.
 *  Decision based on passed arguments at creation of this fragment.
 */
public class View_Followers_Fragment extends MyDialogFragment {

    private MainPages_MyProfile_ViewModel mViewModel;
    private RecyclerView mFollowersRV;
    View view;
    private MaterialToolbar toolbar;
    private FollowersAdapter followersAdapter;

    public View_Followers_Fragment() {
        // Required empty public constructor
    }

    public static View_Followers_Fragment newInstance() {
        View_Followers_Fragment fragment = new View_Followers_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mViewModel == null)
            mViewModel = new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.view_followers_fragment, container, false);
        mFollowersRV = view.findViewById(R.id.view_followers_recyclerview);
        toolbar = view.findViewById(R.id.view_followers_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
           dismiss();
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*
            If key-value FOLLOW is 0 then an ArrayList is filled with all user's followers.
            If the key-value is 1 then the ArrayList is filled with all persons the user follows.
         */
        int follow;
        ArrayList<UserSimple> followers = new ArrayList<>();
        ArrayList<String> ids;
        if(this.getArguments() != null){
            follow = getArguments().getInt("FOLLOW");
        }else follow= -1;

        if(follow == 0){
            toolbar.setTitle("Your Followers");
            ids = mViewModel.getUserProfile().getValue().getFollowers().getValue();
        }else if(follow == 1){
            toolbar.setTitle("Following");
            ids = mViewModel.getUserProfile().getValue().getFollowing().getValue();
        }else ids = new ArrayList<>();


        followersAdapter = new FollowersAdapter(this.getContext(), ids);
        mFollowersRV.setAdapter(followersAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration= new NoSpaceRecyclerViewDecoration();
        mFollowersRV.setLayoutManager(manager);
        mFollowersRV.addItemDecoration(decoration);

    }
}