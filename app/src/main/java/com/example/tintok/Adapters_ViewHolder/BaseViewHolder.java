package com.example.tintok.Adapters_ViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tintok.Model.RecyclerViewModel;

public abstract class BaseViewHolder<T extends RecyclerViewModel> extends RecyclerView.ViewHolder {
    BaseAdapter<T> mAapter;
    public BaseViewHolder(@NonNull View itemView, BaseAdapter<T> mAdapter) {
        super(itemView);
        this.mAapter = mAdapter;
    }

    public abstract void bindData(T itemData, int position);
}
