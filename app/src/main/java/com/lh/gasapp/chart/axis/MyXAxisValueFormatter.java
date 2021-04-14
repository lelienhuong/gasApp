package com.lh.gasapp.chart.axis;


import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.List;

public class MyXAxisValueFormatter extends ValueFormatter {


    private final List<String> timeList;

    public MyXAxisValueFormatter(List<String> timeList) {
        this.timeList = timeList;
    }

    @Override
    public String getFormattedValue(float value) {

        if (isOutOfTimeListRange(value) ) {
            return "0";
        }
        else{
            return String.valueOf(timeList.get((int) value));
        }

    }

    private boolean isOutOfTimeListRange(float value) {
       return value < 0 || value >= timeList.size();
    }
}
