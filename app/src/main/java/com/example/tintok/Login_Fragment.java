package com.example.tintok;

import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tintok.Communication.Communication;
import com.example.tintok.Communication.RestAPI_Entity;
import com.google.gson.JsonObject;

public class Login_Fragment extends Fragment {


    public static Login_Fragment newInstance() {
        return new Login_Fragment();
    }

    private Communication communication;
    private Button loginButton, registerButton;
    private ProgressBar loadingBar;
    private TextView status;
    private EditText email,password;
    private TextView forget;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void init(){
        communication = Communication.getInstance();
        loginButton = getView().findViewById(R.id.sign_inButton);
        registerButton = getView().findViewById(R.id.sign_upButton);
        status = getView().findViewById(R.id.status);
        loadingBar = getView().findViewById(R.id.progressBar);
        email = getView().findViewById(R.id.emailInput);
        password = getView().findViewById(R.id.passInput);
        forget = getView().findViewById(R.id.forget_account_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                HandleLogin();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment, Sign_up_Fragment.newInstance()).addToBackStack("Login").commit();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment, ForgetPasswordFragment.newInstance()).addToBackStack("Login").commit();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void HandleLogin(){
        email.onEditorAction(EditorInfo.IME_ACTION_DONE);
        if(email.getText().toString().length() <= 5 || password.getText().toString().length()<=5){
            status.setVisibility(View.VISIBLE);
            status.setText("Invalid Email or Password");
            return;
        }

        status.setVisibility(View.VISIBLE);
        status.setText("Signing in...");
        loadingBar.setVisibility(View.VISIBLE);
        JsonObject data = new JsonObject();
        data.addProperty("email", email.getText().toString());
        data.addProperty("password", password.getText().toString());
        // Communication.getInstance().emitEvent("login", data);
        communication.LoginRequest(data, new RestAPI_Entity.RestApiListener(){

            @Override
            public void onSuccess(RestAPI_Entity.AbstractResponseEntity response) {
                RestAPI_Entity.StatusResponseEntity res = (RestAPI_Entity.StatusResponseEntity)response;
                if(((RestAPI_Entity.StatusResponseEntity) res).status ){

                    communication.setToken(((RestAPI_Entity.StatusResponseEntity) res).mToken);
                    Intent intent = new Intent(getActivity(), Activity_InitData.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.animation_in, R.anim.animation_out);
                    getActivity().finish();
                }
                else{
                    status.setVisibility(View.VISIBLE);
                    status.setText("Signing in failed: "+ res.reason);
                    loadingBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure() {
                status.setVisibility(View.VISIBLE);
                status.setText("Signing in failed");
            }
        });
    }



}