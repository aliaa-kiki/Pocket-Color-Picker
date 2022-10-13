package com.example.pocketcolorpicker.models;

import android.graphics.Color;

import java.util.concurrent.atomic.AtomicInteger;

public class colorOb implements Comparable<colorOb> {

    private int id;
    private int r, g, b, h, v, colorGroup;
    private String hex;
    private String note;

    public colorOb(int id, int r, int g, int b, String note) {
        this.id =id;
        this.r = r;
        this.g = g;
        this.b = b;
        this.note = note;
        this.hex= this.toHex();
        // finding the HSV code to find the hue and value
        float[] hsv= new float[3];
        Color.RGBToHSV(this.r, this.g, this.b, hsv);
        int hue =(int) hsv[0];
        this.h = hue;
        float temp = hsv[2]*1000;
        int value = (int) temp;
        this.v = value;

        this.colorGroup = this.sortIntoColorGroups();
    }
    public colorOb (int id, int r, int g, int b){
        this(id, r, g, b," ");
    }

    public String toHex (){
        String c_hex = String.format("#%02X%02X%02X", this.r, this.g, this.b);
        return c_hex;
    }

    public int sortIntoColorGroups (){
        if (h >= 18 && h < 44){
            //orange
            return 2;
        }
        else if (h >= 44 && h < 61){
            //yellow
            return 3;
        }
        else if (h >= 61 && h < 162){
            //green
            return 4;
        }
        else if (h >= 162 && h < 268){
            //blue
            return 5;
        }
        else if (h >= 268 && h < 337){
            //purple
            return 6;
        }
        else{
            //red
            return 1;
        }
    }


    @Override
    public String toString() {
        return  id +
                ", " + r +
                ", " + g +
                ", " + b +
                ", " + note ;
    }

    public String getRGB (){
        return (this.r +","+ this.g + ","+ this.b) ;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getV() {
        return v;
    }

    public String getHex() {
        return hex;
    }



    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getColorGroup() {
        return colorGroup;
    }

    public int compareTo(colorOb color){
        int compareValue = ((colorOb)color).getV();
        return compareValue - this.v;
    }
}
