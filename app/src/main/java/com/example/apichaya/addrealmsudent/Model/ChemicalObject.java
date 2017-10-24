package com.example.apichaya.addrealmsudent.Model;

import io.realm.RealmObject;

/**
 * Created by apple on 10/23/2017 AD.
 */

public class ChemicalObject extends RealmObject{

    private int id;
    private String name;
    private int ppm;

    public ChemicalObject() {
    }

    public ChemicalObject(int id, String name, int ppm) {
        this.id = id;
        this.name = name;
        this.ppm = ppm;
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
}
