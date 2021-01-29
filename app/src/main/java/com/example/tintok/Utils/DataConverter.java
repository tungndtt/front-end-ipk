package com.example.tintok.Utils;

import android.util.Log;

import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserSimple;

import java.util.ArrayList;

public class DataConverter {
    public static ArrayList<Post> ConvertFromPostForm(ArrayList<PostForm> received){
        ArrayList<Post> result = new ArrayList<>();
        if(received == null) return result;
        for(PostForm postForm: received){
            Post e = new Post(postForm.getId(), postForm.getStatus(),postForm.getAuthor_id(), new MediaEntity(postForm.getImageUrl()));
            e.likers = postForm.getLikes() == null?new ArrayList<>():postForm.getLikes();
            result.add(e);
        }
        return result;
    }

    public static ArrayList<UserSimple> ConvertFromUserFormToSimple(ArrayList<UserForm> received ){
        ArrayList<UserSimple> result = new ArrayList<>();
        if(received == null) return result;
        for(UserForm f : received){
            UserSimple user = new UserSimple();
            user.setUserID(f.getId());
            user.setUserName(f.getUsername());
            user.setDescription("");
            user.setEmail(f.getEmail());
            user.setProfilePic(new MediaEntity(null, f.getImageUrl()));
            result.add(user);

        }
        return result;
    }
}
