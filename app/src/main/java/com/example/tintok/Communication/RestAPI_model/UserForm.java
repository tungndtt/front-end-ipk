package com.example.tintok.Communication.RestAPI_model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserForm extends UnknownUserForm{
    @SerializedName("id")
    private String id;
    @SerializedName("profile_pic")
    private String url;
    @SerializedName("posts")
    private ArrayList<PostForm> posts;
    @SerializedName("following_posts")
    private ArrayList<String> following_posts;
    @SerializedName("liked_posts")
    private ArrayList<String> liked_posts;

    @SerializedName("followers")
    private ArrayList<String> followers;
    @SerializedName("followees")
    private ArrayList<String> following;

    @SerializedName("lastLogout")
    private long time;

    public UserForm(String username, String email, String password) {
        super(username, email, password);
    }
    public ArrayList<PostForm> getPosts() {
        return posts;
    }
    public String getId(){
        return this.id;
    }
    public String getImageUrl(){ return url; }
    public ArrayList<String> getFollowing_posts() {
        return following_posts;
    }

    public ArrayList<String> getLiked_posts() {
        return liked_posts;
    }
    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }
    public long getTime() {
        return time;
    }

}
