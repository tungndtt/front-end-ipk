package com.android.testsocketclientio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.testsocketclientio.entity.ProfileImage;

import java.util.ArrayList;


public class ImagePageAdapter extends RecyclerView.Adapter<ImagePageAdapter.ViewHolder> {
    private ArrayList<ProfileImage> images;

    ImagePageAdapter(ArrayList<ProfileImage> images){
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_page_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageResource(images.get(position).getImgSrc());
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img = itemView.findViewById(R.id.image_on_profile_page);
        }
    }
}
