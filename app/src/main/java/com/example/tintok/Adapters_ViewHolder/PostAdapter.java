package com.android.testsocketclientio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.testsocketclientio.entity.Post;

import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> { // extends BaseAdapter
    private ArrayList<Post> posts;

    public PostAdapter(ArrayList<Post> posts){
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = this.posts.get(position);
        holder.nComment.setText(item.getNumberOfComments()+"");
        holder.nLike.setText(item.getNumberOfLikes()+"");
        holder.status.setText(item.getStatus());
        holder.author.setText(item.getAuthor());
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nComment, nLike, status, author;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nComment = itemView.findViewById(R.id.post_numberOfComment);
            this.nLike = itemView.findViewById(R.id.post_numberOfLike);
            this.status = itemView.findViewById(R.id.post_status);
            this.author = itemView.findViewById(R.id.post_name);
        }
    }
}
