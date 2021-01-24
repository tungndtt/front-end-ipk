package com.example.tintok.Communication.RestAPI_model;

import com.google.gson.annotations.SerializedName;

public class UnknownUserForm {
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("birthday")
    private String birthday;
    /*
    @SerializedName("location")
    private String location;
     */


    public UnknownUserForm(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }
    public String getEmail() {
        return email;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
