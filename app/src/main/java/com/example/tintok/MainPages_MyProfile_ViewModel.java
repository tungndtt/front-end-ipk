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
import com.example.tintok.Model.UserSimple;
import com.example.tintok.Utils.FileUtil;

import java.io.File;
import java.time.LocalDate;
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
    MutableLiveData<String> location, description;
    MutableLiveData<LocalDate> birthday;
    MutableLiveData<Integer> gender;

    public MainPages_MyProfile_ViewModel(@NonNull Application application) {
        super(application);
        username = new MutableLiveData<>();
        location = new MutableLiveData<>();
        birthday = new MutableLiveData<>();
        gender = new MutableLiveData<>();
        description = new MutableLiveData<>();
    }


    public void resetLiveData(){
        UserProfile user = getUserProfile().getValue();
        setUsername(user.getUserName());
        setLocation(user.getLocation());
        setGender(user.getGender().getI());
        setDate(user.getBirthday());
        setDescription(user.getDescription());
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
    public LiveData<String> getDescription() {
        return description;
    }
    public void setDescription(String desc) {
        description.setValue(desc);
    }
    public LiveData<LocalDate> getBirthday() {
        return birthday;
    }
    public void setDate(LocalDate date) {
        birthday.setValue(date);
    }
    public MutableLiveData<Integer> getGender() {
        return gender;
    }
    public void setGender(Integer g) {
        gender.setValue(g);
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
    public void updateUserInterests(ArrayList<Integer> interests){
        DataRepositoryController.getInstance().updateUserInterests(interests);
    }
    public void updateUserProfilePicture(String url){
        DataRepositoryController.getInstance().updateUserProfilePicture(url);
    }
    public void submitNewProfilePicture(Post newPost){
        DataRepositoryController.getInstance().submitNewProfilePicture(getApplication(), newPost);
    }
}
