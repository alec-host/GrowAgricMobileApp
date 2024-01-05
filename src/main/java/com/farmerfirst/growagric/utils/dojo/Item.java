package com.farmerfirst.growagric.utils.dojo;

public class Item {

    private int image;
    private String name;
    private String value;

    public Item(int k, String n, String v) {
        this.image = k;
        this.name = n;
        this.value = v;
    }

    public int getImage(){
        return image;
    }

    public void setImage(int image){
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) { this.value = value; }
}