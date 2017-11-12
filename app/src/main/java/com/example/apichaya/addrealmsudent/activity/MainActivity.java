package com.example.apichaya.addrealmsudent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apichaya.addrealmsudent.Model.ChemicalObject;
import com.example.apichaya.addrealmsudent.Model.ChemicalObjectBase;
import com.example.apichaya.addrealmsudent.Model.RgbColorObject;
import com.example.apichaya.addrealmsudent.R;
import com.example.apichaya.addrealmsudent.adepter.ChemicalAdapter;
import com.example.apichaya.addrealmsudent.customs.AbstractToolbarActivity;
import com.example.apichaya.addrealmsudent.customs.Functions;
import com.example.apichaya.addrealmsudent.database.ChemicalManager;
import com.example.apichaya.addrealmsudent.database.TestManager;
import com.example.apichaya.addrealmsudent.dialog.MyAlertDialog;

import java.util.ArrayList;

/**
 * Created by apple on 10/23/2017 AD.
 */

public class MainActivity extends AbstractToolbarActivity implements View.OnClickListener {

    public static final String EXTRA_CHEMICAL_ID = "EXTRA_CHEMICAL_ID";
    private RecyclerView recyclerView;
    private TextView txtAddChemical;
    private TextView txtViewGraph;
    private TextView txtNotFound;

    private ChemicalManager chemicalManager;
    private TestManager testManager;
    private LinearLayoutManager linearLayoutManager;
    private ChemicalAdapter chemicalAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindActionbar(ImageView imgIcon, ImageView menuLeft, ImageView imgIconRight, TextView txtTitleToolbar) {
        setTitle("Substance List");
    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        txtAddChemical = (TextView) findViewById(R.id.textviewAddChemical);
        txtViewGraph = (TextView) findViewById(R.id.textviewViewGraph);
        txtNotFound = (TextView) findViewById(R.id.textviewNotFound);

        txtAddChemical.setOnClickListener(this);
        txtViewGraph.setOnClickListener(this);

        chemicalManager = new ChemicalManager();
        testManager = new TestManager();

    }

    @Override
    protected void setupUI() {
        initRecycleView();

    }

    private void initRecycleView() {
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        chemicalAdapter = new ChemicalAdapter(activity);
        recyclerView.setAdapter(chemicalAdapter);

        chemicalAdapter.setOnClickListener(new ChemicalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemId) {

            }

            @Override
            public void onItemRemove(View view, final int itemId, final int position) {
                MyAlertDialog.dialogAlert(activity, new MyAlertDialog.OnClickPositiveListener() {
                    @Override
                    public void onClicked() {
                        chemicalManager.delete(itemId);
                        setChemicalData();
                    }
                });
            }

            @Override
            public void addItemClick(View view, int chemicalId) {
                Intent intent = new Intent(activity, AddActivity.class);
                intent.putExtra(EXTRA_CHEMICAL_ID, chemicalId);
                startActivity(intent);
            }

            @Override
            public void editItemClick(View view, final int chemicalId, final int position) {
                ChemicalObject chemicalObject = chemicalManager.getChemical(chemicalId);
                MyAlertDialog.dialogEditChemical(activity,
                        chemicalObject.getName(),
                        chemicalObject.getPpm(),
                        new MyAlertDialog.OnClickPositiveAddSubstanceListener() {
                            @Override
                            public void onClicked(String name, String ppm) {
                                chemicalManager.update(new ChemicalObject(
                                        chemicalId, name, Integer.parseInt(ppm)));
                                chemicalAdapter.updateChemicalName(position, name, Integer.parseInt(ppm));
                            }
                        });
            }
        });
    }

    private void setChemicalData() {
        ArrayList<ChemicalObject> chemicalArrayList = chemicalManager.queryAll();
        ArrayList<RgbColorObject> rgbColorArrayList = testManager.queryAll();

        ArrayList<ChemicalObjectBase> objectBaseArrayList = new ArrayList<>();
        for (ChemicalObject object : chemicalArrayList) {
            ChemicalObjectBase base = new ChemicalObjectBase();
            base.setId(object.getId());
            base.setName(object.getName());
            base.setPpm(object.getPpm());
            base.setId(object.getId());
            base.setType(ChemicalObjectBase.TYPE_HEADER);
            objectBaseArrayList.add(base);
            int index = 0;
            for (RgbColorObject rgbColorObject : rgbColorArrayList) {
                if (rgbColorObject.getChemicalId() == object.getId()) {
                    base = new ChemicalObjectBase();
                    base.setRedValue(rgbColorObject.getRedValue());
                    base.setGreenValue(rgbColorObject.getGreenValue());
                    base.setBlueValue(rgbColorObject.getBlueValue());
                    base.setId(rgbColorObject.getId());
                    base.setNumber(index + 1);
                    base.setType(ChemicalObjectBase.TYPE_BODY);
                    objectBaseArrayList.add(base);
                    index++;
                }
            }

            //calculation
            ArrayList<RgbColorObject> arrayList = testManager.query(object.getId());
            if (arrayList.size() > 0) {
                ChemicalObjectBase chemicalAVE = new ChemicalObjectBase(
                        "AVE",
                        Functions.getAVE(arrayList, Functions.COLOR_RED),
                        Functions.getAVE(arrayList, Functions.COLOR_GREEN),
                        Functions.getAVE(arrayList, Functions.COLOR_BLUE),
                        ChemicalObjectBase.TYPE_FOOTER);
                objectBaseArrayList.add(chemicalAVE);

                ChemicalObjectBase chemicalSD = new ChemicalObjectBase(
                        "SD",
                        Functions.getSD(arrayList, chemicalAVE.getAveRed(), Functions.COLOR_RED),
                        Functions.getSD(arrayList, chemicalAVE.getAveGreen(), Functions.COLOR_GREEN),
                        Functions.getSD(arrayList, chemicalAVE.getAveBlue(), Functions.COLOR_BLUE),
                        ChemicalObjectBase.TYPE_FOOTER);
                objectBaseArrayList.add(chemicalSD);


                ChemicalObjectBase chemicalPercentRSD = new ChemicalObjectBase(
                        "% RSD",
                        ((chemicalSD.getAveRed() / chemicalAVE.getAveRed()) * 100),
                        ((chemicalSD.getAveGreen() / chemicalAVE.getAveGreen()) * 100),
                        ((chemicalSD.getAveBlue() / chemicalAVE.getAveBlue()) * 100),
                        ChemicalObjectBase.TYPE_FOOTER);
                objectBaseArrayList.add(chemicalPercentRSD);


            }

        }
        txtNotFound.setVisibility((objectBaseArrayList.size() == 0) ? View.VISIBLE : View.GONE);
        chemicalAdapter.setData(objectBaseArrayList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setChemicalData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textviewAddChemical:
                MyAlertDialog.dialogAddChemical(activity, new MyAlertDialog.OnClickPositiveAddSubstanceListener() {
                    @Override
                    public void onClicked(String name, String ppm) {
                        chemicalManager.addChemical(new ChemicalObject(
                                chemicalManager.getSize() + 1, name, Integer.parseInt(ppm)));
                        setChemicalData();
                    }
                });
                break;
            case R.id.textviewViewGraph:
                Intent intent = new Intent(activity, ViewGraphActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }
}
