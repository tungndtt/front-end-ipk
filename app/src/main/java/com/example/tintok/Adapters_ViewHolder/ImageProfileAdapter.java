package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.Model.Post;

import java.util.ArrayList;
import com.example.tintok.R;

public class ImageProfileAdapter extends BaseAdapter<Post, ImageProfileAdapter.ViewHolder> {

    public ImageProfileAdapter(Context context, ArrayList<Post> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_item,parent,false);
        return new ViewHolder(view, this);
    }

    @Override
    public void addItem(Post item) {
        this.items.add(0,item);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class ViewHolder extends BaseViewHolder<Post>{
        private ImageView img;
        public ViewHolder(@NonNull View itemView, BaseAdapter baseAdapter) {
            super(itemView, baseAdapter);
            this.img = itemView.findViewById(R.id.image_on_profile_page);
        }

        @Override
        public void bindData(Post itemData) {
            Glide.with(mAdapter.getContext()).load(itemData.getImage().url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(img);
        }
    }
}