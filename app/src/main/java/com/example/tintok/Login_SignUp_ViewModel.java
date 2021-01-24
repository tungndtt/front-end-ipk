package com.example.tintok;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_Entity;
import com.example.tintok.Communication.RestAPI_model.LoginResponseForm;
import com.example.tintok.Communication.RestAPI_model.UnknownUserForm;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_SignUp_ViewModel extends AndroidViewModel {
    private RestAPI api;

    public Login_SignUp_ViewModel(Application app){
        super(app);
        this.api = Communication.getInstance().getApi();
    }

    public void loginRequest(String email, String password, requestListener listener){
        this.api.postLoginData(new UnknownUserForm("",email,password)).enqueue(new Callback<LoginResponseForm>() {
            @Override
            public void onResponse(Call<LoginResponseForm> call, Response<LoginResponseForm> response) {
                if(response.isSuccessful()){
                    LoginResponseForm body = response.body();
                    Communication.getInstance().setToken(body.getToken());
                    listener.requestSuccess();
                }
                else {
                    try {
                        JSONObject m = new JSONObject(response.errorBody().string());
                        listener.requestFail(m.getString("message"));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<LoginResponseForm> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                listener.connectionFail();
            }
        });
    }

    public void signUpRequest(String username, String email, String birthday, String password, requestListener listener){
        UnknownUserForm form = new UnknownUserForm(username, email, password);
        form.setBirthday(birthday);
        this.api.postRegisterData(form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) listener.requestSuccess();
                else listener.requestFail("Something wrong while sign up!");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                snackBarShow("Some errors occur in sign up!");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void resetPassword(String email, String password){
        this.api.resetPassword(new UserForm("",email,password)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    snackBarShow("Sending to email");
                } else {
                    snackBarShow("No email available!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                snackBarShow("Some errors occur in reset password!");
            }
        });
    }

    private void snackBarShow(String msg){
        Snackbar snackbar = Snackbar.make(((Activity)this.getApplication().getApplicationContext()).findViewById(R.id.fragment), msg, Snackbar.LENGTH_LONG);
        snackbar.setAction("Hide", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    interface requestListener{
        void requestSuccess();
        void requestFail(String reason);
        void connectionFail();
    }
}