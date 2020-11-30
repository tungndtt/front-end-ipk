package com.example.tintok.Adapters_ViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tintok.Model.RecyclerViewModel;

public abstract class BaseViewHolder<T extends RecyclerViewModel> extends RecyclerView.ViewHolder {
    BaseAdapter mAdapter;
    BaseViewHolder(@NonNull View itemView, BaseAdapter mAdapter) {
        super(itemView);
        this.mAdapter = mAdapter;
    }

    public abstract void bindData(T itemData, int position);
}
