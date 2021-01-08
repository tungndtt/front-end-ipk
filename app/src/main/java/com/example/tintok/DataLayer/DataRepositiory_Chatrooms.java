package com.example.tintok.DataLayer;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.CommunicationEvent;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.ChatForm;
import com.example.tintok.Communication.RestAPI_model.MessageForm;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.Model.ChatRoom;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.MessageEntity;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserSimple;
import com.example.tintok.Utils.EmoticonHandler;
import com.example.tintok.Utils.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepositiory_Chatrooms {
    MutableLiveData<ArrayList<ChatRoom>> chatrooms;
    DataRepositoryController controller;
    Socket socket;

    RestAPI api;

    public DataRepositiory_Chatrooms(DataRepositoryController controller){
        this.chatrooms = new MutableLiveData<>(new ArrayList<>());
        this.controller = controller;

    }

    public void socketListener(){
        //this.socket = Communication.getInstance().get_socket();
        Communication.getInstance().get_socket().on(CommunicationEvent.EVENT_NEW_MASSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String roomID = (String) args[0];
                String authorID = (String) args[1];
                String url = (String) args[2];
                String msg = (String) args[3];
                MessageEntity newMsg = null;
                if(!msg.isEmpty()){

                    newMsg = new MessageEntity(authorID, EmoticonHandler.parseMessageFromString(DataRepositoryController.applicationContext, msg), Calendar.getInstance().getTime());
                }else{
                    newMsg = new MessageEntity(authorID, new MediaEntity(null,url), Calendar.getInstance().getTime());
                }
                ChatRoom r = getChatRoomByID(roomID);
                if(r != null) {
                    ArrayList<ChatRoom> rooms = chatrooms.getValue();
                    ArrayList<MessageEntity> msgs = r.getMessageEntities().getValue();
                    msgs.add(newMsg);
                    r.postMessageEntities(msgs);
                    chatrooms.postValue(rooms);
                }

            }
        });
    }

    public MutableLiveData<ArrayList<ChatRoom>> getChatrooms(){
        return  this.chatrooms;
    }

    public void setNewChatRoom(ChatRoom... newRoom){
        ArrayList<ChatRoom> mPeople =  this.chatrooms.getValue();
        for(ChatRoom p : newRoom){
            mPeople.add(p);
        }
        this.chatrooms.setValue(mPeople);

    }
    public void postNewChatRoom(ChatRoom... newRoom){
        ArrayList<ChatRoom> mPeople = this.chatrooms.getValue();
        for(ChatRoom p : newRoom){
            mPeople.add(p);
        }
        this.chatrooms.postValue(mPeople);

    }

    public void initData(){
        this.api = Communication.getInstance().getApi();
        if(this.api != null){
            this.api.getAllChatRooms().enqueue(new Callback<ArrayList<ChatForm>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatForm>> call, Response<ArrayList<ChatForm>> response) {
                    if(response.isSuccessful()){
                        ArrayList<ChatForm> forms = response.body();
                        ArrayList<ChatRoom> rooms = chatrooms.getValue();
                        for(ChatForm c :forms){
                            ChatRoom r = new ChatRoom();
                            r.setChatRoomID(c.getRoom_id());
                            r.setMemberIDs(new ArrayList<>(Arrays.asList(new String[]{c.getUser_1(),c.getUser_2()})));
                            ArrayList<MessageForm> msgs = c.getMessages();
                            ArrayList<MessageEntity> myMSG = new ArrayList<>();
                            for(MessageForm m : msgs){
                               if(m.getMessage() != null){
                                   MessageEntity mm = new MessageEntity(m.getAuthor_id(), EmoticonHandler.parseMessageFromString(DataRepositoryController.applicationContext, m.getMessage()),
                                           Calendar.getInstance().getTime());
                                   myMSG.add(mm);
                               }
                               else if(m.getImageUrl()!= null){
                                   MessageEntity mm = new MessageEntity(m.getAuthor_id(), new MediaEntity(null,m.getImageUrl()), Calendar.getInstance().getTime());
                                   myMSG.add(mm);
                               }
                            }
                            r.postMessageEntities(myMSG);
                            rooms.add(r);
                        }
                        chatrooms.postValue(rooms);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChatForm>> call, Throwable t) {
                    try {
                        throw t;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
        } else{
            Log.e("Alert", "No connection found");
        }
        socketListener();
    }

    public ChatRoom getChatRoomByID(String id){
        ArrayList<ChatRoom> chatRooms = DataRepositoryController.getInstance().getChatRooms().getValue();

        for(ChatRoom room : chatRooms){
            if(room.getChatRoomID().compareTo(id) == 0)
                return room;
        }
        return null;
    }

    //Server part

    public void emitNewMessage(Context mContext, String roomID, MessageEntity newMsg, String encoded){
        ChatRoom r = getChatRoomByID(roomID) ;

        String receiver = "";
        for(String id : r.getMemberIDs())
            if(!id.equals(newMsg.getAuthorID())){
                receiver = id;
                break;
            }
        //pushMsgToServer
        api = Communication.getInstance().getApi();
        if(api != null) {
            if (newMsg.getMedia() != null) {
                MultipartBody.Part part = FileUtil.prepareImageFileBody(mContext, "upload", newMsg.getMedia());
                RequestBody sender_id = RequestBody.create(MultipartBody.FORM, newMsg.getAuthorID());
                RequestBody receiver_id = RequestBody.create(MultipartBody.FORM, receiver);
                RequestBody roomId = RequestBody.create(MultipartBody.FORM, r.getChatRoomID());
                api.uploadFileFromMessage(part, roomId, sender_id, receiver_id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // set id for the post
                            Log.i("Status", "Send file successfully");

                        } else {
                            // Toast.makeText(getApplication(), "Fail to get response", Toast.LENGTH_LONG).show();
                            Log.e("Status", "Send file fails");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            throw t;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        //Toast.makeText(getApplication(), "Connection fails", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Communication.getInstance().get_socket().emit(CommunicationEvent.EVENT_NEW_MASSAGE, roomID, newMsg.getAuthorID(), receiver, encoded);
            }
        }
    }
}
