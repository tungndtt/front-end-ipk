package com.example.tintok.DataLayer;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository_Posts {
    MutableLiveData<ArrayList<Post>> newfeedPosts;
    DataRepositoryController controller;
    RestAPI api;

    public DataRepository_Posts(DataRepositoryController controller){
        newfeedPosts = new MutableLiveData<>(new ArrayList<>());
        this.controller = controller;
        this.api = Communication.getInstance().getApi();
    }

    public MutableLiveData<ArrayList<Post>> getNewfeedPosts(){
        return newfeedPosts;
    }

    public void setNewfeedPosts(Post... newPosts){
        ArrayList<Post> mPosts = newfeedPosts.getValue();
        for(Post p : newPosts){
            mPosts.add(p);
        }
        newfeedPosts.setValue(mPosts);

    }
    public void postNewfeedPosts(Post... newPosts){
        ArrayList<Post> mPosts = newfeedPosts.getValue();
        for(Post p : newPosts){
            mPosts.add(p);
        }
        newfeedPosts.postValue(mPosts);

    }

    public void initData(){

        this.api.getPosts().enqueue(new Callback<ArrayList<PostForm>>() {
            @Override
            public void onResponse(Call<ArrayList<PostForm>> call, Response<ArrayList<PostForm>> response) {
                if(response.isSuccessful()){
                    ArrayList<PostForm> body = response.body();
                    ArrayList<Post> posts = newfeedPosts.getValue();
                    for(PostForm postForm: body){
                        Post e = new Post(postForm.getId(), postForm.getStatus(),postForm.getAuthor_id(), postForm.getAuthor_name(), new MediaEntity(postForm.getImageUrl()));
                        posts.add(e);
                    }
                    newfeedPosts.postValue(posts);
                } else {
                    Log.d("Info", "Response fails");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PostForm>> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Log.e("Error", "Connection error");
            }
        });
    }

    //Server part

}
