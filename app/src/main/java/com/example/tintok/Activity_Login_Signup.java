package com.example.tintok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.example.tintok.Communication.Communication;
import com.google.android.material.appbar.MaterialToolbar;


public class Activity_Login_Signup extends AppCompatActivity {
    public final String ID = "Login" ;
    private Login_SignUp_ViewModel viewModel;
    MaterialToolbar toolbar;

    public Activity_Login_Signup() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity_Login_Signup", "onCreate");
        setContentView(R.layout.activity_login_signup);
        toolbar =  findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        Communication.getInstance();
        this.viewModel = new ViewModelProvider(this).get(Login_SignUp_ViewModel.class);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, Login_Fragment.newInstance(viewModel)).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.e("onBackPressed", "true");
        onBackPressed();
        return false;
    }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("onBackPressed", "YES");
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

     */


    public boolean isOnline() {
        boolean var = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( cm.getActiveNetworkInfo() != null ) {
            var = true;
        }
        return var;
    }

}