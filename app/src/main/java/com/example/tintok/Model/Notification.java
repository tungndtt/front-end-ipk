package com.example.tintok.Model;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;


import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Utils.DateTimeUtil;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Notification {
    public enum NotificationType{
        NEW_FRIEND, LIKE_POST, COMMENT_POST
    }

    public NotificationType getType() {
        return type;
    }

    NotificationType type;


    public  LocalDateTime date;

    public Notification(NotificationType type, LocalDateTime date, String author_id, String url, String postID,  String post_author_id, String post_status) {
        this.type = type;
        this.date = date;
        this.author_id = author_id;
        this.url = url;
        this.postID = postID;
        this.post_author_id = post_author_id;
        this.post_status = post_status;
    }

    public String getAuthor_id() {
        return author_id;
    }

    String author_id;
    String url;

    public String getDate() {
        return DateTimeUtil.ConvertTimeToString(this.date);
    }


    public String getUrl() {
        return url;
    }

    public String getPostID() {
        return postID;
    }




    String postID;
    String post_author_id;
    String post_status;





    public SpannableStringBuilder toTextViewString() {
        UserSimple user = (DataRepositoryController.getInstance().getUserSimpleProfile(this.author_id));
        String author_username = (user == null)?"":user.getUserName();
        SpannableStringBuilder str = new SpannableStringBuilder(author_username);
        StyleSpan bold_italicText = new StyleSpan(Typeface.BOLD_ITALIC);
        StyleSpan italicText = new StyleSpan(Typeface.ITALIC);
        str.setSpan(bold_italicText, 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        if(type == NotificationType.NEW_FRIEND){
            str.append(" and you have been following each other. You can now chat with each others");
        }

        else if(type == NotificationType.LIKE_POST){
            str.append(" has put a like on your post");
        }
        else if(type == NotificationType.COMMENT_POST){
            str.append(" has put a new comment on your post");
        }
        str.setSpan(bold_italicText, author_username.length(), str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        for (UnderlineSpan span : str.getSpans(0, str.length(), UnderlineSpan.class))
            str.removeSpan(span);
        return str;
    }



}
