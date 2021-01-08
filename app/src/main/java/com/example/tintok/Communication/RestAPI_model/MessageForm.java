package com.example.tintok.Communication.RestAPI_model;

import com.google.gson.annotations.SerializedName;

public class MessageForm {
    @SerializedName("author_id")
    private String author_id;
    @SerializedName("message")
    private String message;
    @SerializedName("imageUrl")
    private String imageUrl;

    public String getAuthor_id() {
        return author_id;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
