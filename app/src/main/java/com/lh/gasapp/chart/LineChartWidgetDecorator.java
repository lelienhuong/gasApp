package com.lh.gasapp.chart;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;

public class LineChartWidgetDecorator {

    public static final float LEGEND_SIZE = 11f;
    public static final Legend.LegendForm LEGEND_FORM = Legend.LegendForm.LINE;

    private static LineChart currentWidget;

    public static void decorate(LineChart lineChart_widget) {
        currentWidget = lineChart_widget;

        decorateChartBorder();
        decorateChartLegend();
    }
    private static void decorateChartBorder() {
        currentWidget.setDrawGridBackground(false);
        currentWidget.setDrawBorders(true);
    }

    private static void decorateChartLegend() {
        Legend legend = currentWidget.getLegend();
        legend.setForm(LEGEND_FORM);
        legend.setTextSize(LEGEND_SIZE);

        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }
}
