package com.example.tintok.Communication.RestAPI_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserForm extends UnknownUserForm{
    @SerializedName("id")
    private String id;
    @SerializedName("profile_pic")
    private String url;
    @SerializedName("posts")
    private List<PostForm> posts;

    public UserForm(String username, String email, String password) {
        super(username, email, password);
    }
    public List<PostForm> getPosts() {
        return posts;
    }
    public String getId(){
        return this.id;
    }
    public String getImageUrl(){ return url; }
}
