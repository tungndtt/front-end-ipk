package com.example.tintok.Model;

import android.text.SpannableStringBuilder;

import androidx.annotation.Nullable;

import java.util.Date;

public class Comment extends MessageEntity {
    private String id;
    private UserSimple userSimple;

    public Comment(String id ,String authorID, @Nullable MediaEntity media, Date date) {
        super(authorID, media, date);
        this.id = id;
    }

    public void setUserSimple(UserSimple userSimple){
        this.userSimple = userSimple;
    }

    public Comment(String id, String authorID, @Nullable  SpannableStringBuilder builder, Date date) {
        super(authorID, builder, date);
        this.id = id;
    }
}
