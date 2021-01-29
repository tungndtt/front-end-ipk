package com.example.tintok;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tintok.Adapters_ViewHolder.PostAdapter;
import com.example.tintok.CustomView.AfterRefreshCallBack;
import com.example.tintok.CustomView.PostUploadFragment;
import com.example.tintok.CustomView.Refreshable;
import com.example.tintok.Model.Post;

import java.util.ArrayList;


public class MainPages_Posts_Fragment extends Fragment implements PostAdapter.onPostListener, Refreshable {

    private MainPages_Posts_ViewModel mViewModel = null;

    public void setViewModel(MainPages_Posts_ViewModel mViewModel){
        this.mViewModel = mViewModel;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_pages__posts__fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mViewModel == null)
            mViewModel = new ViewModelProvider(this.getActivity()).get(MainPages_Posts_ViewModel.class);
        // TODO: Use the ViewMode
        this.postAdapter = new PostAdapter(getContext(), new ArrayList<>());
        this.postAdapter.setListener(this);
        this.recyclerView = getView().findViewById(R.id.post_list);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mViewModel.getPosts().observe(this.getViewLifecycleOwner(), posts -> {
            postAdapter.setItems(posts);
        });

        Log.i("INFO", "OnActivityCreated fragment for post fragment ...");
    }

    private RecyclerView recyclerView;

    private PostAdapter postAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("INFO", "Destroy fragment for post fragment ...");
    }

    //private ViewModel viewModel;

    public MainPages_Posts_Fragment() {
        Log.i("Init", "Initialize post fragment...");
        //this.viewModel = ViewModel.getInstance();
    }

    public static MainPages_Posts_Fragment getInstance() {
        MainPages_Posts_Fragment fragment = new MainPages_Posts_Fragment();
        return fragment;
    }

    public void onClickAvatar(View v, int position) {
        String userID = postAdapter.getItems().get(position).getAuthor_id();
        if(userID.compareTo( mViewModel.getCurrentUserID()) == 0)
            return;
        Intent intent = new Intent(this.getContext(), Activity_ViewProfile.class);
        intent.putExtra("author_id", userID );

        v.getContext().startActivity(intent);
    }

    @Override
    public void onClickComment(View v, int position) {
        Intent intent = new Intent(this.getContext(), Activity_Comment.class);
        Post post =  postAdapter.getItems().get(position);
        intent.putExtra("post_id", post.getId());
        v.getContext().startActivity(intent);
    }

    @Override
    public void onClickLike(View v, int position) {
        Post post =  postAdapter.getItems().get(position);
        mViewModel.UserPressLike(post);
    }

    @Override
    public void onNotificationChange(int position) {
        Post post =  postAdapter.getItems().get(position);
        mViewModel.UserPressSubscribe(post);
    }

    @Override
    public void onRefresh(AfterRefreshCallBack e) {
        mViewModel.refreshData(e);

    }
}