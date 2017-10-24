package com.example.apichaya.addrealmsudent.database;

import com.example.apichaya.addrealmsudent.Model.RgbColorObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by apple on 5/15/2017 AD.
 */

public class TestManager {

    private Realm realm;

    public TestManager() {
        realm = Realm.getDefaultInstance();
    }

    public void addChemical(final RgbColorObject chemicalObject) {
        realm.beginTransaction();
        RgbColorObject object = realm.createObject(RgbColorObject.class);
        object.setId(chemicalObject.getId());
        object.setChemicalId(chemicalObject.getChemicalId());
        object.setBlueValue(chemicalObject.getBlueValue());
        object.setGreenValue(chemicalObject.getGreenValue());
        object.setRedValue(chemicalObject.getRedValue());

        realm.commitTransaction();
    }

    public void deleteAll() {
        // obtain the results of a query
        final RealmResults<RgbColorObject> results = realm.where(RgbColorObject.class)
                .findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }

    public void delete(int id) {
        // obtain the results of a query
        final RealmResults<RgbColorObject> results = realm.where(RgbColorObject.class)
                .equalTo("id", id)
                .findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
                results.deleteFirstFromRealm();
            }
        });
    }

    public void deleteWithChemicalId(int chemicalId) {
        // obtain the results of a query
        final RealmResults<RgbColorObject> results = realm.where(RgbColorObject.class)
                .equalTo("chemicalId", chemicalId)
                .findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove all match
                results.deleteAllFromRealm();
            }
        });
    }

    public ArrayList<RgbColorObject> queryAll() {
        RealmQuery<RgbColorObject> query = realm.where(RgbColorObject.class);
        RealmResults<RgbColorObject> result = query
                .findAll();
        ArrayList<RgbColorObject> chemicalArrayList = new ArrayList<>();
        chemicalArrayList.addAll(result);
        return chemicalArrayList;
    }

    public ArrayList<RgbColorObject> query(int chemicalId) {
        RealmQuery<RgbColorObject> query = realm.where(RgbColorObject.class);
        RealmResults<RgbColorObject> result = query
                .equalTo("chemicalId", chemicalId)
                .findAll();
        ArrayList<RgbColorObject> chemicalArrayList = new ArrayList<>();
        chemicalArrayList.addAll(result);
        return chemicalArrayList;
    }

    public int getSize() {
        RealmQuery<RgbColorObject> query = realm.where(RgbColorObject.class);
        RealmResults<RgbColorObject> result = query
                .findAll();
        return result.size();
    }
}
