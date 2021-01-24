package com.example.tintok;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_ViewProfile_ViewModel extends MainPages_Posts_ViewModel {

    private RestAPI api;

    public Activity_ViewProfile_ViewModel(@NonNull Application application) {
        super(application);
        this.profile = new MutableLiveData<>(null);
        this.api = Communication.getInstance().getApi();
    }

    private MutableLiveData<UserProfile> profile;

    public MutableLiveData<UserProfile> getProfile() {
        return profile;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getUserProfile(String id){
        this.api.getUserProfile(id).enqueue(new Callback<UserForm>() {
            @Override
            public void onResponse(Call<UserForm> call, Response<UserForm> response) {
                if(response.isSuccessful()){
                    UserForm userForm = response.body();
                    List<PostForm> posts = userForm.getPosts();
                    String name = userForm.getUsername();
                    ArrayList<Post> new_posts = new ArrayList<>();
                    for(PostForm post : posts){
                        MediaEntity media = new MediaEntity(post.getImageUrl());
                        new_posts.add(new Post(post.getId(), post.getStatus(), post.getAuthor_id(), media));
                    }
                    UserProfile result = new UserProfile();
                    result.setUserName(name);
                    result.setProfilePic(new MediaEntity(userForm.getImageUrl()));
                    Log.e("Activity_VIewProfile_ViewModel", "img: "+userForm.getImageUrl());
                    result.myPosts.postValue(new_posts);
                    profile.postValue(result);
                }
                else{
                    Toast.makeText(Activity_ViewProfile_ViewModel.this.getApplication(), "Cannot get the user", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserForm> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    @Override
    public MutableLiveData<ArrayList<Post>> getPosts() {
        if(this.profile == null)
            return null;
        return this.profile.getValue().getMyPosts();
    }
}
