package com.example.tintok;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI;
import com.example.tintok.Communication.RestAPI_model.UserForm;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordViewModel extends AndroidViewModel {
    private RestAPI api;

    public ForgetPasswordViewModel(Application app){
        super(app);
        this.api = Communication.getInstance().getApi();
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
                snackBarShow("Some errors occur!");
            }
        });
    }

    private void snackBarShow(String msg){
        Snackbar snachbar = Snackbar.make(((Activity)this.getApplication().getApplicationContext()).findViewById(R.id.fragment), msg, Snackbar.LENGTH_LONG);
        snachbar.setAction("Hide", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snachbar.dismiss();
            }
        }).show();
    }
}
