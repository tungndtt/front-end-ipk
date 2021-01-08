package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tintok.R;
import com.example.tintok.Model.Comment;

import java.util.ArrayList;


public class CommentAdapter extends BaseAdapter<Comment, CommentAdapter.ViewHolder>{
    public CommentAdapter(Context mContext, ArrayList<Comment> items) {
        super(mContext, items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void addItem(Comment item) {
        this.items.add(0,item);
        notifyItemInserted(0);
    }

    public static class ViewHolder extends BaseViewHolder<Comment> {

        private TextView name, status;

        public ViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
            super(itemView, mAdapter);
            this.name = itemView.findViewById(R.id.comment_name);
            this.status = itemView.findViewById(R.id.comment_content);
        }

        @Override
        public void bindData(Comment itemData) {
            this.name.setText(itemData.getAuthorID());
            this.status.setText(itemData.getBuilder());
        }
    }
}
