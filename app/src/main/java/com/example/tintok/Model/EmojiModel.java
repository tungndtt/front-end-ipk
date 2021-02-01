package com.example.tintok.Model;

import android.content.Context;

import java.util.ArrayList;

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

    public static ArrayList<EmojiModel> getEmojis(Context context){
        ArrayList<EmojiModel> emojis = new ArrayList<>();
        String dataname = "sample";
        int emojiID;
        int i = 1;
        do {
            String imgName = dataname + i;
            emojiID = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
            if (emojiID == 0)
                break;
            emojis.add(new EmojiModel(imgName, emojiID));
            i++;
        } while (true);
        return emojis;
    }
}
