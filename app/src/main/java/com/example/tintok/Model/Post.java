package com.example.tintok.Model;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class Post extends RecyclerViewModel {
    private String postID;
    private String author;
    private Date datePosted;
    private String caption;
    private MediaEntity media;
    private ArrayList<Comment> comments;
}
