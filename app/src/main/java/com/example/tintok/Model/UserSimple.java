package com.example.tintok.Model;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class UserSimple  {
    private String userID;
    private String userName;
    private String email;
    private String description;
    private LocalDate birthday;     //yyyy-MM-dd
    private Integer age;
    private Gender gender;
    private String location;
    private MediaEntity profilePic;
    private String newPassword;

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
    public int getAge(){
        return age;
    }
    public void setAge(){
        if(birthday != null){
            LocalDate today = LocalDate.now(ZoneId.systemDefault());
            this.age = Integer.valueOf(Period.between(birthday, today).getYears());
        }else this.age =  Integer.valueOf(0);
    }
    public Gender getGender() {
        return gender;
    }
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        setAge();
    }
    public void setGender(int gender) {
        switch(gender){
            //case 0: this.gender = Gender.UNKNOWN;
            //    break;
            case 1: this.gender = Gender.MALE;
                break;
            case 2: this.gender = Gender.FEMALE;
                break;
            case 3: this.gender = Gender.DIVERS;
        }

    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public enum Gender{
        UNKNOWN(0), MALE(1), FEMALE(2), DIVERS(3);
        private int i;
        Gender(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }
    }
}
