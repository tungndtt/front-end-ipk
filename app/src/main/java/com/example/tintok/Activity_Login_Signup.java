package com.example.tintok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.tintok.Communication.Communication;


public class Activity_Login_Signup extends AppCompatActivity {
    public final String ID = "Login" ;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        Communication.getInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, Login_Fragment.newInstance()).commit();

    }

    public boolean isOnline() {
        boolean var = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( cm.getActiveNetworkInfo() != null ) {
            var = true;
        }
        return var;
    }

}