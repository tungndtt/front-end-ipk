package com.example.tintok.DataLayer;

import java.util.ArrayList;

public class DataRepository_Interest {
    public static String[] interests = {
        "LEARNING", "TRAVELING", "SPORT", "READING", "GAMING"
    };

    public static ArrayList<InterestTag> parseData(boolean[] bitmap){
        ArrayList<InterestTag> tags = new ArrayList<>();
        if(interests.length != bitmap.length)
            return null;
        for(int i = 0 ; i<bitmap.length;i++)
            tags.add(new InterestTag(interests[i], bitmap[i]));
        return tags;
    }

    public static class InterestTag{
        public String tag;
        public boolean isChecked;
        public InterestTag(String tag, boolean isChecked){
            this.tag = tag;
            this.isChecked = isChecked;
        }
    }
}
