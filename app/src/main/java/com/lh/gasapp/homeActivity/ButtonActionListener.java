package com.lh.gasapp.homeActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.lh.gasapp.chart.DynamicLineChartActivity;
import com.lh.gasapp.homeActivity.Home;

import java.util.ArrayList;

public class ButtonActionListener implements View.OnClickListener{


    private Home homeActivity;


    public ButtonActionListener(Home home) {
        this.homeActivity = home;
    }

    @Override
    public void onClick(View v) {
        showChart();
    }

    private void showChart() {
        Intent intent = new Intent(homeActivity.getApplicationContext(), DynamicLineChartActivity.class);
        intent.putExtra("gas values", homeActivity.myValueEventListener.getGasList());
        intent.putExtra("time list", homeActivity.timeList);
        homeActivity.startActivity(intent);
    }
}
