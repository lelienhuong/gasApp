package com.lh.gasapp.model;

public class RasData {
    private long gasData;
    private boolean people;
    private boolean statusHuman;

    public RasData(){

    }
    public RasData(RasData targetProductToCopyFrom) {
        this.gasData = targetProductToCopyFrom.gasData;
        this.people = targetProductToCopyFrom.people;
        this.statusHuman = targetProductToCopyFrom.statusHuman;
    }

    public RasData(long gasData, boolean people, boolean statusHuman) {
        this.gasData = gasData;
        this.people = people;
        this.statusHuman = statusHuman;
    }

    public long getGasData() {
        return gasData;
    }

    public void setGasData(long gasData) {
        this.gasData = gasData;
    }

    public boolean isPeople() {
        return people;
    }

    public void setPeople(boolean people) {
        this.people = people;
    }

    public boolean isStatusHuman() {
        return statusHuman;
    }

    public void setStatusHuman(boolean statusHuman) {
        this.statusHuman = statusHuman;
    }
}
