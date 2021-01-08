package com.example.tintok.DataLayer;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.UserSimple;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository_MatchingPeople {
    MutableLiveData<ArrayList<UserSimple>> matchingPeople;
    DataRepositoryController controller;

    public DataRepository_MatchingPeople(DataRepositoryController controller){
        this.matchingPeople = new MutableLiveData<>(new ArrayList<>());
        this.controller = controller;
    }

    public MutableLiveData<ArrayList<UserSimple>> getMatchingPeople(){
        return  this.matchingPeople;
    }

    public void setNewPeople(UserSimple... newPeople){
        ArrayList<UserSimple> mPeople =  this.matchingPeople.getValue();
        for(UserSimple p : newPeople){
            mPeople.add(p);
        }
        this.matchingPeople.setValue(mPeople);

    }
    public void postNewPeople(UserSimple... newPeople){
        ArrayList<UserSimple> mPeople = this.matchingPeople.getValue();
        for(UserSimple p : newPeople){
            mPeople.add(p);
        }
        this.matchingPeople.postValue(mPeople);

    }

    public void initData(){
        RestAPI api = Communication.getInstance().getApi();
        if(api != null){
            api.getAllUsers().enqueue(new Callback<ArrayList<UserForm>>() {
                @Override
                public void onResponse(Call<ArrayList<UserForm>> call, Response<ArrayList<UserForm>> response) {
                    if(response.isSuccessful()){
                        ArrayList<UserForm> forms = response.body();
                        ArrayList<UserSimple> people = new ArrayList<>();
                        for(UserForm f : forms){
                            UserSimple user = new UserSimple();
                            user.setUserID(f.getId());
                            user.setUserName(f.getUsername());
                            user.setDescription("");
                            user.setEmail(f.getEmail());
                            user.setProfilePic(new MediaEntity(null, f.getImageUrl()));
                            people.add(user);
                            Log.e("Mathcingpeeps", f.getImageUrl());
                        }
                        postNewPeople(people.toArray(new UserSimple[people.size()]));
                    } else {
                        Log.e("Info", "Cannot get users");
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
        } else {

        }
    }

    //Server part
}
