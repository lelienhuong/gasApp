package com.lh.gasapp.model;

public class SensorData {
    private int gasData;
    private boolean people;
    private boolean statusHuman;

    public SensorData(){

    }

    public SensorData(SensorData targetProductToCopyFrom) {
        this.gasData = targetProductToCopyFrom.gasData;
        this.people = targetProductToCopyFrom.people;
        this.statusHuman = targetProductToCopyFrom.statusHuman;
    }

    public SensorData(int gasData, boolean people, boolean statusHuman) {
        this.gasData = gasData;
        this.people = people;
        this.statusHuman = statusHuman;
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

}
