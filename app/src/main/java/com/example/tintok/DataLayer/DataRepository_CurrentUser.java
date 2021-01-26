package com.example.tintok.DataLayer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.example.tintok.Utils.FileUtil;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository_CurrentUser {
    MutableLiveData<UserProfile> currentUser;
    DataRepositoryController controller;
    public DataRepository_CurrentUser(DataRepositoryController controller){
        this.controller = controller;
        currentUser = new MutableLiveData<>();
    }

    public void initData() {
        RestAPI api = Communication.getInstance().getApi();
        api.getUser().enqueue(new Callback<UserForm>() {
            @Override
            public void onResponse(Call<UserForm> call, Response<UserForm> response) {
                if(response.isSuccessful()){
                    UserForm form = response.body();
                    UserProfile currUser = new UserProfile();
                    currUser.setUserName(form.getUsername());
                    currUser.setUserID(form.getId());
                    currUser.setEmail(form.getEmail());
                    currUser.setProfilePic(new MediaEntity(form.getImageUrl()));
                    Log.e("aaaaaaaaaaaa","number = " + form.getPosts().size());
                    ArrayList<Post> photos = currUser.getMyPosts().getValue();
                    for(PostForm post : form.getPosts()){
                        Post tmp = new Post(post.getId(), post.getStatus(), post.getAuthor_id(), new MediaEntity(post.getImageUrl()));
                        tmp.likers = post.getLikes() == null?new ArrayList<>():post.getLikes();
                        photos.add(tmp);
                    }
                    currUser.myPosts.postValue(photos);
                    currentUser.postValue( currUser);
                } else {
                    Log.e("Info", "Response fails");
                }
            }

            @Override
            public void onFailure(Call<UserForm> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Log.e("Error", "Some errors occur");
            }
        });
    }

    public void submitNewPost(Context mContext, Post newPost) {
        RestAPI api = Communication.getInstance().getApi();
        if(api != null){
            MultipartBody.Part part = FileUtil.prepareImageFileBody(mContext, "upload", newPost.getImage());
            RequestBody user_id = RequestBody.create(MultipartBody.FORM, newPost.getAuthor_id());
            RequestBody status = RequestBody.create(MultipartBody.FORM, newPost.getStatus());
            String[] split = currentUser.getValue().getProfilePic().url.split("/");
            RequestBody profile_path = RequestBody.create(MultipartBody.FORM, split[split.length-1]);
            api.uploadFile(part, user_id, profile_path, status).enqueue(new Callback<PostForm>() {
                @Override
                public void onResponse(Call<PostForm> call, Response<PostForm> response) {
                    if(response.isSuccessful()){
                        // set id for the post
                        PostForm form = response.body();
                        newPost.setId(form.getId());
                        newPost.getImage().url = form.getImageUrl();
                        newPost.isSubscription = true;
                        newPost.likers = new ArrayList<>();

                        // do something with newPost ...
                        ArrayList<Post> mPosts = currentUser.getValue().myPosts.getValue();
                        mPosts.add(0,newPost);
                        currentUser.getValue().myPosts.postValue(mPosts);

                    } else {
                        // Toast.makeText(getApplication(), "Fail to get response", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PostForm> call, Throwable t) {
                    try {
                        throw t;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    //Toast.makeText(getApplication(), "Connection fails", Toast.LENGTH_LONG).show();
                }
            });
        } else {
           // Toast.makeText(this.getApplication(), "No file to upload", Toast.LENGTH_LONG).show();
        }
    }
}
