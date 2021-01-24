package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.DataLayer.DataRepository_UserSimple;
import com.example.tintok.Model.UserSimple;
import  com.example.tintok.R;
import  com.example.tintok.Model.Post;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;


public class PostAdapter extends BaseAdapter<Post,PostAdapter.ViewHolder> {



    public PostAdapter(Context context, ArrayList<Post> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
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

    @Override
    public void onViewAttachedToWindow(@NonNull PostAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        DataRepositoryController.getInstance().AddUserProfileChangeListener(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PostAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        DataRepositoryController.getInstance().RemoveUserProfileChangeListener(holder);

    }

    public class ViewHolder extends BaseViewHolder<Post> implements View.OnClickListener, DataRepository_UserSimple.OnUserProfileChangeListener {
        private TextView nComment, nLike, status, author;
        private ImageView iv, notificationIcon, profile;

        MaterialButton likeBtn, commentBtn;
        private String author_current_post;
        private String post_id;
        MaterialCardView cardView;
        GestureDetectorCompat mGestureDetector;
        public ViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
            super(itemView, mAdapter);
            this.nComment = itemView.findViewById(R.id.post_numberOfComment);
            this.nLike = itemView.findViewById(R.id.post_numberOfLike);
            this.status = itemView.findViewById(R.id.post_status);
            this.author = itemView.findViewById(R.id.post_name);
            this.iv = itemView.findViewById(R.id.post_image);
            this.cardView = itemView.findViewById(R.id.view);
            this.notificationIcon = itemView.findViewById(R.id.notification_icon);

            cardView.setBackgroundResource(R.drawable.post_background);

            /*cardView.setOnClickListener((v) ->{
                cardView.toggle();
                cardView.setBackgroundResource(R.drawable.post_background);
            });*/
            cardView.setOnTouchListener((v, event) -> {
                mGestureDetector.onTouchEvent(event);
                return true;
            });

            // set on click listener

            //set on click listener
            this.profile = itemView.findViewById(R.id.post_profile);
            this.author.setOnClickListener(this);
            this.profile.setOnClickListener(this);

            this.commentBtn = itemView.findViewById(R.id.comment);
            this.commentBtn.setOnClickListener(this);
        }


        String id;
        @Override
        public void bindData(Post itemData) {
            /*
            this.nComment.setText(itemData.getNumberOfComments()+"");
            this.nLike.setText(itemData.getNumberOfLikes()+"");
             */
            if(!itemData.isSubscription)
                notificationIcon.setImageResource(R.drawable.ic_offnoti);
            else
                notificationIcon.setImageResource(R.drawable.ic_onnoti);
            mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if(!itemData.isSubscription)
                        notificationIcon.setImageResource(R.drawable.ic_onnoti);
                    else
                        notificationIcon.setImageResource(R.drawable.ic_offnoti);
                    itemData.isSubscription = !itemData.isSubscription;

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    cardView.toggle();
                }
            });

            this.status.setText(itemData.getStatus());
            Glide.with(mAdapter.getContext()).load(itemData.getImage().url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(iv);
            this.id = itemData.getAuthor_id();
            UserSimple user = DataRepositoryController.getInstance().getUserSimpleProfile(this.id) ;
            if(user != null){
                Glide.with(mAdapter.getContext()).load(user.getProfilePic().url)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profile);
                this.author.setText(user.getUserName());
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.post_name || id == R.id.post_profile){
                mListener.onClickAvatar(v, getAdapterPosition());

            } else {

                mListener.onClickComment(v, getAdapterPosition());
            }
        }

        @Override
        public void onProfileChange(UserSimple user) {
            if(this.id.compareTo(user.getUserID()) == 0){
                Glide.with(mAdapter.getContext()).load(user.getProfilePic().url)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(profile);
                this.author.setText(user.getUserName());
            }
        }
    }

    onPostListener mListener;

    public void setListener(onPostListener listener){
        this.mListener = listener;
    }
    public interface onPostListener{
        public void onClickAvatar(View v, int position);
        public void onClickComment(View v, int position);
    }

}
