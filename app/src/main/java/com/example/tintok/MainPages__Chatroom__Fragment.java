package com.example.tintok;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.tintok.Adapters_ViewHolder.ChatroomAdapter;
import com.example.tintok.CustomView.NoSpaceRecyclerViewDecoration;
import com.example.tintok.Model.ChatRoom;

import java.util.ArrayList;

public class MainPages__Chatroom__Fragment extends Fragment implements ChatroomAdapter.onChatRoomClickListener {

    EditText searchBar;
    RecyclerView chatrooms;
    ChatroomAdapter adapter;
    private MainPages_Chatroom_ViewModel mViewModel;

    public static MainPages__Chatroom__Fragment newInstance() {
        return new MainPages__Chatroom__Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mainpages__chatroom__fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainPages_Chatroom_ViewModel.class);
        // TODO: Use the ViewModel
        init();
        initChatRoom();
       mViewModel.getChatrooms().observe(this.getViewLifecycleOwner(), chatRooms -> adapter.setItems(chatRooms));
       ArrayList<ChatRoom> mChatRooms = mViewModel.getChatrooms().getValue();
       for(int i = 0 ; i < mChatRooms.size();i++){
           int finalI = i;
           mChatRooms.get(i).getMessageEntities().observe(this.getViewLifecycleOwner(), messageEntities -> {
                adapter.notifyItemChanged(finalI);
           });
       }
    }

    void init(){
        searchBar = getView().findViewById(R.id.search_text);
        chatrooms = getView().findViewById(R.id.roomList);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.setItems(mViewModel.filterByName(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void initChatRoom(){
        this.adapter = new ChatroomAdapter(this.getContext(), mViewModel.getChatrooms().getValue(),this);
        chatrooms.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration= new NoSpaceRecyclerViewDecoration();
        chatrooms.setLayoutManager(manager);
        chatrooms.addItemDecoration(decoration);
    }

    @Override
    public void OnClick(int pos) {
        Intent mIntent = new Intent(this.requireActivity(), Activity_ChatRoom.class);
        requireActivity().overridePendingTransition(R.anim.animation_in, R.anim.animation_out);
        mIntent.putExtra("roomID", adapter.getItems().get(pos).getChatRoomID());
        startActivity(mIntent);
    }
}