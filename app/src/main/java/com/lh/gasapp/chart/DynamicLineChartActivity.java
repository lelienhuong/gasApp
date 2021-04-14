package com.lh.gasapp.chart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.R;
import com.lh.gasapp.firebase.FirebaseWrapper;
import com.lh.gasapp.firebase.valueEventListener.LineChartValueEventListener;
import com.lh.gasapp.model.DynamicLineChartManager;

import java.util.ArrayList;

public class DynamicLineChartActivity extends AppCompatActivity {

    private DynamicLineChartManager dynamicLineChartManager;
    private ArrayList<Integer> gasValues = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ValueEventListener lineChartValueListener;
    private LineChart lineChart_widget;

    private DatabaseReference firebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_line_chart);

        getSupportActionBar().setTitle("Realtime Chart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lineChart_widget = (LineChart) findViewById(R.id.dynamic_chart);

        initChartData();
    }

    private void initChartData() {
        dynamicLineChartManager = new DynamicLineChartManager(lineChart_widget);
        dynamicLineChartManager.setYAxis(1100, 0, 100);
        getDataFromIncomingIntent();
        attachValueListenerToFirebase();
    }

    private void attachValueListenerToFirebase() {
        lineChartValueListener = new LineChartValueEventListener(dynamicLineChartManager);

        firebaseReference = FirebaseWrapper.getReferrence();
        firebaseReference.addValueEventListener(lineChartValueListener);
    }

    private void getDataFromIncomingIntent() {
        Intent intent = getIntent();
        gasValues = intent.getIntegerArrayListExtra("gas values");
        time = intent.getStringArrayListExtra("time list");
        fillLineChart();
    }

    private void fillLineChart() {
        for (int i = 0; i < gasValues.size(); i++) {
            dynamicLineChartManager.addEntry(gasValues.get(i), time.get(i));
        }
    }
}