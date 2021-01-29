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
import com.example.tintok.Communication.CommunicationEvent;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.ChatRoom;
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
                        Post p = new Post(post.getId(), post.getStatus(), post.getAuthor_id(), media);
                        p.likers = post.getLikes() == null ? new ArrayList<>():post.getLikes();
                        new_posts.add(p);
                    }
                    UserProfile result = new UserProfile();
                    result.setUserID(id);
                    result.setUserName(name);
                    result.setProfilePic(new MediaEntity(userForm.getImageUrl()));
                    Log.e("Activity_VIewProfile_ViewModel", "img: "+userForm.getImageUrl());
                    ArrayList<String> dummy = result.getFollowers().getValue();
                    dummy.addAll(userForm.getFollowers());
                    result.postFollowers(dummy);
                    dummy = result.getFollowing().getValue();
                    dummy.addAll(userForm.getFollowing());
                    result.postFollowering(dummy);
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

    public ChatRoom openChatRoomWithUser() {
        ArrayList<String> userIDs = new ArrayList<>(2);
        userIDs.add(this.profile.getValue().getUserID());
        userIDs.add(DataRepositoryController.getInstance().getUser().getValue().getUserID());
        Log.e("Activity_ViewProf_Model", "at "+ this.profile.getValue().getUserID());
        return DataRepositoryController.getInstance().getChatRoomByUser(userIDs);
    }

    public void UserPressFollow() {
        String currentUser = DataRepositoryController.getInstance().getUser().getValue().getUserID();
        ArrayList<String> currentFollower = this.profile.getValue().getFollowers().getValue();
        if(!currentFollower.contains(currentUser)){
            Communication.getInstance().get_socket().emit(CommunicationEvent.FOLLOW_USER, this.profile.getValue().getUserID(),  currentUser);
            currentFollower.add(currentUser);
            this.profile.getValue().getFollowers().postValue(currentFollower);
        }
        else{
            Communication.getInstance().get_socket().emit(CommunicationEvent.UNFOLLOW_USER, this.profile.getValue().getUserID(),  currentUser);
            currentFollower.remove(currentUser);
            this.profile.getValue().getFollowers().postValue(currentFollower);
        }
        this.profile.postValue(this.profile.getValue());
    }
    public String GetCurrentUserID(){
       return DataRepositoryController.getInstance().getUser().getValue().getUserID();
    }
}
