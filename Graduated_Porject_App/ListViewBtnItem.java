package com.example.aict.myapplication;

import android.graphics.drawable.Drawable;


public class ListViewBtnItem {
    private Drawable iconDrawable ;
    private String textStr ;
    private String btnStr;
    private String btnColor;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setText(String text) { textStr = text ; }
    public void setbtnText(String text) { btnStr = text;}
    public void setbtnColor(String color) { btnColor = color;}

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getText() {
        return this.textStr ;
    }
    public String getBtnStr() { return this.btnStr; }
    public String getBtnColor() { return this.btnColor;}

}
