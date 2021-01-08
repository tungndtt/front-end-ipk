package com.example.tintok.Adapters_ViewHolder;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.MessageEntity;
import com.example.tintok.R;
import com.example.tintok.R.color;

public class MessageViewHolder extends BaseViewHolder<MessageEntity> {

    CardView view;
    LinearLayout messageLayout;
    LinearLayoutCompat leftSide, rightSide;
    ImageView leftProfilePic, rightProfilePic, leftPic, rightPic;
    TextView leftContent, leftDate, rightContent, rightDate;
    public MessageViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
        super(itemView, mAdapter);
        messageLayout = itemView.findViewById(R.id.messageLayout);
        view = itemView.findViewById(R.id.view);

        leftSide = itemView.findViewById(R.id.leftSide);
        leftProfilePic = itemView.findViewById(R.id.leftprofilePic);
        leftPic = itemView.findViewById(R.id.leftImg);
        leftContent = itemView.findViewById(R.id.leftcontent);
        leftDate = itemView.findViewById(R.id.leftdate);

        rightSide = itemView.findViewById(R.id.rightSide);
        rightProfilePic = itemView.findViewById(R.id.rightprofilePic);
        rightPic = itemView.findViewById(R.id.rightImg);
        rightContent = itemView.findViewById(R.id.rightcontent);
        rightDate = itemView.findViewById(R.id.rightdate);

    }

    @Override
    public void bindData(MessageEntity itemData) {
        playAppearAnimation(getAdapterPosition());

        if(!itemData.getAuthorID().equals(DataRepositoryController.getInstance().getUser().getUserID())){
            view.setBackgroundColor(Color.WHITE);

            leftSide.setVisibility(View.VISIBLE);
            messageLayout.setGravity(Gravity.LEFT);
            rightSide.setVisibility(View.GONE);

            if(itemData.getMedia() == null){
                leftContent.setVisibility(View.VISIBLE);
                leftPic.setVisibility(View.GONE);
                leftContent.setText(itemData.getBuilder());

            }
            else{
                leftContent.setVisibility(View.GONE);
                leftPic.setVisibility(View.VISIBLE);
                MediaEntity m = itemData.getMedia();
                if(m.uri != null)
                    Glide.with(mAdapter.getContext()).load(m.uri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(leftPic);
                else if(m.url != null)
                    Glide.with(mAdapter.getContext()).load(m.url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(leftPic);
                else if(m.bitmap != null)
                    Glide.with(mAdapter.getContext()).load(m.bitmap).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(leftPic);
            }
            leftDate.setText(itemData.getDatePosted().toString());
        }

        else{
            view.setBackgroundResource(R.drawable.message_background);

            leftSide.setVisibility(View.GONE);
            messageLayout.setGravity(Gravity.RIGHT);
            rightSide.setVisibility(View.VISIBLE);

            if(itemData.getMedia() == null){
                rightContent.setVisibility(View.VISIBLE);
                rightPic.setVisibility(View.GONE);
                rightContent.setText(itemData.getBuilder());
            }
            else{
                rightContent.setVisibility(View.GONE);
                rightPic.setVisibility(View.VISIBLE);
                MediaEntity m = itemData.getMedia();
                if(m.uri != null)
                    Glide.with(mAdapter.getContext()).load(m.uri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(rightPic);
                else if(m.url != null)
                    Glide.with(mAdapter.getContext()).load(m.url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(rightPic);
                else if(m.bitmap != null)
                    Glide.with(mAdapter.getContext()).load(m.bitmap).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(rightPic);
            }
            rightDate.setText(itemData.getDatePosted().toString());
        }
    }

    private void playAppearAnimation(int i){
        if(i <= mAdapter.lastIndexAnimated)
            return;
        mAdapter.lastIndexAnimated = i;
        itemView.setAlpha(0);
        int duration = 500;
        int startDelay = (i%2 +1)*200;
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(this.itemView, "alpha" ,0f, 0.5f, 1f).setDuration(duration);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(this.itemView, "scaleX",0f, 0.5f, 1f).setDuration(duration);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(this.itemView, "scaleY",0f, 0.5f, 1f).setDuration(duration);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(startDelay);
        animatorSet.playTogether(animatorAlpha, animatorScaleX, animatorScaleY);
        animatorSet.start();


    }

}
