package com.example.tintok.DataLayer;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Model.ChatRoom;
import com.example.tintok.Model.MessageEntity;
import com.example.tintok.Model.Notification;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.example.tintok.Model.UserSimple;

import java.util.ArrayList;
import java.util.HashMap;

public class DataRepositoryController {
    public static Context applicationContext;
    private static DataRepositoryController instance;
    public static synchronized DataRepositoryController getInstance(){
        if(instance == null)
            instance = new DataRepositoryController();
        return instance;
    }

    DataRepositiory_Chatrooms dataRepositiory_chatrooms;
    DataRepository_CurrentUser dataRepository_currentUser;
    DataRepository_MatchingPeople dataRepository_matchingPeople;
    DataRepository_Notificaitons dataRepository_notificaitons;
    DataRepository_Posts dataRepository_posts;

    DataRepository_UserSimple dataRepository_userSimple;

    public void submitNewPost(Context mContext, Post newPost) {
        this.dataRepository_currentUser.submitNewPost(mContext, newPost);
    }

    public boolean isDataReady(){
        return true;
    }


    private DataRepositoryController(){
       dataRepository_userSimple = new DataRepository_UserSimple();

        dataRepositiory_chatrooms = new DataRepositiory_Chatrooms(this);
        dataRepository_currentUser = new DataRepository_CurrentUser(this);
        dataRepository_matchingPeople = new DataRepository_MatchingPeople(this);
        dataRepository_notificaitons = new DataRepository_Notificaitons(this);
        dataRepository_posts = new DataRepository_Posts(this);
    }

    public void initDataFromServer(){
        Communication.getInstance().initScoket();
        Log.e("DataInitRepo", "at: "+ Communication.getInstance().get_socket());
        dataRepository_currentUser.initData();
        dataRepositiory_chatrooms.initData();
        dataRepository_matchingPeople.initData();
        dataRepository_notificaitons.initData();
        dataRepository_posts.initData();

        /*ArrayList<ChatRoom> mChatRooms = new ArrayList<>();
        ArrayList<MessageEntity> msgs = new ArrayList<>();
        MutableLiveData<ArrayList<MessageEntity>> msgsM = new MutableLiveData<>();
        msgsM.postValue(msgs);
        ChatRoom room = new ChatRoom();
        room.setChatRoomID("test");
        room.setMutableMessengerEntities(msgsM);
        mChatRooms.add(room);
        chatRooms.setValue(mChatRooms);*/

        UserProfile mUser = new UserProfile();



        //fetchData

        Log.e("DataRepo","InitDataDone ");


    }

//region Chat Rooms

    public MutableLiveData<ArrayList<ChatRoom>> getChatRooms() {
        return dataRepositiory_chatrooms.getChatrooms();
    }
    public ChatRoom getChatRoomByID(String id){
        return dataRepositiory_chatrooms.getChatRoomByID(id);
    }
    public void emitNewMessage(Context mContext, String roomID, MessageEntity newMsg, String encoded){
        dataRepositiory_chatrooms.emitNewMessage( mContext, roomID, newMsg, encoded);
    }
    public void addNewMessageListener(DataRepositiory_Chatrooms.OnNewMessagesListener mListener){
        dataRepositiory_chatrooms.addNewMessageListener(mListener);
    }
    public void removeNewMessageListener(DataRepositiory_Chatrooms.OnNewMessagesListener mListener){
       dataRepositiory_chatrooms.removeNewMessageListener(mListener);
    }
//endregion



//region CurrentUser
    public MutableLiveData<UserProfile> getUser() {
        return  dataRepository_currentUser.currentUser;
    }
    public boolean isThisUserLikedPost(Post p){
        if(p.likers == null)
            return false;
        return p.likers.contains(getUser().getValue().getUserID());
    }
    public boolean isThisUserSubscribedPost(Post p){
        return false;
        //To Do
    }
//endregion

//region matchingPeople
    public MutableLiveData<ArrayList<UserSimple>> getMatchingPeople() {
        return dataRepository_matchingPeople.getMatchingPeople();
    }
//endregion
    public MutableLiveData<ArrayList<Post>> getNewfeedPosts() {
        return dataRepository_posts.getNewfeedPosts();
    }

//region Notifications
    public MutableLiveData<ArrayList<Notification>> getNotifications() {
        return dataRepository_notificaitons.getNotifications();
    }
    public void addNotificationListener(DataRepository_Notificaitons.OnNewNotificationListener mListener){
       dataRepository_notificaitons.addNotificationListener(mListener);
    }
    public void removeNotificationListener(DataRepository_Notificaitons.OnNewNotificationListener mListener){
        dataRepository_notificaitons.removeNotificationListener(mListener);
    }
//endregion



//region UserSimpleCache
    public UserSimple getUserSimpleProfile(String id){
        UserSimple result = dataRepository_userSimple.findUserSimpleinCahe(id);
        return result;
    }

    public void AddUserProfileChangeListener(DataRepository_UserSimple.OnUserProfileChangeListener newListener){
        dataRepository_userSimple.addListener(newListener);
    }

    public void RemoveUserProfileChangeListener(DataRepository_UserSimple.OnUserProfileChangeListener newListener){
        dataRepository_userSimple.removeListener(newListener);
    }

//endregion

    //Network, data update tasks


//region CustomObserver
    //should be called if new Data received
    public void notifyObserver(){
        for(DataObserver dataObserver:dataObservers)
            dataObserver.notifyDataChange(this);
    }

    ArrayList<DataObserver> dataObservers = new ArrayList<>();

    public interface DataObserver{
        public void notifyDataChange(DataRepositoryController dataRepositoryController);
    }
//endregion
}
