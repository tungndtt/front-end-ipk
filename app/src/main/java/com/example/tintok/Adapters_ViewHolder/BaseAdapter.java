package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tintok.Model.*;
import com.example.tintok.R;

import java.util.ArrayList;

public abstract class BaseAdapter<T extends RecyclerViewModel, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {
    Context context;
    ArrayList<T> items;

    BaseAdapter(Context context, ArrayList<T> models){
        this.context = context;
        this.items = models;
    }


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bindData(items.get(position),position);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }
    public ArrayList<T> getItems() {
        return new ArrayList<>(items);
    }
    public void setItemAt(int position, T item){
        items.set(position, item);
        notifyItemChanged(position);
    }
<<<<<<< HEAD
    public T removeItem(int position) {
        T item = items.remove(position);
        notifyItemRemoved(position);
        return item;
=======
    public void removeItem(int position) {
        T item = items.remove(position);
        notifyItemRemoved(position);
>>>>>>> upstream/master
    }

    public void setItems(ArrayList<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

}
