package com.lh.gasapp;

public interface SensorValueDisplayer {

    public void notifyGasValueChanged(int gasValue);

    public void notifyNewPointInTime();

    public void notifyGasStatusNotSafe();
    public void notifyGasStatusSafe();

    public void notifyHumanDetected();
    public void notifyHumanNotDetected();
}
