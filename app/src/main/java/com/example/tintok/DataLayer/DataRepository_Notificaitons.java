package com.example.tintok.DataLayer;

import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI_model.NotificationForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Notification;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserSimple;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Communication.getInstance().getApi().getAllNotifications().enqueue(new Callback<ArrayList<NotificationForm>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationForm>> call, Response<ArrayList<NotificationForm>> response) {
                ArrayList<NotificationForm> notis = response.body();
                ArrayList<Notification> clientNoti  = new ArrayList<>();
                for(NotificationForm noti : notis){
                    Notification.NotificationType m = Notification.NotificationType.COMMENT_POST;
                    if(noti.getType().compareTo(NotificationForm.NEW_COMMENT) == 0)
                        m = Notification.NotificationType.COMMENT_POST;
                    else if(noti.getType().compareTo(NotificationForm.NEW_REACTION) == 0)
                        m = Notification.NotificationType.LIKE_POST;

                    Notification newNoti = new Notification(m, noti.getDate(), noti.getAuthor_username(), noti.getAuthor_profile_pic(),
                            noti.getUrl(), noti.getPostID(), noti.getPost_author_id(), noti.getStatus());
                    clientNoti.add(newNoti);
                }
                notifications.postValue(clientNoti);
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationForm>> call, Throwable t) {

            }
        });
    }

    //Server part
}
