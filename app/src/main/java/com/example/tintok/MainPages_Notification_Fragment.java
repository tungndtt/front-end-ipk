package com.example.tintok;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tintok.Adapters_ViewHolder.ChatroomAdapter;
import com.example.tintok.Adapters_ViewHolder.NotificationsAdapter;
import com.example.tintok.CustomView.NoSpaceRecyclerViewDecoration;
import com.example.tintok.DataLayer.DataRepository_Notificaitons;
import com.example.tintok.Model.Notification;
import com.example.tintok.Model.Post;

import java.util.ArrayList;

public class MainPages_Notification_Fragment extends Fragment implements NotificationsAdapter.onNotificationClickListener {

    private MainPages_Notification_ViewModel mViewModel;

    RecyclerView notifications;
    NotificationsAdapter adapter;

    public static MainPages_Notification_Fragment newInstance() {
        return new MainPages_Notification_Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_pages__notification__fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainPages_Notification_ViewModel.class);
        // TODO: Use the ViewModel
        init();
        mViewModel.getNotifications().observe(this.getViewLifecycleOwner(), notifications -> {
            adapter.setItems(notifications);
        });
    }

    void init(){
        notifications = getView().findViewById(R.id.notificationview);
        this.adapter = new NotificationsAdapter(this.getContext(), new ArrayList<>());
        notifications.setAdapter(adapter);
        adapter.setNotificationClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration= new NoSpaceRecyclerViewDecoration();
        notifications.setLayoutManager(manager);
        notifications.addItemDecoration(decoration);

    }



    private void NotifyActivityOnNotiClicked(Notification noti){
        try{
            Activity_AppMainPages a = (Activity_AppMainPages)getActivity();
            a.OnNotificationClicked(noti);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(this.getContext(), Activity_Comment.class);
        Notification notification =  adapter.getItems().get(position);
        intent.putExtra("post_id", notification.getPostID());
        startActivity(intent);
        NotifyActivityOnNotiClicked(notification);
    }

    @Override
    public void onProfileClick(int position) {
        Intent intent = new Intent(this.getContext(), Activity_ViewProfile.class);
        intent.putExtra("author_id", adapter.getItems().get(position).getAuthor_id());
        startActivity(intent);
    }
}