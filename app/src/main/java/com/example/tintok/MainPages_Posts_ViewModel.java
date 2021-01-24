package com.example.tintok;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.Post;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainPages_Posts_ViewModel extends AndroidViewModel {
    public MainPages_Posts_ViewModel(@NonNull Application application) {
        super(application);
    }
    // TODO: Implement the ViewModel

    public MutableLiveData<ArrayList<Post>> getPosts(){

        return DataRepositoryController.getInstance().getNewfeedPosts();
    }
}