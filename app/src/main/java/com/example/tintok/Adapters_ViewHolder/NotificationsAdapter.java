package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.Model.Notification;
import com.example.tintok.R;

import java.util.ArrayList;

public class NotificationsAdapter extends BaseAdapter<Notification, NotificationsAdapter.NotificationViewHolder> {
    public NotificationsAdapter(Context context, ArrayList<Notification> models) {
        super(context, models);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view, this);
    }

    public void setNotificationClickListener(onNotificationClickListener mListener) {
        this.mListener = mListener;
    }

    onNotificationClickListener mListener;
    public interface onNotificationClickListener{
        void onNotificationClick(int position);
    }
    public class NotificationViewHolder extends BaseViewHolder<Notification> implements View.OnClickListener  {

        ImageView profilePic, contentPic;
        TextView content, date;
        public NotificationViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
            super(itemView, mAdapter);
            profilePic = itemView.findViewById(R.id.profilePic);
            contentPic = itemView.findViewById(R.id.contentPic);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);

        }

        @Override
        public void bindData(Notification itemData) {
            this.content.setText(itemData.toTextViewString());
            this.date.setText(itemData.getDate().toString());
            Glide.with(mAdapter.getContext()).load(itemData.getUrl()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(contentPic);
            Glide.with(mAdapter.getContext()).load(itemData.getAuthor_profile_pic()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profilePic);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null)
                mListener.onNotificationClick(this.getAdapterPosition());
        }
    }
}
