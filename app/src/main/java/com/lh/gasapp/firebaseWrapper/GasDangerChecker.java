package com.lh.gasapp.firebaseWrapper;

import android.util.Log;

import java.util.ArrayList;

public class GasDangerChecker {
    public static final int MAX_DELAY_BETWEEN_UPDATING_EVENT = 30000;
    public static final int MAX_GAS_LIST_SIZE = 5;
    private static final int MAX_ANALYSIS_LIST_SIZE = 3;

    private long previousTime , currentTime ;

    private ArrayList<Integer> gasList = new ArrayList<>();
    private ArrayList<Double> analizeList = new ArrayList<>();

    private int tong;
    private double averageGasValue;

    public GasDangerChecker(){
        previousTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();

    }

    public boolean isNewGasValueSafe(int gasValue) {
        updateCurrentTime();

        if(notTooLongSinceLastUpdate()) {
            addNewGasValue(gasValue);

            if(gasValueListIsFull()){
                addNewAverageValueToAnalize();
                resetGasList();

                if(analysisValuesArrayIsFull()){
                    if(isGasAtRisk()) return false;
                    analizeList.clear();
                }
            }
        }
        else{
            analizeList.clear();
            gasList.clear();
            gasList.add(gasValue);
            tong = gasValue;
        }
        previousTime = currentTime;
        return true;
    }

    private void resetGasList() {
        gasList.clear();
        tong = 0;
    }

    private void addNewGasValue(int gasValue) {
        gasList.add(gasValue);
        tong = tong + gasValue;
    }

    private boolean isGasAtRisk() {
        for (int i = 0; i < analizeList.size() - 1; i++) {
            if (analizeList.get(i) > analizeList.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private void addNewAverageValueToAnalize() {
        averageGasValue = tong/5;
        analizeList.add(averageGasValue);
    }

    private boolean analysisValuesArrayIsFull() {
        return analizeList.size() == MAX_ANALYSIS_LIST_SIZE;
    }

    private boolean gasValueListIsFull() {
        return gasList.size() == MAX_GAS_LIST_SIZE;
    }

    private boolean notTooLongSinceLastUpdate() {
        return currentTime - previousTime <= MAX_DELAY_BETWEEN_UPDATING_EVENT;
    }

    private void updateCurrentTime() {
        currentTime = System.currentTimeMillis();
    }
}
