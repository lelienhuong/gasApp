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
import com.lh.gasapp.notification.Alarm;
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
    public int index = 0,lastIndexAnalysis=-1;
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
        SensorData sensorData = new SensorData();
        sensorData.setPeople((Boolean) snapshot.child("people").getValue());
        gasValues.clear();
        for (DataSnapshot data: snapshot.child("gasValue_recent").getChildren())
        {
//            String key=data.getKey();
            int value=Integer.parseInt(data.getValue().toString());
            gasValues.add(value);
        }
        notifyGasValueChanged(sensorData);

    }

    private void notifyGasValueChanged(SensorData sensorData) {
        Log.d("ZOOOO","zo");
        gasValue = gasValues.get(gasValues.size()-1);
        sensorValueDisplayer.notifyGasValueChanged(gasValue);
        notifyHumanDetectionStatus(sensorData);
        if(gasValue > 400 && check == false){ //check để lưu vị trí tại điểm đột biến có giá trị >400
            index = gasValues.size()-1; //gasValues.size();
               check = true;
        }
        if(gasValues.size() - index >= 10 && check == true) { //20
                lastIndexAnalysis = index + 5*2 -1; //so 2 dau tien la mot mang co bnhieu gia tri (=10)
                check = false;
                int tong = 0;
                int i = index - 5;
                if (i < 0) {
                    i = 0;
                    for (int j = i; j < index; j++) {
                        tong = tong + gasValues.get(j);
                    }
                    tong = tong / (index); // gia tri trong 1 khung  //moi sua them
                    analysisValuesArrays.add(tong);
                    i = index;
                    while (i <= index + 5) {
                        for (int j = i; j < i + 5; j++) {
                            tong = tong + gasValues.get(j);
                        }
                        tong = tong / 5; // 10 gia tri trong 1 khung
                        analysisValuesArrays.add(tong);
                        i = i + 5;
                    }
                } else {
                    while (i <= index + 5) {
                        for (int j = i; j < i + 5; j++) {
                            tong = tong + gasValues.get(j);
                        }
                        tong = tong / 5; // 10 gia tri trong 1 khung
                        analysisValuesArrays.add(tong);
                        i = i + 5;
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
                    Log.d("BINHTHUONG","OKKKKKKK");
                    sensorValueDisplayer.notifyGasStatusSafe();
                    sensorData.setBellOnRequired(false); //moi sua them
                    try {
                        Alarm.mediaPlayer.stop();
                        Alarm.instance.finish();
                    }catch (Exception e){
                        Log.d("NULL","null");
                    }
                } else {
                    Log.d("BAODONG","OKKKKKKK");
                    analysisValuesArrays.clear();
                    sensorValueDisplayer.notifyGasStatusNotSafe();
                    sensorValueDisplayer.startAlarm(sensorData);
                }
            }
        if(lastIndexAnalysis!=-1 && gasValues.size() - lastIndexAnalysis >= 15 && check== false){
            Log.d("BINHTHUONG","OKKKKKKK");
            sensorValueDisplayer.notifyGasStatusSafe();
            sensorData.setBellOnRequired(false);
            try {
                Alarm.mediaPlayer.stop();
                Alarm.instance.finish();
            }catch (Exception e){
                Log.d("NULL","null");
            }
        }

    }

    private void notifyHumanDetectionStatus(SensorData sensorData) {
        if (sensorData.isPeople()) {
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
