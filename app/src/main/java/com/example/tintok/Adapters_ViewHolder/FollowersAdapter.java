package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.Model.UserSimple;
import com.example.tintok.R;

import java.util.ArrayList;

public class FollowersAdapter extends BaseAdapter<UserSimple, FollowersAdapter.ViewHolder> {


    public FollowersAdapter(Context context, ArrayList<UserSimple> models) {
        super(context, models);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followers, parent, false);
        return new ViewHolder(view, this);

    }

    public class ViewHolder extends BaseViewHolder<UserSimple> {

        TextView mFollowerName;
        AppCompatImageView mFollowerProfilePic;

        public ViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
            super(itemView, mAdapter);
            mFollowerName = itemView.findViewById(R.id.item_follower_username);
            mFollowerProfilePic = itemView.findViewById(R.id.item_follower_profilePic);
        }

        @Override
        public void bindData(UserSimple itemData) {
            if(itemData != null){
                mFollowerName.setText(itemData.getUserName());
                Glide.with(mAdapter.getContext()).load(itemData.getProfilePic().url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(mFollowerProfilePic);
            }


        }
    }
}
