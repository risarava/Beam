package com.example.apichaya.addrealmsudent.Model;

/**
 * Created by apple on 10/23/2017 AD.
 */

public class ChemicalObjectBase {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_BODY = 1;
    public static final int TYPE_FOOTER = 2;

    private int id;
    private String name;
    private int ppm;

    private int redValue;
    private int greenValue;
    private int blueValue;
    private int number;

    private double aveRed;
    private double aveGreen;
    private double aveBlue;

    private int type;

    public ChemicalObjectBase() {
    }

    public ChemicalObjectBase(String name, double aveRed, double aveGreen, double aveBlue, int type) {
        this.name = name;
        this.aveRed = aveRed;
        this.aveGreen = aveGreen;
        this.aveBlue = aveBlue;
        this.type = type;
    }

    public double getAveRed() {
        return aveRed;
    }

    public void setAveRed(double aveRed) {
        this.aveRed = aveRed;
    }

    public double getAveGreen() {
        return aveGreen;
    }

    public void setAveGreen(double aveGreen) {
        this.aveGreen = aveGreen;
    }

    public double getAveBlue() {
        return aveBlue;
    }

    public void setAveBlue(double aveBlue) {
        this.aveBlue = aveBlue;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPpm() {
        return ppm;
    }

    public void setPpm(int ppm) {
        this.ppm = ppm;
    }

    public int getRedValue() {
        return redValue;
    }

    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public int getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
