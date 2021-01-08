package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.tintok.Model.Notification;
import com.example.tintok.R;

import java.util.ArrayList;

public class NotificationsAdapter extends BaseAdapter<Notification, NotificationViewHolder> {
    public NotificationsAdapter(Context context, ArrayList<Notification> models) {
        super(context, models);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view, this);
    }
}
