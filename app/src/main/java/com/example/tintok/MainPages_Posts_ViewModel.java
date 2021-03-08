package com.example.tintok;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.CommunicationEvent;
import com.example.tintok.CustomView.AfterRefreshCallBack;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.Post;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainPages_Posts_ViewModel extends AndroidViewModel {
    public MainPages_Posts_ViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<ArrayList<Post>> getPosts(){

        return DataRepositoryController.getInstance().getNewfeedPosts();
    }

    public String getCurrentUserID() {
        return DataRepositoryController.getInstance().getUser().getValue().getUserID();
    }

    private void submitLike(String id) {
        Communication.getInstance().get_socket().emit(CommunicationEvent.LIKE_POST, getCurrentUserID(), id);
    }


    private void unsubmitLike(String id) {
        Communication.getInstance().get_socket().emit(CommunicationEvent.UNLIKE_POST, getCurrentUserID(), id);
    }

    private void subscribePost(String id) {
        Communication.getInstance().get_socket().emit(CommunicationEvent.FOLLOW_POST, getCurrentUserID(), id);
    }

    private void unsubscribePost(String id) {
        Communication.getInstance().get_socket().emit(CommunicationEvent.UNFOLLOW_POST, getCurrentUserID(), id);
    }

    public void UserPressLike(Post post){
        if(DataRepositoryController.getInstance().isThisUserLikedPost(post)){
            post.likers.remove(getCurrentUserID());
            unsubmitLike(post.getId());
        }
        else {
            post.likers.add(getCurrentUserID());
            submitLike(post.getId());
        }

        Log.e("Post_VM", "at post " + post.getId());

        if(post.comments != null){
            int type = 0;
            ArrayList<Post> posts = DataRepositoryController.getInstance().getNewfeedPosts().getValue();
            int index = posts.indexOf(post);
            if(index == -1){
                posts = DataRepositoryController.getInstance().getUser().getValue().myPosts.getValue();
                index = posts.indexOf(post);
                type = 1;
            }

            if(index != -1){
                Post p = posts.get(index);
                Log.e("Post_VM", "before "+p.likers);
                if(p.likers.contains(getCurrentUserID()) )
                    p.likers.remove(getCurrentUserID());
                else
                    p.likers.add(getCurrentUserID());
                Log.e("Post_VM", "after "+p.likers);
                if(type == 0)
                    DataRepositoryController.getInstance().getNewfeedPosts().postValue(posts);
                else
                    DataRepositoryController.getInstance().getUser().getValue().myPosts.postValue(posts);
            }
        }
    }

    public void UserPressSubscribe(Post post){
        DataRepositoryController.getInstance().UpdateFollowingPost(post);
        if(DataRepositoryController.getInstance().isThisUserSubscribedPost(post)){
            subscribePost(post.getId());
        }
        else{
            unsubscribePost(post.getId());
        }
    }

    public void refreshData(AfterRefreshCallBack e) {
        DataRepositoryController.getInstance().refreshPost(e);
    }
}