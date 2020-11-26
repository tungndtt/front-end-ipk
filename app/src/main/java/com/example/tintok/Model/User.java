package com.example.tintok.Model;

import java.util.ArrayList;
import java.util.Queue;

public class User extends RecyclerViewModel {
    private String userID;
    private String userName;
    private String email;
    private ArrayList<String> followers;
    private ArrayList<String> followering;
    private Queue<User> matching;
    private ArrayList<String> myPosts;
    private ArrayList<ChatRoom> chatRooms;
    private MediaEntity profilePic;


}
