package com.example.tintok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.strictmode.CleartextNetworkViolation;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.tintok.Communication.Communication;

import java.net.URI;
import java.net.URISyntaxException;


import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;


public class MainActivity extends AppCompatActivity {
    public final String ID = "Login" ;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*setContentView(R.layout.web_view);
        webView = findViewById(R.id.webview);
        webView.loadUrl("http://192.168.112.65:3000/confirmation/waeawe");
        webView.setWebViewClient(new WebViewClient());*/


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