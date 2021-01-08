package com.example.tintok;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.Post;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainPages_Posts_ViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public MutableLiveData<ArrayList<Post>> getPosts(){
        return DataRepositoryController.getInstance().getNewfeedPosts();
    }
}