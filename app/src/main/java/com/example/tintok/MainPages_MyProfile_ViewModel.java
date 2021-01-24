package com.example.tintok;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.example.tintok.Utils.FileUtil;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPages_MyProfile_ViewModel extends MainPages_Posts_ViewModel {
    public MainPages_MyProfile_ViewModel(@NonNull Application application) {
        super(application);
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
}
