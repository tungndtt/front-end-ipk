package com.example.tintok.Adapters_ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.tintok.Model.User;
import com.example.tintok.R;


public class PeopleBrowsingViewHolder extends BaseViewHolder<User> {
    TextView name, description;
    ImageView imageView;
    public ImageView likeImg, dislikeImg;
    ScaleGestureDetector gestureDetector;
    Matrix mMatrix = new Matrix();
    float scale = 1f;
    public PeopleBrowsingViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
        super(itemView, mAdapter);
        name = itemView.findViewById(R.id.profilename);
        description = itemView.findViewById(R.id.profileDes);
        imageView = itemView.findViewById(R.id.profileimage);
        likeImg = itemView.findViewById(R.id.likeImg);
        dislikeImg = itemView.findViewById(R.id.dislikeImg);
        gestureDetector = new ScaleGestureDetector(itemView.getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener(){
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale = scale*detector.getScaleFactor();
                scale = Math.max(0.5f, Math.min(scale, 2.0f));
                mMatrix.setScale(scale,scale, detector.getFocusX(), detector.getFocusY());
                imageView.setImageMatrix(mMatrix);
                return true;
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                Log.e("inside Touch", "at "+ mAdapter.getItemCount());
                return true;

            }
        });

    }

    //like = true;
    //dislike = false;
    @Override
    public void bindData(User itemData, int position) {
        Log.e("called","why not");
        name.setText(itemData.getUserName());
        description.setText(itemData.getDescription());
        if(position %2 == 0)
            imageView.setImageResource(android.R.drawable.screen_background_dark);
        else
            imageView.setImageResource(android.R.drawable.screen_background_dark);

    }
}
