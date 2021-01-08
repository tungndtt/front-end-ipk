package com.example.tintok.Model;

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
}
