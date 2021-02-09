package com.example.tintok.Model;

import android.widget.CheckBox;

public class Interest {

    private int id;
    private int imageResource;
    private String interest;
    private CheckBox checkbox;
    private boolean isSelected;

    public Interest(int id, int imageResource, String interest) {
        this.id = id;
        this.imageResource = imageResource;
        this.interest = interest;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public int getId(){
        return id;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }
}
