package com.lh.gasapp.firebase.valueEventListener;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.SensorValueDisplayer;
import com.lh.gasapp.model.SensorData;
import com.lh.gasapp.utils.GasDangerChecker;

import java.util.ArrayList;


public class MyValueEventListener implements ValueEventListener {

    private int gasValue;
    private SensorValueDisplayer sensorValueDisplayer;
    private GasDangerChecker gasDangerChecker;

    private ArrayList<Integer> gasValueList = new ArrayList<>();
    private long time1 = 0, time2 = 0,previous;
    private double oldData = -1,oldValue = -1;
    public boolean check = false;
    public int index = 0;
    public static ArrayList<Integer> gasValues = new ArrayList<Integer>();

    ArrayList<Integer> analysisValuesArrays = new ArrayList<>();

    public MyValueEventListener() {
        gasDangerChecker = new GasDangerChecker();
    }

    public void attachValueDisplayer(SensorValueDisplayer sensorValueDisplayer) {
        this.sensorValueDisplayer = sensorValueDisplayer;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        SensorData sensorData = snapshot.getValue(SensorData.class);
        for (DataSnapshot data: snapshot.child("gasValue_recent").getChildren())
        {
//            String key=data.getKey();
            int value=Integer.parseInt(data.getValue().toString());
            gasValues.add(value);
        }
         //   Log.d("IDDDD", String.valueOf(gasValues));

        notifyGasValueChanged(sensorData);
    }

    private void notifyGasValueChanged(SensorData sensorData) {

 //       String keyID = FirebaseDatabase.getInstance().getReference().child("ERL43dCv3RXCMy8LRLzyIt6WtLH3").child("gasArray").push().getKey();
   //     Log.d("IDDDD",keyID);
//        gasValues = sensorData.getGasArray();
//        Log.d("MANGTEST", String.valueOf(sensorData.getGasArray()));
//        gasValue = sensorData.getGasData();
        gasValue = gasValues.get(gasValues.size()-1);
        sensorValueDisplayer.notifyGasValueChanged(gasValue);
        double value = sensorData.getGasData();
//        if(time1 == 0) {
//            time1 = System.currentTimeMillis();
//            time2 = 0;
//            oldValue = (int) sensorData.getGasData();
//        }else{
//            time2 = System.currentTimeMillis();
//        }
        if(gasValues.get(gasValues.size()-1) > 400){
//            Log.d("Giatri", String.valueOf(gasValues.get(gasValues.size()-1)));
            sensorValueDisplayer.notifyGasStatusNotSafe();
        }else{
            sensorValueDisplayer.notifyGasStatusSafe();
        }
        notifyHumanDetectionStatus(sensorData);
//        int gasValue = (int) sensorData.getGasData();
        if(gasValue > 400 && check == false){
            index = gasValues.size();
               check = true;
        }
//        if(time2 != 0) {
            if(gasValues.size() - index >= 20 && check == true) {
                check = false;
                int tong = 0;
                int i = index - 10;
                if (i < 0) {
                    i = 0;
                    for (int j = i; j < index; j++) {
                        tong = tong + gasValues.get(j);
                    }
                    tong = tong / 10; // 5 gia tri trong 1 khung
                    analysisValuesArrays.add(tong);
                    i = index;
                    while (i <= index + 10) {
                        for (int j = i; j < i + 10; j++) {
                            tong = tong + gasValues.get(j);
                        }
                        tong = tong / 10; // 5 gia tri trong 1 khung
                        analysisValuesArrays.add(tong);
                        i = i + 10;
                    }
                } else {
                    while (i <= index + 10) {
                        for (int j = i; j < i + 10; j++) {
                            tong = tong + gasValues.get(j);
                        }
                        tong = tong / 10; // 5 gia tri trong 1 khung
                        analysisValuesArrays.add(tong);
                        i = i + 10;
                    }
                }
                for (int k = 0; k < analysisValuesArrays.size() - 1; k++) {
                    if (analysisValuesArrays.get(k) < analysisValuesArrays.get(k + 1)) {
                        continue;
                    } else {
                        analysisValuesArrays.clear();
                        break;
                    }
                }
                if (analysisValuesArrays.size() == 0) {
                    sensorValueDisplayer.notifyGasStatusSafe();
                } else {
                    sensorValueDisplayer.notifyGasStatusNotSafe();
                    analysisValuesArrays.clear();
                    sensorValueDisplayer.startAlarm();
                }
            }
//            int soluong = (int) (time2 - time1)/1000;
//            for (int i = 1; i <= soluong; i++) {
//                gasValues.add((int) oldValue);
//            }
//            oldValue = gasValue;
//            time1 = time2;
//            Log.d("MANG", String.valueOf(gasValues));
//        }
    }

    private void notifyHumanDetectionStatus(SensorData sensorData) {
        if (sensorData.isPeoplePresented()) {
            sensorValueDisplayer.notifyHumanDetected();
        } else {
            sensorValueDisplayer.notifyHumanNotDetected();
        }
    }

    public ArrayList<Integer> getGasList() {
        return gasValueList;
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
