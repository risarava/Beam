package com.example.apichaya.addrealmsudent.database;

import com.example.apichaya.addrealmsudent.Model.ChemicalObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by apple on 5/15/2017 AD.
 */

public class ChemicalManager {

    private Realm realm;

    public ChemicalManager() {
        realm = Realm.getDefaultInstance();
    }

    public void addChemical(final ChemicalObject chemicalObject) {

        if (!isChemical(chemicalObject.getId())) {
            realm.beginTransaction();
            ChemicalObject object = realm.createObject(ChemicalObject.class);
            object.setId(chemicalObject.getId());
            object.setName(chemicalObject.getName());
            object.setPpm(chemicalObject.getPpm());
            realm.commitTransaction();
        } else {
            update(chemicalObject);
        }
    }

    public void update(final ChemicalObject chemicalObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ChemicalObject query = realm.where(ChemicalObject.class)
                        .equalTo("id", chemicalObject.getId())
                        .findFirst();
                query.setName(chemicalObject.getName());
                query.setPpm(chemicalObject.getPpm());
            }
        });
    }

    public void deleteAll() {
        // obtain the results of a query
        final RealmResults<ChemicalObject> results = realm.where(ChemicalObject.class)
                .findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });

        TestManager testManager = new TestManager();
        testManager.deleteAll();
    }

    public void delete(int chemicalId) {
        // obtain the results of a query
        final RealmResults<ChemicalObject> results = realm.where(ChemicalObject.class)
                .equalTo("id", chemicalId)
                .findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
                results.deleteFirstFromRealm();
            }
        });

        TestManager testManager = new TestManager();
        testManager.deleteWithChemicalId(chemicalId);
    }

    public ArrayList<ChemicalObject> queryAll() {
        RealmQuery<ChemicalObject> query = realm.where(ChemicalObject.class);
        RealmResults<ChemicalObject> result = query
                .findAll();
        ArrayList<ChemicalObject> chemicalArrayList = new ArrayList<>();
        chemicalArrayList.addAll(result);
        return chemicalArrayList;
    }

    public ChemicalObject getChemical(int chemicalId) {
        RealmQuery<ChemicalObject> query = realm.where(ChemicalObject.class);
        RealmResults<ChemicalObject> result = query
                .equalTo("id", chemicalId)
                .findAll();
        return result.get(0);
    }

    public int getSize() {
        RealmQuery<ChemicalObject> query = realm.where(ChemicalObject.class);
        RealmResults<ChemicalObject> result = query
                .findAll();
        return result.size();
    }

    public boolean isChemical(int chemicalId) {
        RealmQuery<ChemicalObject> query = realm.where(ChemicalObject.class);
        RealmResults<ChemicalObject> result = query
                .equalTo("id", chemicalId)
                .findAll();
        if (result.size() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
