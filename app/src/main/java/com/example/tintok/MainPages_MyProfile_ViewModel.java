package com.example.tintok;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.DataLayer.ResponseEvent;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.example.tintok.Utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPages_MyProfile_ViewModel extends MainPages_Posts_ViewModel {
    MutableLiveData<String> username;
    MutableLiveData<String> location;
    MutableLiveData<Boolean> infoIsEdited;
    public MainPages_MyProfile_ViewModel(@NonNull Application application) {
        super(application);
        username = new MutableLiveData<>();
        location = new MutableLiveData<>();
        infoIsEdited = new MutableLiveData<>();
        infoIsEdited.setValue(false);
    }

    public LiveData<Boolean> getInfoIsEdited() {
        return infoIsEdited;
    }
    public void setInfoIsEdited(Boolean aBoolean) {
        infoIsEdited.setValue(aBoolean);
    }

    public LiveData<String> getUsername() {
        return username;
    }
    public void setUsername(String name) {
        username.setValue(name);
    }
    public LiveData<String> getLocation() {
        return location;
    }
    public void setLocation(String loc) {
        location.setValue(loc);
    }

    public MutableLiveData<ResponseEvent> getNetworkResponse(){
        return DataRepositoryController.getInstance().getNetworkResponse();
    }
    public MutableLiveData<UserProfile> getUserProfile(){
        return DataRepositoryController.getInstance().getUser();
    }

    public void submitNewPost(Post newPost) {
        DataRepositoryController.getInstance().submitNewPost(getApplication(), newPost);
    }

    @Override
    public MutableLiveData<ArrayList<Post>> getPosts() {
        return getUserProfile().getValue().getMyPosts();
    }
    public MutableLiveData<Boolean> getIsUserUpdating(){
        return DataRepositoryController.getInstance().getIsUserUpdating();
    }
    public void updateUserInfo(UserProfile userProfile){
        DataRepositoryController.getInstance().updateUserInfo(userProfile);
    }
    public void changePassword(List<String> passwords){
        DataRepositoryController.getInstance().changePassword(passwords);
    }
}
