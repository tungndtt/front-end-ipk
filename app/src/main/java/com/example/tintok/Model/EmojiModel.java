package com.example.tintok.Model;

public class EmojiModel {

    private final String resourceImgName;
    private final int resourceID;

    public EmojiModel(String resourceImgName, int resourceID) {
        this.resourceImgName = resourceImgName;
        this.resourceID = resourceID;
    }

    public String getResourceImgName() {
        return resourceImgName;
    }

    public int getResourceID() {
        return resourceID;
    }

}
