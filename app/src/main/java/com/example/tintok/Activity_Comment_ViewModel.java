package com.example.tintok;

import android.app.Application;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.CommunicationEvent;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.PostForm;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.Comment;
import com.example.tintok.Model.MediaEntity;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Comment_ViewModel extends AndroidViewModel {

    public Activity_Comment_ViewModel(@NonNull Application application) {
        super(application);
        this.api = Communication.getInstance().getApi();
        this.listComments = new MutableLiveData<>();
        Communication.getInstance().get_socket().on(CommunicationEvent.NEW_COMMENT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String author_id = (String) args[0];
                String author_name = (String) args[1];
                String comment = (String) args[2];
                String post_id = (String) args[3];
                ArrayList<Comment> comments = listComments.getValue();
                comments.add(0,new Comment("some id", author_id, new SpannableStringBuilder(), null));
                listComments.postValue(comments);
            }
        });
    }

    private RestAPI api;
    private MutableLiveData<ArrayList<Comment>> listComments;


    public MutableLiveData<ArrayList<Comment>> getListComments() {
        return listComments;
    }

    public void getComments(String post_id) {
        api.getComments(post_id).enqueue(new Callback<PostForm>() {
            @Override
            public void onResponse(Call<PostForm> call, Response<PostForm> response) {
                if(response.isSuccessful()){
                    ArrayList<Comment> comments = response.body().getComments();
                    Log.i("Length", ""+comments.size());
                    listComments.postValue(comments);
                } else {
                    Toast.makeText(Activity_Comment_ViewModel.this.getApplication(),"Request fails", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostForm> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    public void sendComment(String comment, String post_id){
        Communication.getInstance()
                .get_socket()
                .emit(
                        CommunicationEvent.SEND_COMMENT,
                        post_id,
                        DataRepositoryController.getInstance().getUser().getUserID(),
                        DataRepositoryController.getInstance().getUser().getUserName(),
                        comment
                );
    }

    public void leavePost(String post_id){
        Communication.getInstance().get_socket().emit(CommunicationEvent.LEAVE_POST, post_id);
    }
}
