package com.lh.gasapp.utils;

import java.util.ArrayList;

public class GasDangerChecker {
    public static final int SIZE_OF_ARRAYLIST_TO_EXAMPLE = 10;
    public static final int GAS_DANGER_THRESHOLD = 400;


    private ArrayList<Integer> gasList = new ArrayList<>();
    private ArrayList<Integer> leftMostGasList = new ArrayList<>();
    private ArrayList<Integer> rightMostGasList = new ArrayList<>();

    private int savedDangerIndex;

    private boolean startedExampling = false;
    private boolean isSafe = true;

    public boolean isNewGasValueSafe(int gasValue) {
        gasList.add(gasValue);

        if(hasStartedExampling() && currentIndexIsRightMostIndex()){
            finishExampling();
        }

        if(gasValue > GAS_DANGER_THRESHOLD && hasNotStartedExamplingYet()){
            startExampling();
        }

        return isSafe;
    }

    private void finishExampling() {
        saveRightMostGasList();
        checkWhetherItIsSafeOrNot();
    }

    private void checkWhetherItIsSafeOrNot() {
        float averageGasValue_left = getAverage(leftMostGasList);
        float averageGasValue_right = getAverage(rightMostGasList);

        if(averageGasValue_left < averageGasValue_right){
            isSafe = false;
        }
        startedExampling = false;
    }

    private float getAverage(ArrayList<Integer> list) {
        float result = 0;
        for(int  i = 0; i < list.size(); i++){
            result += list.get(i);
        }
        result /= list.size();
        return result;
    }

    private void saveRightMostGasList() {
        int rightMostIndex = getCurrentGasIndex();
        for(int  i = savedDangerIndex + 1 ; i <= rightMostIndex ; i++){
            rightMostGasList.add(gasList.get(i));
        }
    }

    private boolean currentIndexIsRightMostIndex() {
        return getCurrentGasIndex() - savedDangerIndex == SIZE_OF_ARRAYLIST_TO_EXAMPLE;
    }

    private boolean hasNotStartedExamplingYet() {
        return !hasStartedExampling();
    }

    private boolean hasStartedExampling() {
        return startedExampling;
    }

    private void startExampling() {
        savedDangerIndex = getCurrentGasIndex();
        saveLeftMostGasList();

        startedExampling = true;
    }


    private void saveLeftMostGasList() {
        int leftMostIndex = getLeftMostIndex();
        for(int  i = leftMostIndex ; i < savedDangerIndex ; i++){
            leftMostGasList.add(gasList.get(i));
        }
    }

    private int getLeftMostIndex() {
        int result = getCurrentGasIndex() - SIZE_OF_ARRAYLIST_TO_EXAMPLE;
        if(result < 0 ){
            result = 0;
        }
        return result;
    }

    private int getCurrentGasIndex() {
        return  gasList.size() - 1;
    }

}
