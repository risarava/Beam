package com.example.apichaya.addrealmsudent.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.apichaya.addrealmsudent.Model.RgbColorObject;
import com.example.apichaya.addrealmsudent.R;
import com.example.apichaya.addrealmsudent.adepter.MyAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by apichaya on 10/19/2017 AD.
 */

public class CalculateActivity extends AppCompatActivity {

    Button btnAve;
    TextView tvAveRed;
    TextView tvAveGreen;
    TextView tvAveBlue;

    Button btnSD;
    TextView tvSDred;
    TextView tvSDgreen;
    TextView tvSDblue;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<RgbColorObject> rgbColorObjectArrayList = new ArrayList<>();
    Realm realm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        initInstance();

    }

    private void initInstance() {
        realm = Realm.getDefaultInstance();
        btnAve = (Button) findViewById(R.id.btnAve);
        tvAveRed = (TextView) findViewById(R.id.tvAveRed);
        tvAveGreen = (TextView) findViewById(R.id.tvAveGreen);
        tvAveBlue = (TextView) findViewById(R.id.tvAveBlue);


        btnSD = (Button) findViewById(R.id.btnSD);
        tvSDred = (TextView) findViewById(R.id.tvSDred);
        tvSDgreen = (TextView) findViewById(R.id.tvSDgreen);
        tvSDblue = (TextView) findViewById(R.id.tvSDblue);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MyAdapter(getApplicationContext(), rgbColorObjectArrayList);
        mRecyclerView.setAdapter(mAdapter);
        showData();
    }

    private double getSumAVE(int val1, int index) {
        double sum;
        sum = val1 / index;
        return sum;
    }

    private void showData() {

        RealmResults<RgbColorObject> result = realm.where(RgbColorObject.class).findAll();
        rgbColorObjectArrayList = new ArrayList<>();
        rgbColorObjectArrayList.addAll(result);
        mAdapter.setRgbColorObjectArrayList(rgbColorObjectArrayList);

        calculateAVE(rgbColorObjectArrayList);

    }

    private void calculateAVE(ArrayList<RgbColorObject> arrayList) {
        int sumRed = 0;
        int sumBlue = 0;
        int sumGreen = 0;
        int index = 0;

        for (RgbColorObject color : arrayList) {
            sumRed += color.getRedValue();
            sumBlue += color.getBlueValue();
            sumGreen += color.getGreenValue();
            index++;
        }

        double aveRed = getSumAVE(sumRed, index);
        double aveBlue = getSumAVE(sumBlue, index);
        double aveGreen = getSumAVE(sumGreen, index);

        double sdRed = 0;
        double sdBlue = 0;
        double sdGreen = 0;

        for (RgbColorObject color : arrayList) {
            sdRed +=  Math.pow(color.getRedValue() - aveRed, 2);
            sdBlue += Math.pow(color.getBlueValue() - aveBlue, 2);
            sdGreen +=  Math.pow(color.getGreenValue() - aveGreen, 2);
        }

        sdRed = Math.pow( (sdRed/ (index -1)), 0.5);
        sdGreen =  Math.pow(( sdGreen/ (index -1)), 0.5);
        sdBlue =  Math.pow( (sdBlue/ (index -1)), 0.5);

        tvAveRed.setText(String.format("%.2f",aveRed));
        tvAveBlue.setText(String.format("%.2f",aveBlue));
        tvAveGreen.setText(String.format("%.2f",aveGreen));

        tvSDred.setText(String.format("%.2f",sdRed));
        tvSDgreen.setText(String.format("%.2f",sdGreen));
        tvSDblue.setText(String.format("%.2f",sdBlue));
    }

}
