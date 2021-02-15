package com.example.tintok.DataLayer;

import android.util.Log;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.UserSimple;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository_UserSimple {
    HashMap<String, UserSimple> cacheQueriedUserSimple;
    public DataRepository_UserSimple(){
        cacheQueriedUserSimple = new HashMap<>();
        mListeners = new ArrayList<>();
    }

    public void Cache(UserSimple newUser){
        this.cacheQueriedUserSimple.put(newUser.getUserID(), newUser);
    }
    public UserSimple findUserSimpleinCahe(String id){
        UserSimple m =  this.cacheQueriedUserSimple.get(id);
        if(m == null && !onQueries.contains(id)){
            ArrayList<String> ids = new ArrayList<>();
            ids.add(id);
            this.UpdateProfile(ids);
        }
        return  m ;
    }
    public void updateUserSimpleInCache(UserSimple user){
        this.cacheQueriedUserSimple.put(user.getUserID(), user);
        for(OnUserProfileChangeListener l:mListeners)
            l.onProfileChange(user);
    }

    private ArrayList<String> onQueries = new ArrayList<>();
    public void UpdateProfile(ArrayList<String> ids){
        onQueries.addAll(ids);

        HashMap<String,ArrayList<String>> hm = new HashMap<>();
        hm.put("users",ids);
        Communication.getInstance().getApi().getUsersById(hm).enqueue(new Callback<ArrayList<UserForm>>() {
            @Override
            public void onResponse(Call<ArrayList<UserForm>> call, Response<ArrayList<UserForm>> response) {
                if(response.isSuccessful()){
                    ArrayList<UserForm> users = response.body();
                    for(UserForm user: users){
                        UserSimple newUser = new UserSimple();
                        newUser.setUserID(user.getId());
                        newUser.setUserName(user.getUsername());
                        newUser.setProfilePic(new MediaEntity(user.getImageUrl()));
                        Cache(newUser);

                        for(OnUserProfileChangeListener l:mListeners)
                            l.onProfileChange(newUser);
                    }
                    onQueries.removeAll(ids);
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserForm>> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
    ArrayList<OnUserProfileChangeListener> mListeners;
    public interface OnUserProfileChangeListener{
        void onProfileChange(UserSimple user);
    }
    public void addListener(OnUserProfileChangeListener newListener){
        if(!mListeners.contains(newListener))
            mListeners.add(newListener);
    }
    public void removeListener(OnUserProfileChangeListener newListener){
        mListeners.remove(newListener);
    }
}
