package com.example.tintok.Adapters_ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.ChatRoom;
import com.example.tintok.Model.MessageEntity;
import com.example.tintok.Model.UserSimple;
import com.example.tintok.R;

public class ChatroomViewHolder extends BaseViewHolder<ChatRoom> implements View.OnClickListener {
    ImageView profilepic;
    TextView name, date, lastmsg;
    ChatroomAdapter.onChatRoomClickListener mListener;
    public ChatroomViewHolder(@NonNull View itemView, BaseAdapter mAdapter, ChatroomAdapter.onChatRoomClickListener mListener) {
        super(itemView, mAdapter);
        this.mListener = mListener;
        profilepic = itemView.findViewById(R.id.profilePic);
        name = itemView.findViewById(R.id.profilename);
        date = itemView.findViewById(R.id.lastModifiedDate);
        lastmsg = itemView.findViewById(R.id.newestMsg);

        itemView.setOnClickListener(this);



    }

    @Override
    public void bindData(ChatRoom itemData) {
        DataRepositoryController data = DataRepositoryController.getInstance();
        String thisUserID = data.getUser().getUserID();
        UserSimple another = null;
/*        for(String id : itemData.getMemberIDs()){
            if(id.compareTo(thisUserID)!= 0){
                another = data.getUserSimpleProfile(id);
                break;
            }
        }*/
        if(another!=null){
            Glide.with(mAdapter.getContext()).load(another.getProfilePic().url).diskCacheStrategy(DiskCacheStrategy.DATA).into(profilepic);
            name.setText(another.getUserName());
            String dateModified = itemData.getMessageEntities().getValue().get(itemData.getMessageEntities().getValue().size()-1).getDatePosted().toString();
            date.setText(dateModified);
            MessageEntity mess = itemData.getMessageEntities().getValue().get(itemData.getMessageEntities().getValue().size()-1);
            if(mess.getBuilder() == null)
                lastmsg.setText("IMG");
            else
                lastmsg.setText(mess.getBuilder());
        }
    }

    @Override
    public void onClick(View v) {
        mListener.OnClick(getAdapterPosition());
    }
}
