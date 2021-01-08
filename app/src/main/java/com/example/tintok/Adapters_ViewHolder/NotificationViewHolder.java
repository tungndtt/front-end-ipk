package com.example.tintok.Adapters_ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tintok.Model.Notification;
import com.example.tintok.R;

public class NotificationViewHolder extends BaseViewHolder<Notification> {

    ImageView profilePic, contentPic;
    TextView content, date;
    public NotificationViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
        super(itemView, mAdapter);
        profilePic = itemView.findViewById(R.id.profilePic);
        contentPic = itemView.findViewById(R.id.contentPic);
        content = itemView.findViewById(R.id.content);
        date = itemView.findViewById(R.id.date);

    }

    @Override
    public void bindData(Notification itemData) {

    }
}
