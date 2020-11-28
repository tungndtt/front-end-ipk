package com.android.testsocketclientio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.testsocketclientio.entity.Item;

import java.util.ArrayList;

public class PostFragment extends Fragment {
    private  PostAdapter postAdapter;
    public PostFragment() {
        Log.i("Init", "Initialize post fragment...");
        ArrayList<Item> posts = new ArrayList<>();
        posts.add(new Item(20,10, "Pepe is crying", "Pig"));
        posts.add(new Item(40,15, "Sadge", "Tung Doan"));
        posts.add(new Item(2,100, "Pepe omegalul", "Pink Pepe"));
        posts.add(new Item(34,5, "...", "Pepe"));
        posts.add(new Item(20,12, "no comment", "Ganmo"));
        this.postAdapter = new PostAdapter(posts);
    }

    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "Creating fragment for post fragment ...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.post_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.post_list);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }
}