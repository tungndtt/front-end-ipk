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

import org.json.JSONException;
import org.json.JSONObject;

public class Sign_up_Fragment extends Fragment {

    private SignUpViewModel mViewModel;
    private Button registerButton;
    private ProgressBar loadingBar;
    private TextView status;
    private EditText name, email,password, retypepassword, day,month,year;
    Communication communication;

    public static Sign_up_Fragment newInstance() {
        return new Sign_up_Fragment();
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
        communication = Communication.getInstance();
        registerButton = getView().findViewById(R.id.sign_upButton);
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
        JSONObject data = new JSONObject();
        try {
            data.put("email", email.getText().toString());
            data.put("password", password.getText().toString());
            data.put("day_ofBirth", dayInt);
            data.put("month_ofBirth", monthInt);
            data.put("year_ofBirth", yearInt);
            data.put("name", name.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        communication.LoginRequest(data, new RestAPI_Entity.RestApiListener(){

            @Override
            public void onSuccess(RestAPI_Entity.AbstractResponseEntity response) {
                RestAPI_Entity.StatusResponseEntity res = (RestAPI_Entity.StatusResponseEntity)response;
                if(((RestAPI_Entity.StatusResponseEntity) res).status ){
                    status.setVisibility(View.VISIBLE);
                    status.setText("Signing up sucessfull. Please press return to Login");
                }
                else{
                    status.setVisibility(View.VISIBLE);
                    status.setText("Signing up failed: "+ res.reason);
                    loadingBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure() {
                status.setVisibility(View.VISIBLE);
                status.setText("Signing up failed");
            }
        });
    }
}