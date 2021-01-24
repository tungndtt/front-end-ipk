package com.example.tintok;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

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

import org.json.JSONException;
import org.json.JSONObject;

public class Sign_up_Fragment extends Fragment implements Login_SignUp_ViewModel.requestListener {

    private SignUpViewModel mViewModel;
    private Button registerButton, backBtn;
    private ProgressBar loadingBar;
    private TextView status;
    private EditText name, email,password, retypepassword, day,month,year;
    private Login_SignUp_ViewModel viewModel;

    public static Sign_up_Fragment newInstance(Login_SignUp_ViewModel viewModel) {
        return new Sign_up_Fragment(viewModel);
    }

    public Sign_up_Fragment(Login_SignUp_ViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        // TODO: Use the ViewModel
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onStart() {
        super.onStart();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void init(){
        registerButton = getView().findViewById(R.id.sign_upButton);
        backBtn = getView().findViewById(R.id.back_to_login_button);
        status = getView().findViewById(R.id.status);
        loadingBar = getView().findViewById(R.id.progressBar);
        name = getView().findViewById(R.id.nameInput);
        email = getView().findViewById(R.id.emailInput);
        password = getView().findViewById(R.id.passInput);
        retypepassword = getView().findViewById(R.id.passInputConfirm);
        day = getView().findViewById(R.id.dayofbirth_date);
        month = getView().findViewById(R.id.dayofbirth_month);
        year = getView().findViewById(R.id.dayofbirth_year);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
               HandleSignUp();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void HandleSignUp(){
        email.onEditorAction(EditorInfo.IME_ACTION_DONE);
        if(name.getText().toString().isEmpty()){
            status.setVisibility(View.VISIBLE);
            status.setText("Name cant be blank");
            return;
        }
        if(email.getText().toString().length() <= 5 || password.getText().toString().length()<=5){
            status.setVisibility(View.VISIBLE);
            status.setText("Invalid Email or Password");
            return;
        }
        int dayInt, monthInt, yearInt;
        if(day.getText().toString().isEmpty() || month.getText().toString().isEmpty()||year.getText().toString().isEmpty()){
            status.setVisibility(View.VISIBLE);
            status.setText("Invalid Date of Birth");
            return;
        }
        try {
            dayInt = Integer.parseInt(day.getText().toString());
            monthInt = Integer.parseInt(month.getText().toString());
            yearInt = Integer.parseInt(year.getText().toString());
        }catch (Exception e){
            status.setVisibility(View.VISIBLE);
            status.setText("Invalid Date of Birth");
            return;
        }
        if(year.getText().toString().length()!=4 || (dayInt <0 || dayInt >31)
            || (monthInt<=0 || monthInt >=12)){
            status.setVisibility(View.VISIBLE);
            status.setText("Invalid Date of Birth");
            return;
        }

        if(retypepassword.getText().toString().compareTo(password.getText().toString()) != 0){
            status.setVisibility(View.VISIBLE);
            status.setText("Passwords dont match");
            return;
        }

        status.setVisibility(View.VISIBLE);
        status.setText("Signing un...");
        loadingBar.setVisibility(View.VISIBLE);
        JsonObject data = new JsonObject();
        data.addProperty("email", email.getText().toString());
        data.addProperty("password", password.getText().toString());
        data.addProperty("day_ofBirth", dayInt);
        data.addProperty("month_ofBirth", monthInt);
        data.addProperty("year_ofBirth", yearInt);
        data.addProperty("name", name.getText().toString());
        String birthday = dayInt+"/"+monthInt+"/"+yearInt;
        this.viewModel.signUpRequest(name.getText().toString(), email.getText().toString(), birthday, password.getText().toString(), this);
        /*
        communication.LoginRequest(data, new RestAPI_Entity.RestApiListener(){

            @Override
            public void onSuccess(RestAPI_Entity.AbstractResponseEntity response) {
                RestAPI_Entity.StatusResponseEntity res = (RestAPI_Entity.StatusResponseEntity)response;
                if(((RestAPI_Entity.StatusResponseEntity) res).status ){

                }
                else{

                }
            }

            @Override
            public void onFailure() {
                status.setVisibility(View.VISIBLE);
                status.setText("Signing up failed");
            }
        });
         */
    }


    @Override
    public void requestSuccess() {
        status.setVisibility(View.VISIBLE);
        status.setText("Signing up sucessfull. Please press return to Login");
    }

    @Override
    public void requestFail(String reason) {
        status.setVisibility(View.VISIBLE);
        status.setText("Signing up failed: "+ reason);
        loadingBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void connectionFail() {
        status.setVisibility(View.VISIBLE);
        status.setText("Signing up failed");
    }
}