package com.example.tintok.Model;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Notification {
    public enum NotificationType{
        NEW_FOLLOWER, NEW_POSTS_FROM_FOLLOWINGS, LIKE_POST, COMMENT_POST
    }
    NotificationType type;


    Date date;

    public Notification(NotificationType type, Date date, String author_username, String author_profile_pic, String url, String postID,  String post_author_id, String post_status) {
        this.type = type;
        this.date = date;
        this.author_username = author_username;
        this.author_profile_pic = author_profile_pic;
        this.url = url;
        this.postID = postID;
        this.post_author_id = post_author_id;
        this.post_status = post_status;
    }

    String author_username;
    String author_profile_pic;
    String url;

    public Date getDate() {
        return date;
    }

    public String getAuthor_username() {
        return author_username;
    }

    public String getAuthor_profile_pic() {
        return author_profile_pic;
    }

    public String getUrl() {
        return url;
    }

    public String getPostID() {
        return postID;
    }


    public String getPost_author_id() {
        return post_author_id;
    }

    public String getPost_status() {
        return post_status;
    }

    String postID;
    String post_author_id;
    String post_status;





    public SpannableStringBuilder toTextViewString() {
        SpannableStringBuilder str = new SpannableStringBuilder(author_username);
        StyleSpan bold_italicText = new StyleSpan(Typeface.BOLD_ITALIC);
        StyleSpan italicText = new StyleSpan(Typeface.ITALIC);
        str.setSpan(bold_italicText, 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        if(type == NotificationType.NEW_FOLLOWER){
            str.append(" has just followed you");
        }
        else if(type == NotificationType.NEW_POSTS_FROM_FOLLOWINGS){
            str.append(" has just added a new post");
        }
        else if(type == NotificationType.LIKE_POST){
            str.append(" has just put a like on your post");
        }
        else if(type == NotificationType.COMMENT_POST){
            str.append(" has just put a new comment on your post");
        }
        str.setSpan(bold_italicText, author_username.length(), str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return str;
    }



}
