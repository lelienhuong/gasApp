package com.lh.gasapp.model;

import java.util.ArrayList;

public class SensorData {
    private int gasData;
    private boolean people;
    private boolean statusHuman;
    private ArrayList<Integer> gasValue_recent = new ArrayList<Integer>();
    private boolean isBellOnRequired = false;
    public SensorData(){

    }

    public SensorData(SensorData targetProductToCopyFrom) {
        this.gasData = targetProductToCopyFrom.gasData;
        this.people = targetProductToCopyFrom.people;
        this.statusHuman = targetProductToCopyFrom.statusHuman;
        this.gasValue_recent = targetProductToCopyFrom.gasValue_recent;
        this.isBellOnRequired = targetProductToCopyFrom.isBellOnRequired;
    }

    public boolean isBellOnRequired() {
        return isBellOnRequired;
    }

    public void setBellOnRequired(boolean bellOnRequired) {
        isBellOnRequired = bellOnRequired;
    }

    public SensorData(int gasData, boolean people, boolean statusHuman, ArrayList<Integer> gasValue_recent, boolean isBellOnRequired) {
        this.gasData = gasData;
        this.people = people;
        this.statusHuman = statusHuman;
        this.gasValue_recent = gasValue_recent;
        this.isBellOnRequired = isBellOnRequired;
    }

    public int getGasData() {
        return gasData;
    }

    public void setGasData(int gasData) {
        this.gasData = gasData;
    }

    public boolean isPeoplePresented() {
        return people;
    }

    public void setPeople(boolean people) {
        this.people = people;
    }

    public ArrayList<Integer> getGasArray() {
        return gasValue_recent;
    }
}
