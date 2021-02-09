package com.example.tintok.DataLayer;

import com.example.tintok.Model.Interest;
import com.example.tintok.R;

import java.util.ArrayList;

public class DataRepository_Interest {
    public static String[] interests = {"GAMING", "READING", "TRAVELLING",
            "SPORT", "SHOPPING", "LEARNING","GOSSIP"
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
    public static ArrayList<Interest> interestArrayList = new ArrayList<Interest>();
    public static ArrayList<Interest> getInterestArrayList() {
        if(interestArrayList.size() == 0) {
            interestArrayList.add(new Interest(0, R.drawable.ic_edit, interests[0]));          //GAMING
            interestArrayList.add(new Interest(1,R.drawable.ic_arrow_send, interests[1]));    //READING
            interestArrayList.add(new Interest(2,R.drawable.ic_close, interests[2]));         //TRAVELLING
            interestArrayList.add(new Interest(3,R.drawable.ic_key, interests[3]));           //SPORT
            interestArrayList.add(new Interest(4,R.drawable.ic_backspace, interests[4]));     //SHOPPING
            interestArrayList.add(new Interest(5,R.drawable.ic_backspace, interests[5]));     //LEARNING
            interestArrayList.add(new Interest(6,R.drawable.ic_backspace, interests[6]));     //GOSSIP
        }
        return interestArrayList;
    }
}
