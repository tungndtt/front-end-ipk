package com.example.tintok;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tintok.Adapters_ViewHolder.PostAdapter;
import com.example.tintok.CustomView.PostUploadFragment;
import com.example.tintok.Model.Post;


public class MainPages_Posts_Fragment extends Fragment implements PostAdapter.onPostListener   {

    private MainPages_Posts_ViewModel mViewModel;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_pages__posts__fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainPages_Posts_ViewModel.class);
        // TODO: Use the ViewMode
        this.postAdapter = new PostAdapter(getContext(), mViewModel.getPosts().getValue());
        this.postAdapter.setListener(this);
        this.recyclerView = getView().findViewById(R.id.post_list);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

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

    @Override
    public void onClickAvatar(View v, int position) {
        Intent intent = new Intent(getContext(), Activity_ViewProfile.class);
        intent.putExtra("author_id", postAdapter.getItems().get(position).getAuthor_id());
        v.getContext().startActivity(intent);
    }

    @Override
    public void onClickComment(View v, int position) {
        Intent intent = new Intent(getContext(), Activity_Comment.class);
        intent.putExtra("post_id", postAdapter.getItems().get(position).getId());
        v.getContext().startActivity(intent);
    }
}