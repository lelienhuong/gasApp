package com.lh.gasapp.firebase.valueEventListener;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.model.DetailValue;
import com.lh.gasapp.chart.DynamicLineChartManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DetailValueEachDayListener implements ValueEventListener {


    private String dateToShowChart;
    private ArrayList<DetailValue> detailValueList;
    private DynamicLineChartManager dynamicLineChartManager;
    private Boolean isFirstTime = true;

    public DetailValueEachDayListener(String dateToShowChart){
        this.dateToShowChart = dateToShowChart;
        detailValueList = new ArrayList<DetailValue>();
    }

    public void attachLineChart(DynamicLineChartManager dynamicLineChartManager) {
        this.dynamicLineChartManager = dynamicLineChartManager;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        detailValueList.clear();
        readCurrentDetailValues(snapshot);
    }

    private void readCurrentDetailValues(DataSnapshot snapshot) {
        Iterable<DataSnapshot> listOfDetailValue = snapshot.child(dateToShowChart).getChildren();
        DetailValue value;
        Set<String> timeList;
        for (DataSnapshot eachDetailValue : listOfDetailValue){

            HashMap<String, String> hashMap = (HashMap<String, String>) eachDetailValue.getValue();
            timeList = hashMap.keySet();

            for ( String time : timeList ) {
                String test = String.valueOf(hashMap.get(time));
                value = new DetailValue(time,Integer.parseInt( test ));

                detailValueList.add(value);
            }
        }
        dynamicLineChartManager.setEntryList(detailValueList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}

