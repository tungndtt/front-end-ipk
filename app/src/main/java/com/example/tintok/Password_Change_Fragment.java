package com.example.tintok;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tintok.CustomView.MyDialogFragment;
import com.example.tintok.DataLayer.ResponseEvent;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class Password_Change_Fragment extends MyDialogFragment {

    private View view;
    private EditText mCurrentPwEditText, mNewPwEditText, mRetypePWEditText;
    private MaterialButton mSaveBtn, mCancelBtn, mBackBtn;
    private TextView mCurrentPwError, mNewPwError, mRetypePwError;
    private ProgressBar mProgressBar;
    MainPages_MyProfile_ViewModel mViewModel;

    public Password_Change_Fragment(){

    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reset_password_fragment, container, false);
        initViews();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mViewModel == null)
            mViewModel = new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);

        mViewModel.getNetworkResponse().observe(this, responseEvent -> {  // = null or string
          //  Log.e("RE", responseEvent.getContentIfNotHandled());
            if(responseEvent.getType() == ResponseEvent.Type.PASSWORD){
                String response = responseEvent.getContentIfNotHandled(); // null or String
                if(response != null && response.equals("Created")){
                    Log.e("pw", "updated");
                   // Snackbar.make(getActivity().getSupportFragmentManager().findFragmentById(.).getView(), "Updated", Snackbar.LENGTH_LONG);
                    //getActivity().getSupportFragmentManager().findFragmentById(R.id.mainPageContent).getView();
                    Snackbar.make(getView(), "Updated", Snackbar.LENGTH_LONG).show();

                }
                if(response != null && response.equals("Forbidden")){
                    Log.e("pw", "wrong");
                    mCurrentPwError.setVisibility(View.VISIBLE);
                }

            }


        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentPwError.setVisibility(View.INVISIBLE);
                mNewPwError.setVisibility(View.INVISIBLE);
                mRetypePwError.setVisibility(View.INVISIBLE);

                handleInput();
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentPwError.setVisibility(View.INVISIBLE);
                mNewPwError.setVisibility(View.INVISIBLE);
                mRetypePwError.setVisibility(View.INVISIBLE);
                mCurrentPwEditText.setText("");
                mNewPwEditText.setText("");
                mRetypePWEditText.setText("");

            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();


            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

    }


    private void initViews(){
        mCurrentPwEditText = view.findViewById(R.id.reset_pw_oldPW);
        mNewPwEditText = view.findViewById(R.id.reset_pw_newPW);
        mRetypePWEditText = view.findViewById(R.id.reset_pw_retypePW);
        mCancelBtn = view.findViewById(R.id.reset_pw_cancelBtn);
        mSaveBtn = view.findViewById(R.id.reset_pw_saveBtn);
        mProgressBar = view.findViewById(R.id.reset_pw_progressBar);
        mCurrentPwError = view.findViewById(R.id.reset_pw_oldPWerror);
        mNewPwError = view.findViewById(R.id.reset_pw_newPWerror);
        mRetypePwError = view.findViewById(R.id.reset_pw_retypePWerror);
        mBackBtn = view.findViewById(R.id.reset_pw_backBtn);


    }
    private void handleInput() {

        String currentPW = mCurrentPwEditText.getText().toString();
        String newPW = mNewPwEditText.getText().toString();
        if(currentPW.isEmpty()){
            mCurrentPwError.setText(getResources().getString(R.string.error_password_empty));
            mCurrentPwError.setVisibility(View.VISIBLE);
            return;
        }
        if(newPW.length() <= 5){
            mNewPwError.setText(getResources().getString(R.string.error_password_length));
            mNewPwError.setVisibility(View.VISIBLE);
            return;
        }
        if(mCurrentPwEditText.getText().toString().equals(newPW)){
            mNewPwError.setText("Your passwords have to be different");
            mNewPwError.setVisibility(View.VISIBLE);
            return;
        }
        if(!mRetypePWEditText.getText().toString().equals(newPW)){
            mRetypePwError.setText(getResources().getString(R.string.error_password_notMatching));
            mRetypePwError.setVisibility(View.VISIBLE);
            return;
        }

        List<String> passwords = Arrays.asList(currentPW, newPW);
        //hideKeyboard();
        mViewModel.changePassword(passwords);

    }


}
