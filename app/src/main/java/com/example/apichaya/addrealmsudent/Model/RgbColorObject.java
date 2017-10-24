package com.example.apichaya.addrealmsudent.Model;

import io.realm.RealmObject;

/**
 * Created by apichaya on 10/19/2017 AD.
 */

public class RgbColorObject extends RealmObject {

    private int id;
    private int chemicalId;
    int redValue , greenValue , blueValue;

    public int getChemicalId() {
        return chemicalId;
    }

    public void setChemicalId(int chemicalId) {
        this.chemicalId = chemicalId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public RgbColorObject(int id, int chemicalId, int redValue, int greenValue, int blueValue) {
        this.id = id;
        this.chemicalId = chemicalId;
        this.redValue = redValue;
        this.greenValue = greenValue;
        this.blueValue = blueValue;
    }

    public RgbColorObject(int redValue , int greenValue , int blueValue) {
        this.redValue = redValue;
        this.greenValue = greenValue;
        this.blueValue = blueValue;

    }
    public RgbColorObject() {

    }
}
