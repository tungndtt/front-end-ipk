package com.example.tintok;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ForgetPasswordFragment extends Fragment {
    private EditText email,password;
    private Login_SignUp_ViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public ForgetPasswordFragment(Login_SignUp_ViewModel viewModel){
        this.viewModel = viewModel;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ForgetPasswordFragment newInstance(Login_SignUp_ViewModel viewModel) {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment(viewModel);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        this.email = view.findViewById(R.id.forget_password_email);
        this.password = view.findViewById(R.id.forget_password_password);

        Button back = view.findViewById(R.id.for_password_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        Button submit = view.findViewById(R.id.forget_password_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
              //  viewModel.resetPassword(email.getText().toString(), password.getText().toString());
                getParentFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
