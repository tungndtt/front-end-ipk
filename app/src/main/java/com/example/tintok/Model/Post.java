package com.example.tintok.Model;

import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

public class Post {
    private String id;
    private String status;
    private String author_id;
    private MediaEntity image;
    public ArrayList<String> likers;
    public MutableLiveData<ArrayList<Comment>> comments;

    public Post(String id, String status, String author_id, MediaEntity image) {
        this.id = id;
        this.status = status;
        this.author_id = author_id;
        this.image = image;
        comments = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }


    public MediaEntity getImage() {
        return image;
    }

    public void setImage(MediaEntity image) {
        this.image = image;
    }

}
