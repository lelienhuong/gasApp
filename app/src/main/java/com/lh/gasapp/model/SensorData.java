package com.lh.gasapp.model;

import com.lh.gasapp.firebase.FirebaseWrapper;

import java.util.ArrayList;

public class SensorData {
    private int gasData;
    private boolean people;
    private ArrayList<Integer> gasValue_recent = new ArrayList<Integer>();
    private boolean isBellOnRequired = false;
    private ArrayList gasValueHistory = new ArrayList();
    public SensorData(){
    }

    public SensorData(SensorData targetProductToCopyFrom) {
        this.gasData = targetProductToCopyFrom.gasData;
        this.people = targetProductToCopyFrom.people;
        this.gasValue_recent = targetProductToCopyFrom.gasValue_recent;
        this.isBellOnRequired = targetProductToCopyFrom.isBellOnRequired;
        this.gasValueHistory = targetProductToCopyFrom.gasValueHistory;
    }


    public void setBellOnRequired(boolean bellOnRequired) {
        FirebaseWrapper.getReferrence().child("isBellOnRequired").setValue(bellOnRequired);
    }

    public SensorData(int gasData, boolean people, boolean statusHuman, ArrayList<Integer> gasValue_recent, boolean isBellOnRequired, ArrayList gasValueHistory) {
        this.gasData = gasData;
        this.people = people;
        this.gasValue_recent = gasValue_recent;
        this.isBellOnRequired = isBellOnRequired;
        this.gasValueHistory = gasValueHistory;
    }

    public ArrayList getGasValueHistory() {
        return gasValueHistory;
    }

    public void setGasValueHistory(ArrayList gasValueHistory) {
        this.gasValueHistory = gasValueHistory;
    }

    public int getGasData() {
        return gasData;
    }

    public void setGasData(int gasData) {
        this.gasData = gasData;
    }

    public boolean isPeople() {
        return people;
    }

    public void setPeople(boolean people) {
        this.people = people;
    }

    public ArrayList<Integer> getGasValue_recent() {
        return gasValue_recent;
    }

    public void setGasValue_recent(ArrayList<Integer> gasValue_recent) {
        this.gasValue_recent = gasValue_recent;
    }

    public boolean isBellOnRequired() {
        return isBellOnRequired;
    }
}
