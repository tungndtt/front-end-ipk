package com.example.tintok.Adapters_ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tintok.Model.User;
import com.example.tintok.R;

import java.util.ArrayList;


public class PeopleBrowsingAdapter extends BaseAdapter<User, PeopleBrowsingViewHolder> {
    public PeopleBrowsingAdapter(Context context, ArrayList<User> models) {
        super(context, models);

    }

    @NonNull
    @Override
    public PeopleBrowsingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainpages__peoplebrowsing__item, parent, false);
        return new PeopleBrowsingViewHolder(view, this);
    }

}
