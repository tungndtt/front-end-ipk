package com.example.tintok;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tintok.Communication.Communication;

public class AppMainPagesActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_pages);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        Communication.getInstance().initScoket();
    }
}