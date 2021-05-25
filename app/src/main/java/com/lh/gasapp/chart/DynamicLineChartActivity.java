package com.lh.gasapp.chart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.R;
import com.lh.gasapp.firebase.FirebaseWrapper;
import com.lh.gasapp.firebase.valueEventListener.DetailValueEachDayListener;
import com.lh.gasapp.firebase.valueEventListener.LineChartValueEventListener;
import com.lh.gasapp.model.DetailValue;
import com.lh.gasapp.model.DynamicLineChartManager;

import java.util.ArrayList;

public class DynamicLineChartActivity extends AppCompatActivity {

    private DynamicLineChartManager dynamicLineChartManager;
    private ArrayList<Integer> gasValues = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ValueEventListener lineChartValueListener;
    private LineChart lineChart_widget;

    private DatabaseReference firebaseReference;
    private ArrayList<DetailValue> detailValueList;
    private String dateToShowChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_line_chart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lineChart_widget = (LineChart) findViewById(R.id.dynamic_chart);

        initChartData();
        getSupportActionBar().setTitle("Ngày " + dateToShowChart);
    }

    private void initChartData() {
        dynamicLineChartManager = new DynamicLineChartManager(lineChart_widget);
        dynamicLineChartManager.setYAxis(1100, 0, 100);
        getDataFromIncomingIntent();
        dynamicLineChartManager.setDescription("Data of " + dateToShowChart);
    }

    private void getDataFromIncomingIntent() {
        Intent intent = getIntent();
        dateToShowChart = intent.getStringExtra("Date");
        Log.d("DATE", dateToShowChart);
        attachValueListenerToDate();
    }

    private void attachValueListenerToDate(){
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        DetailValueEachDayListener detailValueEachDayListener = new DetailValueEachDayListener(dateToShowChart);
        detailValueEachDayListener.attachLineChart(dynamicLineChartManager);
        reference.addValueEventListener(detailValueEachDayListener);
    }



}