package com.example.tintok.Model;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;

import java.util.Date;

public class Notification {
    public enum NotificationType{
        NEW_FOLLOWER, NEW_POSTS_FROM_FOLLOWINGS, LIKE_POST, COMMENT_POST
    }
    NotificationType type;
    UserSimple author;
    String postTriggeredID;
    MediaEntity postPic;
    Date date;

    public Notification(NotificationType type, UserSimple author,
            String postTriggeredID, Date date, MediaEntity postPic){
        this.type = type;
        this.author = author;
        this.postTriggeredID = postTriggeredID;
        this.date = date;
        this.postPic = postPic;
    }

    public NotificationType getType(){
        return type;
    }

    public SpannableStringBuilder toTextViewString() {
        SpannableStringBuilder str = new SpannableStringBuilder(author.getUserName());
        StyleSpan bold_italicText = new StyleSpan(Typeface.BOLD_ITALIC);
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
        return str;
    }
    public String getDate(){
        return date.toString();
    }

    public String getPostTriggered(){
        return postTriggeredID;
    }

    public MediaEntity getAuthorPic(){
        return author.getProfilePic();
    }
}
