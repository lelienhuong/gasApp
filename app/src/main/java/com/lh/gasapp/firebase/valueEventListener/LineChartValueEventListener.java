package com.lh.gasapp.firebase.valueEventListener;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.model.DynamicLineChartManager;
import com.lh.gasapp.model.SensorData;

import java.text.SimpleDateFormat;

public class LineChartValueEventListener {

    private DynamicLineChartManager dynamicLineChartManager;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public LineChartValueEventListener(DynamicLineChartManager dynamicLineChartManager) {
        this.dynamicLineChartManager = dynamicLineChartManager;
    }

    public void onDataChange(@NonNull DataSnapshot snapshot) {
        SensorData sensorData = snapshot.getValue(SensorData.class);
        int gasValue = (int) sensorData.getGasData();
        dynamicLineChartManager.addEntry(gasValue, simpleDateFormat.format(System.currentTimeMillis()));

    }

    public void onCancelled(@NonNull DatabaseError error) {

    }
}
