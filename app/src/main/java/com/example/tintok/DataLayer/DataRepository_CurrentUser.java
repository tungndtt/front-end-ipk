package com.example.tintok.DataLayer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Post;
import com.example.tintok.Model.UserProfile;
import com.example.tintok.Model.UserSimple;
import com.example.tintok.Utils.FileUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository_CurrentUser {
    MutableLiveData<UserProfile> currentUser;
    DataRepositoryController controller;
    public long lastSeen;
    MutableLiveData<Boolean> isUserUpdating;
    MutableLiveData<ResponseEvent>  networkStatus;

    public DataRepository_CurrentUser(DataRepositoryController controller){
        this.controller = controller;
        currentUser = new MutableLiveData<>();
        isUserUpdating = new MutableLiveData<>();
        networkStatus = new MutableLiveData<>();
    }


    public void initData() {
        RestAPI api = Communication.getInstance().getApi();
        api.getUser().enqueue(new Callback<UserForm>() {
            @Override
            public void onResponse(Call<UserForm> call, Response<UserForm> response) {
                if(response.isSuccessful()){
                    UserForm form = response.body();
                    UserProfile currUser = new UserProfile();
                    currUser.setUserName(form.getUsername());
                    currUser.setUserID(form.getId());
                    currUser.setEmail(form.getEmail());
                    currUser.setBirthday(LocalDate.parse(form.getBirthday(),  DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    currUser.setLocation(form.getLocation());
                    currUser.setDescription(form.getDescription());
                    currUser.setGender(form.getGender());
                    currUser.setProfilePic(new MediaEntity(form.getImageUrl()));
                    currUser.userInterests.postValue(form.getInterests());//setUserInterests(form.getInterests()); //LIVEDATA
                    ArrayList<Post> photos = currUser.getMyPosts().getValue();
                    for(PostForm post : form.getPosts()){
                        Post tmp = new Post(post.getId(), post.getStatus(), post.getAuthor_id(), new MediaEntity(post.getImageUrl()));
                        tmp.likers = post.getLikes() == null?new ArrayList<>():post.getLikes();
                        photos.add(tmp);
                    }

                    currUser.myPosts.postValue(photos);
                    ArrayList<String> dummy = currUser.getFollowers().getValue();
                    dummy.addAll(form.getFollowers());
                    Log.e("DataRepo_CurrentU","follower = " + form.getFollowers());
                    currUser.postFollowers(dummy);
                    dummy = currUser.getFollowing().getValue();
                    dummy.addAll(form.getFollowing());
                    currUser.postFollowering(dummy);
                    dummy = currUser.getFollowingPost().getValue();
                    dummy.addAll(form.getFollowing_posts());
                    currUser.getFollowingPost().postValue(dummy);


                    currentUser.postValue( currUser);
                    lastSeen = form.getTime();
                } else {
                    Log.e("Info", "Response fails");
                }
            }

            @Override
            public void onFailure(Call<UserForm> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Log.e("Error", "Some errors occur");
            }
        });
    }

    public void submitNewPost(Context mContext, Post newPost) {
        RestAPI api = Communication.getInstance().getApi();
        if(api != null){
            MultipartBody.Part part = FileUtil.prepareImageFileBody(mContext, "upload", newPost.getImage());
            RequestBody user_id = RequestBody.create(MultipartBody.FORM, newPost.getAuthor_id());
            RequestBody status = RequestBody.create(MultipartBody.FORM, newPost.getStatus());
            String[] split = currentUser.getValue().getProfilePic().url.split("/");
            RequestBody profile_path = RequestBody.create(MultipartBody.FORM, split[split.length-1]);
            api.uploadFile(part, user_id, status, profile_path).enqueue(new Callback<PostForm>() {
                @Override
                public void onResponse(Call<PostForm> call, Response<PostForm> response) {
                    if(response.isSuccessful()){
                        // set id for the post
                        PostForm form = response.body();
                        newPost.setId(form.getId());
                        newPost.getImage().url = form.getImageUrl();
                        newPost.likers = new ArrayList<>();

                        // do something with newPost ...
                        ArrayList<Post> mPosts = currentUser.getValue().myPosts.getValue();
                        mPosts.add(0,newPost);
                        currentUser.getValue().myPosts.postValue(mPosts);

                    } else {
                        // Toast.makeText(getApplication(), "Fail to get response", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PostForm> call, Throwable t) {
                    try {
                        throw t;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    //Toast.makeText(getApplication(), "Connection fails", Toast.LENGTH_LONG).show();
                }
            });
        } else {
           // Toast.makeText(this.getApplication(), "No file to upload", Toast.LENGTH_LONG).show();
        }
    }

    public void UpdateSubcribedPost(Post p) {
        ArrayList<String> followingPost = this.currentUser.getValue().getFollowingPost().getValue();
        if(followingPost.contains(p.getId()))
            followingPost.remove(p.getId());
        else
            followingPost.add(p.getId());
        this.currentUser.getValue().getFollowingPost().postValue(followingPost);
    }
    private UserProfile setUpdatedUserProfile(UserForm userForm){
        UserProfile userProfile = currentUser.getValue();
        userProfile.setUserName(userForm.getUsername());
        userProfile.setGender(userForm.getGender());
        userProfile.setBirthday(LocalDate.parse(userForm.getBirthday(),  DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        userProfile.setLocation(userForm.getLocation());
        userProfile.setDescription(userForm.getDescription());
        return userProfile;

    }
    public void updateUserInfo(UserProfile userProfile){

        isUserUpdating.setValue(true);
        RestAPI api = Communication.getInstance().getApi();
        UserForm userForm = new UserForm(userProfile.getUserName(), "", "");
        userForm.setId(userProfile.getUserID());
        userForm.setBirthday(userProfile.getBirthday().toString());
        userForm.setLocation(userProfile.getLocation());
        userForm.setDescription(userProfile.getDescription());
        userForm.setGender(userProfile.getGender().getI());

        if(api != null){
            api.updateUserInfo(userForm).enqueue(new Callback<UserForm>() {
                @Override
                public void onResponse(Call<UserForm> call, Response<UserForm> response) {
                    if(response.isSuccessful()){
                        currentUser.postValue(setUpdatedUserProfile(response.body()));
                        UserSimple newUser = new UserSimple();
                        newUser.setUserID(currentUser.getValue().getUserID());
                        newUser.setUserName(currentUser.getValue().getUserName());
                        newUser.setProfilePic(new MediaEntity(currentUser.getValue().getProfilePic().url));
                        DataRepositoryController.getInstance().dataRepository_userSimple.updateUserSimpleInCache(newUser);
                        //TODO send status
                        Log.e("onResp", response.message());
                        networkStatus.postValue(new ResponseEvent(ResponseEvent.Type.USER_UPDATE, response.message()));
                    }
                    isUserUpdating.postValue(false);

                }

                @Override
                public void onFailure(Call<UserForm> call, Throwable t) {
                    Log.e("response", "failed");
                    isUserUpdating.postValue(false);

                }
            });
        }

    }

    public void updateUserPassword(List<String> passwords){
        isUserUpdating.setValue(true);
        RestAPI api = Communication.getInstance().getApi();

        UserForm userForm = new UserForm("", currentUser.getValue().getEmail(), passwords.get(0));
        userForm.setId(currentUser.getValue().getUserID());
        userForm.setNew_password(passwords.get(1));

        if(api != null){
            api.updateUserPassword(userForm).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("onRe", "response " + response.message());
                    networkStatus.postValue(new ResponseEvent(ResponseEvent.Type.PASSWORD, response.message())); // Created or Unauthorized

                    isUserUpdating.postValue(false);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("onFail", "response " + t.getMessage());
                    isUserUpdating.postValue(false);
                }
            });

        }

    }
    //TODO:
    public void updateUserProfilePic(UserProfile userProfile){}
    //TODO:
    public void updateUserInterests(ArrayList<Integer> newInterests){
        isUserUpdating.setValue(true);
        RestAPI api = Communication.getInstance().getApi();
        UserForm userForm = new UserForm("", "", "");
        userForm.setInterests(newInterests);
        if(api != null){
            api.updateUserInterests(userForm).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.isSuccessful())
                       currentUser.getValue().setUserInterests(newInterests); //DataRepositoryController.getInstance().dataRepository_currentUser.currentUser.getValue().setUserInterests(newInterests);

                    networkStatus.postValue(new ResponseEvent(ResponseEvent.Type.INTEREST_UPDATE, response.message()));
                    isUserUpdating.postValue(false);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }



    }

}
