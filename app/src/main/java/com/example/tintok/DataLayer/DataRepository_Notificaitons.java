package com.example.tintok.DataLayer;

import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Model.Notification;
import com.example.tintok.Model.Post;

import java.util.ArrayList;

public class DataRepository_Notificaitons {
    MutableLiveData<ArrayList<Notification>> notifications;
    DataRepositoryController controller;

    public DataRepository_Notificaitons(DataRepositoryController controller){
        notifications = new MutableLiveData<>(new ArrayList<>());
        this.controller = controller;
    }

    public MutableLiveData<ArrayList<Notification>> getNotifications(){
        return notifications;
    }

    public void setNewfeedPosts(Notification... newNoti){
        ArrayList<Notification> mNotifications = notifications.getValue();
        for(Notification p : newNoti){
            mNotifications.add(p);
        }
        notifications.setValue(mNotifications);

    }
    public void postNewfeedPosts(Notification... newNoti){
        ArrayList<Notification> mNotifications = notifications.getValue();
        for(Notification p : newNoti){
            mNotifications.add(p);
        }
        notifications.postValue(mNotifications);

    }

    public void initData(){

    }

    //Server part
}
