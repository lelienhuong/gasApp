package com.lh.gasapp.chart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LineDataSetFactory {
    public static final float LEGEND_SIZE = 11f;
    public static final Legend.LegendForm LEGEND_FORM = Legend.LegendForm.LINE;

    public static final int MAX_LABEL_TO_DISPLAY_ON_SCREEN = 10;

    public static final float LINE_WIDTH = 1.5f;
    public static final float CURVE_RADIUS = 1.5f;
    public static final float VALUE_DISPLAY_SIZE = 10;

    public static final String DATASET_NAME = "Gas Values";
    public static final int LINE_COLOR  = Color.GREEN;

    private LineChart lineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    private List<String> timeList = new ArrayList<>();


    public static LineDataSet createSingleLineDataSet() {

        LineDataSet lineDataSet = new LineDataSet(null, DATASET_NAME);
        lineDataSet.setLineWidth(LINE_WIDTH);
        lineDataSet.setCircleRadius(CURVE_RADIUS);
        lineDataSet.setColor(LINE_COLOR);
        lineDataSet.setCircleColor(LINE_COLOR);
        lineDataSet.setHighLightColor(LINE_COLOR);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setValueTextSize(VALUE_DISPLAY_SIZE);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        return lineDataSet;
    }

}
