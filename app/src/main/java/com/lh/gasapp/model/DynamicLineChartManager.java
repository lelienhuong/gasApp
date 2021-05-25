package com.lh.gasapp.model;

import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.lh.gasapp.chart.LineChartWidgetDecorator;
import com.lh.gasapp.chart.LineDataSetFactory;
import com.lh.gasapp.chart.axis.AxisFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DynamicLineChartManager {

    private LineChart lineChart_widget;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    private List<String> timeList = new ArrayList<>();


    public DynamicLineChartManager(LineChart lineChart_widget) {
        this.lineChart_widget = lineChart_widget;
        leftAxis= AxisFactory.getAxisLeft(this.lineChart_widget);
        rightAxis= AxisFactory.getAxisRight(this.lineChart_widget);
        xAxis = AxisFactory.getXAxis(this.lineChart_widget, timeList);
        lineData = new LineData();

        initSingleLineDataSet();
        initLineChart();
    }

    public DynamicLineChartManager(LineChart mLineChart, List<String> names, List<Integer> colors) {
        this.lineChart_widget = mLineChart;
        leftAxis= AxisFactory.getAxisLeft(this.lineChart_widget);
        rightAxis= AxisFactory.getAxisRight(this.lineChart_widget);
        xAxis = AxisFactory.getXAxis(this.lineChart_widget, timeList);
        lineData = new LineData();
        initMultipleLineDataSet(names, colors);
        initLineChart();
    }

    private void initSingleLineDataSet() {
        lineDataSet = LineDataSetFactory.createSingleLineDataSet();
        lineData.addDataSet(lineDataSet);
    }

    private void initLineChart() {
        LineChartWidgetDecorator.decorate(lineChart_widget);
        lineChart_widget.setData(lineData);
        lineChart_widget.invalidate();
    }

    private void initMultipleLineDataSet(List<String> names, List<Integer> colors) {
        lineDataSets = LineDataSetFactory.createMultipleLineDataSet(names, colors);
    }

    public void addEntry(int yValue, String xTime) {
        timeList.add(xTime);
        Log.d("DEBUG", "Adding entry: " + yValue + " at " + lineDataSet.getEntryCount());

        Entry entry = new Entry(lineDataSet.getEntryCount() , yValue);
        lineData.addEntry(entry, 0);
        lineData.notifyDataChanged();
        lineChart_widget.notifyDataSetChanged();
        lineChart_widget.setVisibleXRangeMaximum(6);
        lineChart_widget.moveViewToX(lineDataSet.getEntryCount() - 5);
    }

    public void setEntryList(ArrayList<DetailValue> detailValueList) {
        timeList.clear();
        lineData.clearValues();
        lineDataSet = LineDataSetFactory.createSingleLineDataSet();
        lineData.addDataSet(lineDataSet);
        for(DetailValue detailValue : detailValueList ){
            addEntry(detailValue.gasValue, detailValue.time);
        }
    }


    public void addEntry(List<Integer> numbers) {

        if (lineDataSets.get(0).getEntryCount() == 0) {
            lineData = new LineData(lineDataSets);
            lineChart_widget.setData(lineData);
        }
        if (timeList.size() > 11) {
            timeList.clear();
        }
        timeList.add(df.format(System.currentTimeMillis()));
        for (int i = 0; i < numbers.size(); i++) {
            Entry entry = new Entry(lineDataSet.getEntryCount(), numbers.get(i));
            lineData.addEntry(entry, i);
            lineData.notifyDataChanged();
            lineChart_widget.notifyDataSetChanged();
            lineChart_widget.setVisibleXRangeMaximum(6);
            lineChart_widget.moveViewToX(lineData.getEntryCount() - 5);
        }
    }

    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);
        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        lineChart_widget.invalidate();
    }

    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "High limit line";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftAxis.addLimitLine(hightLimit);
        lineChart_widget.invalidate();
    }

    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "Low limit line";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftAxis.addLimitLine(hightLimit);
        lineChart_widget.invalidate();
    }

    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart_widget.setDescription(description);
        lineChart_widget.invalidate();
    }

}