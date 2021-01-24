package com.example.tintok.Communication.RestAPI_model;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class NotificationForm {

    public static String NEW_COMMENT = "new_comment";
    public static String NEW_REACTION = "new_reaction";


    public String getType() {
        return type;
    }

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

    public String getStatus() {
        return status;
    }

    public String getPost_author_id() {
        return post_author_id;
    }

    @SerializedName("type")
    String type;
    @SerializedName("time")
    Date date;
    @SerializedName("author_username")
    String author_username;
    @SerializedName("author_profile_pic")
    String author_profile_pic;
    @SerializedName("post_img")
    String url;
    @SerializedName("post_id")
    String postID;
    @SerializedName("post_status")
    String status;
    @SerializedName("post_author_id")
    String post_author_id;


}
