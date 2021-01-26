package com.example.tintok.Communication.RestAPI_model;

import com.example.tintok.Model.Comment;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostForm {
    // post id
    @SerializedName("id")
    private String id;

    @SerializedName("status")
    private String status;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("author_id")
    private String author_id;

    @SerializedName("comments")
    private ArrayList<CommentForm> comments;

    @SerializedName("likes")
    private ArrayList<String> likes;

    public ArrayList<CommentForm> getComments() {
        return comments;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthor_id(){ return this.author_id; }

    public ArrayList<String> getLikes() {
        return likes;
    }

}
