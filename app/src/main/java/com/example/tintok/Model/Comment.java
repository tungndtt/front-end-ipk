package com.example.tintok.Model;

import android.text.SpannableStringBuilder;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Comment extends MessageEntity {

    private String id;


    public String getId() {
        return id;
    }


    public Comment(String id, String authorID, @Nullable  SpannableStringBuilder builder, @Nullable MediaEntity media, Date date) {
        super(authorID, builder, date);
        this.id = id;
        this.media = media;
    }
}
