package com.example.tintok;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.ChatRoom;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.MessageEntity;
import com.example.tintok.Utils.EmoticonHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_Chatroom_ViewModel extends AndroidViewModel {

    private EmoticonHandler mEmoiconHandler = null;
    private MutableLiveData<ArrayList<MessageEntity>> messageEntities = null;

    public Activity_Chatroom_ViewModel(@NonNull Application application) {
        super(application);
    }

    public EmoticonHandler getEmoticonHandler(Context mContext, EditText editText){
        if(mEmoiconHandler == null)
            mEmoiconHandler = new EmoticonHandler(mContext, editText);
        return mEmoiconHandler;
    }

    public MutableLiveData<ArrayList<MessageEntity>> getMessEntity(String roomID){
        if(messageEntities == null)
            messageEntities = this.getChatRoomByID(roomID).getMessageEntities();
        return messageEntities;
    }

    public ChatRoom getChatRoomByID(String id){
        return DataRepositoryController.getInstance().getChatRoomByID(id);
    }

    void handleSendImg(String roomID, ArrayList<Uri> imgs) {
        Date now = Calendar.getInstance().getTime();
        for (Uri img : imgs) {
            DataRepositoryController.getInstance().emitNewMessage(getApplication(), roomID,new MessageEntity(DataRepositoryController.getInstance().getUser().getUserID(), new MediaEntity(img, null), now), "" );
        }

    }

    void handleSendImgFromCamera(String roomID, Bitmap m) {
        DataRepositoryController.getInstance().emitNewMessage(getApplication(), roomID, new MessageEntity(DataRepositoryController.getInstance().getUser().getUserID(), new MediaEntity(m), Calendar.getInstance().getTime()), "" );
    }

    void handleSendMessage(String roomID) {
        Pair<String, SpannableStringBuilder> newMsg = mEmoiconHandler.parseMessage();

        DataRepositoryController.getInstance().emitNewMessage(getApplication(), roomID,new MessageEntity(DataRepositoryController.getInstance().getUser().getUserID(), newMsg.second, Calendar.getInstance().getTime()), newMsg.first );
    }


}
