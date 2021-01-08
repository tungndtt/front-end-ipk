package com.example.tintok;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_ViewProfile_ViewModel extends AndroidViewModel {

    private RestAPI api;

    public Activity_ViewProfile_ViewModel(@NonNull Application application) {
        super(application);
        this.profile = new MutableLiveData<>(null);
        this.api = Communication.getInstance().getApi();
    }

    private MutableLiveData<UserProfile> profile;

    public MutableLiveData<UserProfile> getProfile() {
        return profile;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getUserProfile(String id){
        this.api.getUserProfile(id).enqueue(new Callback<UserForm>() {
            @Override
            public void onResponse(Call<UserForm> call, Response<UserForm> response) {
                if(response.isSuccessful()){
                    new GetUserProfileTask().execute(response.body());
                }
                else{
                    Toast.makeText(Activity_ViewProfile_ViewModel.this.getApplication(), "Cannot get the user", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserForm> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }


    private class GetUserProfileTask extends AsyncTask<UserForm, Void, UserProfile> {

        @Override
        protected UserProfile doInBackground(UserForm... dataForms) {
            List<PostForm> posts = dataForms[0].getPosts();
            String name = dataForms[0].getUsername();
            ArrayList<Post> new_posts = new ArrayList<>();
            for(PostForm post : posts){
                MediaEntity media = new MediaEntity(post.getImageUrl());
                new_posts.add(new Post(post.getId(), post.getStatus(), post.getAuthor_id(), name, media));
            }
            UserProfile result = new UserProfile();
            result.setUserName(name);
            result.myPosts.postValue(new_posts);
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(UserProfile user) {
            profile.postValue(user);
        }
    }
}
