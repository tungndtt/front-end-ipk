package com.example.tintok.Model;

import android.text.SpannableStringBuilder;

import com.example.tintok.Utils.DateTimeUtil;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
public class MessageEntity {
    private String authorID;
    MediaEntity media;
    private SpannableStringBuilder builder;
    public LocalDateTime datePosted;

    public String getAuthorID() {
        return authorID;
    }


    public SpannableStringBuilder getBuilder() {
        return builder;
    }

    public String getDatePosted() {
        return DateTimeUtil.ConvertTimeToString(this.datePosted);
    }

    public MediaEntity getMedia() {
        return media;
    }


    public MessageEntity(String authorID, MediaEntity media, LocalDateTime date){
        this.authorID = authorID;
        this.media = media;
        this.datePosted = date;
    }
    public MessageEntity(String authorID, SpannableStringBuilder builder, LocalDateTime date){
        this.authorID = authorID;
        this.media = null;
        this.builder = builder;
        this.datePosted = date;
    }
}
