package com.example.tintok;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;


public class Password_Reset_Fragment extends Fragment implements Login_SignUp_ViewModel.requestListener {

    private View view;
    private EditText mCurrentPwEditText, mNewPwEditText, mRetypePWEditText;
    private MaterialButton mSaveBtn, mCancelBtn, mBackBtn;
    private TextView mEmailError, mNewPwError, mRetypePwError, mTitleTV;
    private ProgressBar mProgressBar;
    private Login_SignUp_ViewModel viewModel;

    public Password_Reset_Fragment(Login_SignUp_ViewModel viewModel){
        this.viewModel = viewModel;

    }

    public static Password_Reset_Fragment newInstance(Login_SignUp_ViewModel viewModel){
        Password_Reset_Fragment fragment = new Password_Reset_Fragment(viewModel);
        return fragment;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reset_password_fragment, container, false);
        initViews();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(viewModel == null)
            viewModel = new ViewModelProvider(this).get(Login_SignUp_ViewModel.class);
        mCurrentPwEditText.setHint("Email address");
        mCurrentPwEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        //TODO: change ICON
        mTitleTV.setText("Reset Password");




        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEmailError.setVisibility(View.INVISIBLE);
                mNewPwError.setVisibility(View.INVISIBLE);
                mRetypePwError.setVisibility(View.INVISIBLE);

                handleInput();
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEmailError.setVisibility(View.INVISIBLE);
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
                getParentFragmentManager().popBackStack();


            }
        });

    }


    private void initViews(){
        mCurrentPwEditText = view.findViewById(R.id.reset_pw_oldPW);
        mNewPwEditText = view.findViewById(R.id.reset_pw_newPW);
        mRetypePWEditText = view.findViewById(R.id.reset_pw_retypePW);
        mCancelBtn = view.findViewById(R.id.reset_pw_cancelBtn);
        mSaveBtn = view.findViewById(R.id.reset_pw_saveBtn);
        mProgressBar = view.findViewById(R.id.reset_pw_progressBar);
        mEmailError = view.findViewById(R.id.reset_pw_oldPWerror);
        mNewPwError = view.findViewById(R.id.reset_pw_newPWerror);
        mRetypePwError = view.findViewById(R.id.reset_pw_retypePWerror);
        mBackBtn = view.findViewById(R.id.reset_pw_backBtn);
        mTitleTV = view.findViewById(R.id.reset_pw_topTV);


    }
    private void handleInput() {

        String email = mCurrentPwEditText.getText().toString();
        String newPW = mNewPwEditText.getText().toString();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailError.setText(getResources().getString(R.string.error_email_invalid));
            mEmailError.setVisibility(View.VISIBLE);
            return;
        }
        if(newPW.length() < 6){
            mNewPwError.setText(getResources().getString(R.string.error_password_length));
            mNewPwError.setVisibility(View.VISIBLE);
            return;
        }
        if(!mRetypePWEditText.getText().toString().equals(newPW)){
            mRetypePwError.setText(getResources().getString(R.string.error_password_notMatching));
            mRetypePwError.setVisibility(View.VISIBLE);
            return;
        }

        viewModel.resetPassword(email, newPW, this);

    }


    @Override
    public void requestSuccess() {

        Snackbar.make(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment).getView(), R.string.confirmation_email_sent, Snackbar.LENGTH_LONG).show();

        getParentFragmentManager().popBackStack();
    }

    @Override
    public void requestFail(String reason) {
        mEmailError.setText(reason);
        mEmailError.setVisibility(View.VISIBLE);

    }

    @Override
    public void connectionFail() {
        //snackBarShow("Some errors occur in reset password!");
        Snackbar.make(getView(), "Some errors occur in reset password!", Snackbar.LENGTH_LONG);
    }
}
