package com.lh.gasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.model.DynamicLineChartManager;
import com.lh.gasapp.model.RasData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DynamicLineChart extends AppCompatActivity {

    private DynamicLineChartManager dynamicLineChartManager;
    private List<Integer> gasValues = new ArrayList<>(); //Data collection
    private List<String> names = new ArrayList<>(); // collection of polyline names
    private ArrayList<Integer> colour = new ArrayList<>();//Polyline color collection
    private ArrayList<String> time = new ArrayList<>();
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_line_chart);

        getSupportActionBar().setTitle("Realtime Chart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LineChart mChart = (LineChart) findViewById(R.id.dynamic_chart);

        //Polyline name
        names.add("Gas Values");
        //Polyline color
        colour.add(Color.GREEN);

        dynamicLineChartManager = new DynamicLineChartManager(mChart, names.get(0), colour.get(0));

        dynamicLineChartManager.setYAxis(1100, 0, 100);

        Intent intent = getIntent();
        if (intent != null) {
            gasValues = intent.getIntegerArrayListExtra("gas values");
            time = intent.getStringArrayListExtra("time list");
        }

        for (int i = 0; i < gasValues.size(); i++) {
            dynamicLineChartManager.addEntry(gasValues.get(i), time.get(i));
        }

        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RasData rasData = snapshot.getValue(RasData.class);
                int gasValue = (int) rasData.getGasData();
                dynamicLineChartManager.addEntry(gasValue, df.format(System.currentTimeMillis()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}