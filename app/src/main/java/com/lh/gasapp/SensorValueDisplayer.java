package com.lh.gasapp;

import java.util.ArrayList;

public interface SensorValueDisplayer {

    public void notifyGasValueChanged(double gasValue);

    public void notifyNewPointInTime();

    public void notifyGasStatusNotSafe();
    public void notifyGasStatusSafe();

    public void notifyHumanDetected();
    public void notifyHumanNotDetected();

    public void startAlarm();
}
