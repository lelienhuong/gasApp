package com.lh.gasapp.chart.axis;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.List;

public class AxisFactory {
    public static final int MAX_LABEL_TO_DISPLAY_ON_SCREEN = 10;

    public static XAxis getXAxis(LineChart lineChart_widget, List<String> timeList) {
        XAxis xAxis = lineChart_widget.getXAxis();
        initXAxis(xAxis);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(timeList));
        return xAxis;
    }

    private static void initXAxis(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(MAX_LABEL_TO_DISPLAY_ON_SCREEN);
    }

    public static YAxis getAxisLeft(LineChart lineChart_widget) {
        YAxis leftAxis = lineChart_widget.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        return leftAxis;
    }

    public static YAxis getAxisRight(LineChart lineChart_widget) {
        YAxis rightAxis = lineChart_widget.getAxisRight();
        rightAxis.setAxisMinimum(0f);
        return rightAxis;
    }
}
