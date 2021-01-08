package com.example.tintok;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tintok.Adapters_ViewHolder.CommentAdapter;
import com.example.tintok.Adapters_ViewHolder.PostAdapter;
import com.example.tintok.Model.Comment;

import java.util.ArrayList;
import java.util.List;

public class Activity_Comment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton button;
    private EditText comment;

    private Activity_Comment_ViewModel viewModel;
    private CommentAdapter adapter;

    private String post_id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        this.viewModel = new ViewModelProvider(this).get(Activity_Comment_ViewModel.class);

        Intent intent = getIntent();
        this.post_id = intent.getStringExtra("post_id");
        this.viewModel.getComments(post_id);

        this.adapter = new CommentAdapter(this, new ArrayList<Comment>());
        this.recyclerView = findViewById(R.id.list_comment);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.viewModel.getListComments().observe(this, new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                adapter.setItems(comments);
                recyclerView.smoothScrollToPosition(0);
            }
        });

        this.comment = findViewById(R.id.comment_field);
        this.button = findViewById(R.id.send_comment);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmt = comment.getText().toString();
                if(cmt.length() > 0)
                    viewModel.sendComment(cmt, post_id);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.viewModel.leavePost(this.post_id);
        Log.i("Comment activity", "Destroyed!");
    }

}