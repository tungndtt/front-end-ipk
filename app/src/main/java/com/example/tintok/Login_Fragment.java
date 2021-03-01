package com.example.tintok;

import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class Login_Fragment extends Fragment implements Login_SignUp_ViewModel.requestListener {


    public static Login_Fragment newInstance(Login_SignUp_ViewModel viewModel) {
        return new Login_Fragment(viewModel);
    }

    public Login_Fragment(){

    }

    public Login_Fragment(Login_SignUp_ViewModel viewModel){
        this.viewModel = viewModel;
    }

    private Button loginButton, registerButton;
    private ProgressBar loadingBar;
    private TextView status;
    private EditText email,password;
    private TextView forget, privacy;
    private Login_SignUp_ViewModel viewModel;
    private Privacy_Fragment privacyFragment;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        init();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("TINTOK");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void init(){
        loginButton = getView().findViewById(R.id.sign_inButton);
        registerButton = getView().findViewById(R.id.sign_upButton);
        status = getView().findViewById(R.id.status);
        loadingBar = getView().findViewById(R.id.login_progressBar);
        email = getView().findViewById(R.id.emailInput);
        password = getView().findViewById(R.id.passInput);
        forget = getView().findViewById(R.id.forget_account_text);
        privacy = getView().findViewById(R.id.login_privacy_policy);

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
                getParentFragmentManager().beginTransaction().replace(R.id.fragment, Sign_up_Fragment.newInstance(viewModel)).addToBackStack("Login").commit();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment, Password_Reset_Fragment.newInstance(viewModel)).addToBackStack("Login").commit();
                //getParentFragmentManager().beginTransaction().replace(R.id.fragment, ForgetPasswordFragment.newInstance(viewModel)).addToBackStack("Login").commit();
            }
        });
        privacy.setOnClickListener(v -> {
            if(privacyFragment == null)
                privacyFragment = Privacy_Fragment.newInstance();
            privacyFragment.show(getActivity().getSupportFragmentManager(), "privacy_fragment");
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
        // Communication.getInstance().emitEvent("login", data);
        viewModel.loginRequest(email.getText().toString(), password.getText().toString(), this);
    }


    @Override
    public void requestSuccess() {
        loadingBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(getActivity(), Activity_InitData.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.animation_in, R.anim.animation_out);
        getActivity().finish();
    }

    @Override
    public void requestFail(String reason) {
        status.setVisibility(View.VISIBLE);
        status.setText("Signing in failed: "+ reason);
        loadingBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void connectionFail() {
        loadingBar.setVisibility(View.INVISIBLE);
        status.setVisibility(View.VISIBLE);
        status.setText(R.string.error_connection_failed);
    }
}