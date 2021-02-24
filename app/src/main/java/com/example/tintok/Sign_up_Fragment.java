package com.example.tintok;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tintok.DataLayer.DataRepository_Interest;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Sign_up_Fragment extends Fragment implements Login_SignUp_ViewModel.requestListener {

    public static final String INTERESTS_SIGN_UP = "interests_sign_up";
    private Button registerButton;
    private ProgressBar loadingBar;
    private TextView status;
    private EditText name, email,password, retypepassword, day,month,year;
    private Login_SignUp_ViewModel viewModel;
    private RadioGroup mGenderGroup;
    private TextView mInterestTV;
    private String selectedInterest;
    private DialogFragment interestFragment;
    private View view;

    public Sign_up_Fragment(){

    }

    public static Sign_up_Fragment newInstance(Login_SignUp_ViewModel viewModel) {
        return new Sign_up_Fragment(viewModel);
    }

    public Sign_up_Fragment(Login_SignUp_ViewModel viewModel){
        this.viewModel = viewModel;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Sign-Up", "onCreate");
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("Sign-UP", "onCreateView");
        view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        registerButton =view.findViewById(R.id.sign_upButton);
        status = view.findViewById(R.id.status);
        loadingBar = view.findViewById(R.id.progressBar);
        name = view.findViewById(R.id.nameInput);
        email = view.findViewById(R.id.emailInput);
        password = view.findViewById(R.id.passInput);
        retypepassword = view.findViewById(R.id.passInputConfirm);
        day = view.findViewById(R.id.dayofbirth_date);
        month = view.findViewById(R.id.dayofbirth_month);
        year = view.findViewById(R.id.dayofbirth_year);
        mGenderGroup = view.findViewById(R.id.register_gender_group);
        mInterestTV = view.findViewById(R.id.register_interests_inputTV);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(viewModel == null){
            viewModel = new ViewModelProvider(this).get(Login_SignUp_ViewModel.class);
        }
        if(DataRepository_Interest.getInterestArrayList().size() == 0)
            DataRepository_Interest.initInterestArrayList();
        viewModel.setChosenInterests(new ArrayList<>());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onStart() {
        super.onStart();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Registration");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInterestTV.setText(selectedInterest);
        viewModel.getChosenInterests().observe(getViewLifecycleOwner(), integers -> {
            selectedInterest= "";
            for(int i=0; i<integers.size();i++)
                selectedInterest += DataRepository_Interest.interests[integers.get(i)] + " ";
            if(selectedInterest.isEmpty())
                selectedInterest = getResources().getString(R.string.interests_clickToChose);
            mInterestTV.setText(selectedInterest.toLowerCase());
            status.setVisibility(View.INVISIBLE);
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                HandleSignUp();
            }
        });

        mInterestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interestFragment == null)
                    interestFragment = Interests_SignUp_Fragment.newInstance();
                interestFragment.show(getChildFragmentManager(), INTERESTS_SIGN_UP);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void HandleSignUp(){
        Log.e("handle", "again");
        email.onEditorAction(EditorInfo.IME_ACTION_DONE);
        if(name.getText().toString().isEmpty()){
            setErrorStatus(R.string.error_username_empty);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
            setErrorStatus(R.string.error_email_invalid);
            return;
        }
        if(password.getText().toString().length() < 6){
            setErrorStatus(R.string.error_password_length);
            return;
        }

        int dayInt, monthInt, yearInt;
        if(day.getText().toString().isEmpty() || month.getText().toString().isEmpty()||year.getText().toString().isEmpty()){
            setErrorStatus(R.string.error_birthday_invalid);
            return;
        }
        try {
            dayInt = Integer.parseInt(day.getText().toString());
            monthInt = Integer.parseInt(month.getText().toString());
            yearInt = Integer.parseInt(year.getText().toString());
        }catch (Exception e){
            setErrorStatus(R.string.error_birthday_invalid);
            return;
        }
        if(year.getText().toString().length()!=4 || (dayInt <=0 || dayInt >31)
            || (monthInt<=0 || monthInt > 12)){
            setErrorStatus(R.string.error_birthday_invalid);
            return;
        }

        if(retypepassword.getText().toString().compareTo(password.getText().toString()) != 0){
           setErrorStatus(R.string.error_password_notMatching);
            return;
        }

        if(mGenderGroup.getCheckedRadioButtonId() == -1){
            setErrorStatus(R.string.error_gender_empty);
            return;
        }
        if(selectedInterest.equals(getResources().getString(R.string.interests_clickToChose))){
            setErrorStatus(R.string.error_interest_empty);
            return;
        }
        status.setVisibility(View.VISIBLE);
        status.setText(R.string.registration_signUp);
        status.setTextColor(Color.BLACK);
        loadingBar.setVisibility(View.VISIBLE);
        String birthday = dayInt+"/"+monthInt+"/"+yearInt;
        int gender = mGenderGroup.indexOfChild(getView().findViewById(mGenderGroup.getCheckedRadioButtonId())) + 1;
        ArrayList<Integer> chosenInterests = viewModel.getChosenInterests().getValue();
        this.viewModel.signUpRequest(name.getText().toString(), email.getText().toString(), birthday, password.getText().toString(), gender, chosenInterests, this);
    }



    private void setErrorStatus(int error){
        status.setVisibility(View.VISIBLE);
        status.setText(error);
        status.setTextColor(Color.RED);
    }


    @Override
    public void requestSuccess() {
        loadingBar.setVisibility(View.INVISIBLE);
        Snackbar.make(getView(), R.string.registration_email_sent, Snackbar.LENGTH_LONG);
        //TODO:
        //getActivity().getSupportFragmentManager().popBackStack();//.beginTransaction().replace(R.id.fragment, )
        getActivity().onBackPressed();
    }

    @Override
    public void requestFail(String reason) {
       // setErrorStatus(R.string.error_registration_failed);
        status.setText(reason);
        loadingBar.setVisibility(View.INVISIBLE);
        //TODO: reset email textfield?
    }

    @Override
    public void connectionFail() {
        status.setVisibility(View.VISIBLE);
        status.setText(R.string.error_connection_failed);
        loadingBar.setVisibility(View.INVISIBLE);
    }
}