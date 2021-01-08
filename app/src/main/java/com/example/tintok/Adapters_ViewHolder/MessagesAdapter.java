package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.tintok.Model.MessageEntity;
import com.example.tintok.R;

import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter<MessageEntity, MessageViewHolder > {


    public MessagesAdapter(Context context, ArrayList<MessageEntity> models) {
        super(context, models);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view, this);
    }
}
