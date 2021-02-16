package com.example.tintok.DataLayer;

public class ResponseEvent {

    private String response;
    private Type type;
    private boolean hasBeenHandled = false;

    public ResponseEvent(Type type, String message){
        this.type = type;
        this.response = message;
    }

    public String getContentIfNotHandled(){
        if(hasBeenHandled)
            return null;
        else {
            hasBeenHandled = true;
            return response;
        }
    }
    public Type getType(){
        return type;
    }

    public String peekContent(){
        return response;
    }

    public enum Type {PASSWORD, USER_UPDATE, PROFILE_PICTURE_UPDATE, PROFILE_PICTURE_UPLOAD, INTEREST_UPDATE};

}
