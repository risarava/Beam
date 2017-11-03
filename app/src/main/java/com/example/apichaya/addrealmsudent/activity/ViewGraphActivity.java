package com.example.apichaya.addrealmsudent.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apichaya.addrealmsudent.Model.ChemicalObject;
import com.example.apichaya.addrealmsudent.Model.GraphObject;
import com.example.apichaya.addrealmsudent.Model.RgbColorObject;
import com.example.apichaya.addrealmsudent.R;
import com.example.apichaya.addrealmsudent.customs.AbstractToolbarActivity;
import com.example.apichaya.addrealmsudent.customs.Functions;
import com.example.apichaya.addrealmsudent.database.ChemicalManager;
import com.example.apichaya.addrealmsudent.database.TestManager;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 11/3/2017 AD.
 */

public class ViewGraphActivity extends AbstractToolbarActivity {

    private ArrayList<String> xAxisTitle = new ArrayList<>();

    private CombinedChart chart;

    private ChemicalManager chemicalManager;
    private TestManager testManager;

    @Override
    protected int setContentView() {
        return R.layout.activity_view_graph;
    }

    @Override
    protected void bindActionbar(ImageView imgIcon, ImageView menuLeft, LinearLayout toolbar, TextView txtTitleToolbar) {
        setTitle("View graph");
    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        chart = (CombinedChart) findViewById(R.id.barLineChart);

        chemicalManager = new ChemicalManager();
        testManager = new TestManager();

    }

    @Override
    protected void setupUI() {
        onBackPressedButtonLeft();
        setChemicalData();
    }

    private void setChemicalData() {
        ArrayList<ChemicalObject> chemicalArrayList = chemicalManager.queryAll();

        ArrayList<GraphObject> graphRedArrayList = new ArrayList<>();
        ArrayList<GraphObject> graphGreenArrayList = new ArrayList<>();
        ArrayList<GraphObject> graphBlueArrayList = new ArrayList<>();

        for (ChemicalObject object : chemicalArrayList) {
            //calculation
            ArrayList<RgbColorObject> arrayList = testManager.query(object.getId());
            if (arrayList.size() > 0) {
                graphRedArrayList.add(new GraphObject("Red", R.color.color_red,
                        Functions.getAVE(arrayList, Functions.COLOR_RED)));

                graphGreenArrayList.add(new GraphObject("Green", R.color.color_green,
                        Functions.getAVE(arrayList, Functions.COLOR_GREEN)));

                graphBlueArrayList.add(new GraphObject("Blue", R.color.color_blue,
                        Functions.getAVE(arrayList, Functions.COLOR_BLUE)));
                xAxisTitle.add(String.valueOf(object.getPpm()));
            }
        }

        setChart(graphRedArrayList, graphGreenArrayList, graphBlueArrayList);
    }

    private void setChart(ArrayList<GraphObject> graphRedArrayList, ArrayList<GraphObject> graphGreenArrayList,
                          ArrayList<GraphObject> graphBlueArrayList) {
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        chart.setPinchZoom(true);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setTextSize(12f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setTextSize(12f);
        xAxis.setGranularity(1f);

        chart.getAxisRight().setEnabled(false);

        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisTitle.get((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        //set data to graph
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(getLineDataSet(graphRedArrayList));
        dataSets.add(getLineDataSet(graphGreenArrayList));
        dataSets.add(getLineDataSet(graphBlueArrayList));

        LineData lineData = new LineData(dataSets);

        CombinedData data = new CombinedData();
        data.setData(lineData);

        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private LineDataSet getLineDataSet(ArrayList<GraphObject> objectArrayList) {
        ArrayList<Entry> entryArrayList = new ArrayList<>();

        for (int index = 0; index < objectArrayList.size(); index++) {
            entryArrayList.add(new Entry(index, (float) objectArrayList.get(index).getValue()));
        }

        LineDataSet set = new LineDataSet(entryArrayList, objectArrayList.get(0).getTitle());
        set.setColor(activity.getResources().getColor(objectArrayList.get(0).getColor()));
        set.setLineWidth(3.0f);
        set.setCircleColorHole(activity.getResources().getColor(objectArrayList.get(0).getColor()));
        set.setCircleColor(activity.getResources().getColor(objectArrayList.get(0).getColor()));
        set.setCircleRadius(5f);
        set.setFillColor(activity.getResources().getColor(objectArrayList.get(0).getColor()));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setValueTextSize(8);
        set.setDrawValues(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return set;
    }
}
