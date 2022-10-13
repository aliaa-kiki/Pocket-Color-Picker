package com.example.pocketcolorpicker.models;

import java.util.ArrayList;
import java.util.List;

public class paletteOb {


    private int id;
    private String name;
    private List<colorOb> colorList;

    // constructor for populated palette
    public paletteOb(int id, String name, List<colorOb> colorList) {
        this.id= id;
        this.name = name;
        this.colorList = colorList;
    }

    //constructor for an empty palette
    public paletteOb(int id, String name) {
        this.id= id;
        this.name = name;
        this.colorList = new ArrayList<>();
    }

    // adding a color to the palette
    public void addColor (colorOb colorOb){
        if (colorOb == null){
            throw new IllegalArgumentException("color can't be null");
        }
        colorList.add(colorOb);
    }

    //getters and setters
    public String getColorsCodesList (){
        String colorsList= "";
        if (this.colorList.isEmpty()){}

        else { for (colorOb color :this.colorList){
                try { colorsList +=String.valueOf(color.getId())+",";}
                catch (Exception e){}

            }}
        return colorsList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<colorOb> getColorList() {
        return colorList;
    }

    public void setColorList(List<colorOb> colorList) {
        this.colorList = colorList;
    }
}
