package com.example.tintok;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.ChatRoom;
import com.example.tintok.Model.UserSimple;

import java.util.ArrayList;

public class MainPages_Chatroom_ViewModel extends ViewModel {


    public MutableLiveData<ArrayList<ChatRoom>> getChatrooms(){
        return DataRepositoryController.getInstance().getChatRooms();
    }

    public ArrayList<ChatRoom> filterByName(String filter){
        if(filter.isEmpty())
            return this.getChatrooms().getValue();
        DataRepositoryController data = DataRepositoryController.getInstance();
        String thisUserID = data.getUser().getUserID();
        ArrayList<ChatRoom> result = new ArrayList<>();
        ArrayList<ChatRoom> original = this.getChatrooms().getValue();
        for(ChatRoom room:original){
            UserSimple another = null;
            for(String id : room.getMemberIDs()){
                if(id.compareTo(thisUserID)!= 0){
                    another = data.getUserSimpleProfile(id);
                    break;
                }
            }
            if(another!=null){
                if(another.getUserName().contains(filter))
                    result.add(room);
            }
        }
        return result;
    }

}
