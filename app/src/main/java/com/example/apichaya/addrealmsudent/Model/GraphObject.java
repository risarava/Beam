package com.example.apichaya.addrealmsudent.Model;

/**
 * Created by apple on 11/3/2017 AD.
 */

public class GraphObject {

    private String title;
    private int color;
    private double value;

    public GraphObject(String title, int color, double value) {
        this.title = title;
        this.color = color;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
