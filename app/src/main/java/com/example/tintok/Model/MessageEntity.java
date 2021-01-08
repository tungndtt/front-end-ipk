package com.example.tintok.Model;

import android.text.SpannableStringBuilder;

import java.util.Date;

public class MessageEntity {
    private String authorID;
    MediaEntity media;
    private SpannableStringBuilder builder;
    private Date datePosted;

    public String getAuthorID() {
        return authorID;
    }


    public SpannableStringBuilder getBuilder() {
        return builder;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public MediaEntity getMedia() {
        return media;
    }


    public MessageEntity(String authorID, MediaEntity media, Date date){
        this.authorID = authorID;
        this.media = media;
        this.datePosted = date;
    }
    public MessageEntity(String authorID, SpannableStringBuilder builder, Date date){
        this.authorID = authorID;
        this.media = null;
        this.builder = builder;
        this.datePosted = date;
    }
}
