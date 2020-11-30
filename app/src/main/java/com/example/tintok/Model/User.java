package com.example.tintok.Model;

import java.util.ArrayList;
import java.util.Queue;

public class User extends RecyclerViewModel {
    private String userID;
    private String userName;
    private String email;
    private String description;
    private ArrayList<String> followers;
    private ArrayList<String> followering;
    private Queue<User> matching;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowering() {
        return followering;
    }

    public void setFollowering(ArrayList<String> followering) {
        this.followering = followering;
    }

    public Queue<User> getMatching() {
        return matching;
    }

    public void setMatching(Queue<User> matching) {
        this.matching = matching;
    }

    public ArrayList<Post> getMyPosts() {
        return myPosts;
    }

    public void setMyPosts(ArrayList<Post> myPosts) {
        this.myPosts = myPosts;
    }

    public ArrayList<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(ArrayList<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public MediaEntity getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(MediaEntity profilePic) {
        this.profilePic = profilePic;
    }

    private ArrayList<Post> myPosts;
    private ArrayList<ChatRoom> chatRooms;
    private MediaEntity profilePic;


}
