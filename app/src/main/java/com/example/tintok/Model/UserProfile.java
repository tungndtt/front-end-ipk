package com.example.tintok.Model;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Queue;

public class UserProfile extends UserSimple {
    public MutableLiveData<ArrayList<String>> followers;
    public MutableLiveData<ArrayList<String>> following;

    public MutableLiveData<ArrayList<Post>> myPosts;

    public MutableLiveData<ArrayList<MediaEntity>> mPhotos;

    public MutableLiveData<ArrayList<MediaEntity>> getPhotos() {
        return mPhotos;
    }

    public void postPhotos(ArrayList<MediaEntity> photos) {
        this.mPhotos.postValue(photos);
    }


    public UserProfile(){
        this.myPosts = new MutableLiveData<>(new ArrayList<>());
    }


    public MutableLiveData<ArrayList<String>> getFollowers() {
        return followers;
    }

    public void postFollowers(ArrayList<String> followers) {
        this.followers.postValue(followers);
    }


    public MutableLiveData<ArrayList<String>> getFollowing() {
        return following;
    }

    public void postFollowering(ArrayList<String> followering) {
        this.following.postValue(followering);
    }


    public MutableLiveData<ArrayList<Post>> getMyPosts() {
        return myPosts;
    }

    public void postMyPosts(ArrayList<Post> myPosts) {
        this.myPosts.postValue(myPosts);
    }
}
