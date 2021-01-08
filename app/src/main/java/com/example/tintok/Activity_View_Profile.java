package com.example.tintok;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import com.example.tintok.Model.UserProfile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.widget.Toast;

public class Activity_View_Profile extends AppCompatActivity {

    private Fragment profile_view;

    private Activity_ViewProfile_ViewModel viewModel;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        this.viewModel = new ViewModelProvider(this).get(Activity_ViewProfile_ViewModel.class);
        this.viewModel.getProfile().observe(this, new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile user) {
                if(user != null)
                    createProfileFragment(user);
            }
        });
        //this.viewModel.setActivity(this);
        String author_id = getIntent().getStringExtra("author_id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_profile_activity_toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            toolbar.setTitle("Profile");
        }
        this.viewModel.getUserProfile(author_id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createProfileFragment(UserProfile user){
        this.profile_view = MainPages_MyProfile_Fragment.getInstance(user);
        getSupportFragmentManager().beginTransaction().replace(R.id.other_profile_display_fragment, this.profile_view).commit();
    }

}