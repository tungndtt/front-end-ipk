package com.example.tintok.Model;

import androidx.annotation.Nullable;

public class UserSimple  {
    private String userID;
    private String userName;
    private String email;
    private String description;
    private int age;
    //Location needed
    private MediaEntity profilePic;

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

    public MediaEntity getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(MediaEntity profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        try{
            return ((UserSimple)obj).getUserID().compareTo(this.getUserID()) == 0;
        }catch (Exception e){
            return super.equals(obj);
        }
    }
}
