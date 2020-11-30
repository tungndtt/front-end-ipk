package com.example.tintok.Adapters_ViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tintok.Model.RecyclerViewModel;

public abstract class BaseViewHolder<T extends RecyclerViewModel> extends RecyclerView.ViewHolder {
    BaseAdapter mAdapter;
<<<<<<< HEAD
    BaseViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
=======
    public BaseViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
>>>>>>> upstream/master
        super(itemView);
        this.mAdapter = mAdapter;
    }

    public abstract void bindData(T itemData, int position);
}
