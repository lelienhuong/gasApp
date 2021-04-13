package com.lh.gasapp.firebaseWrapper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.SensorValueDisplayer;
import com.lh.gasapp.model.SensorData;

import java.util.ArrayList;

public class MyValueEventListener implements ValueEventListener {

    private int gasValue;
    private SensorValueDisplayer sensorValueDisplayer;
    private GasDangerChecker gasDangerChecker;

    private ArrayList<Integer> gasValueList = new ArrayList<>();

    public MyValueEventListener() {
        gasDangerChecker = new GasDangerChecker();
    }

    public void attachValueDisplayer(SensorValueDisplayer sensorValueDisplayer) {
        this.sensorValueDisplayer = sensorValueDisplayer;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        SensorData sensorData = snapshot.getValue(SensorData.class);

        notifyGasValueChanged(sensorData);
        notifyHumanDetectionStatus(sensorData);
        notifyNewPointInTime();
    }

    private void notifyGasValueChanged(SensorData sensorData) {
        gasValue = sensorData.getGasData();
        sensorValueDisplayer.notifyGasValueChanged(gasValue);

        gasValueList.add(gasValue);
        if(gasDangerChecker.isNewGasValueSafe(gasValue) == true){
            sensorValueDisplayer.notifyGasStatusSafe();
        }else{

            sensorValueDisplayer.notifyGasStatusNotSafe();
        }
    }

    private void notifyHumanDetectionStatus(SensorData sensorData) {
        if (sensorData.isPeoplePresented()) {
            sensorValueDisplayer.notifyHumanDetected();
        } else {
            sensorValueDisplayer.notifyHumanNotDetected();
        }
    }

    private void notifyNewPointInTime() {
        sensorValueDisplayer.notifyNewPointInTime();
    }

    public ArrayList<Integer> getGasValues() {
        return gasValueList;
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
