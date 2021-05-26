package com.lh.gasapp.chart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.MainActivity;
import com.lh.gasapp.R;
import com.lh.gasapp.firebase.FirebaseWrapper;
import com.lh.gasapp.firebase.valueEventListener.DetailValueEachDayListener;
import com.lh.gasapp.firebase.valueEventListener.LineChartValueEventListener;
import com.lh.gasapp.homeActivity.Home;
import com.lh.gasapp.model.DetailValue;
import com.lh.gasapp.model.DynamicLineChartManager;

import java.util.ArrayList;
import java.util.Calendar;

public class DynamicLineChartActivity extends AppCompatActivity {

    private DynamicLineChartManager dynamicLineChartManager;
    private ArrayList<Integer> gasValues = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ValueEventListener lineChartValueListener;
    private LineChart lineChart_widget;

    private DatabaseReference firebaseReference;
    private ArrayList<DetailValue> detailValueList;
    private String dateToShowChart;

    private ImageView iconBack;
    private ImageView iconCalendar;

    private ArrayList<String> dateList;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_line_chart);
        attachListenerToDays();
        dateToShowChart = dateList.get(dateList.size() - 1);
        Log.d("DATE", dateToShowChart);

        initIconToolbar();

        lineChart_widget = (LineChart) findViewById(R.id.dynamic_chart);

        initChartData();
    }

    private void initIconToolbar() {
        iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DynamicLineChartActivity.this, Home.class);
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                dateToShowChart = dayOfMonth + "_" + month;
            }
        };

        iconCalendar = findViewById(R.id.icon_calendar);
        iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DynamicLineChartActivity.this, setListener, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    public void attachListenerToDays(){
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dateList.clear();
                Iterable<DataSnapshot> listOfDates = snapshot.getChildren();
                for (DataSnapshot eachDateAsKey : listOfDates) {
                    dateList.add(eachDateAsKey.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initChartData() {
        dynamicLineChartManager = new DynamicLineChartManager(lineChart_widget);
        dynamicLineChartManager.setYAxis(1100, 0, 100);
        attachValueListenerToDate();
        dynamicLineChartManager.setDescription("Data of " + dateToShowChart);
    }

    private void getDataFromIncomingIntent() {
        Intent intent = getIntent();
        dateToShowChart = intent.getStringExtra("Date");
        Log.d("DATE", dateToShowChart);
        attachValueListenerToDate();
    }

    private void attachValueListenerToDate(){
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        DetailValueEachDayListener detailValueEachDayListener = new DetailValueEachDayListener(dateToShowChart);
        detailValueEachDayListener.attachLineChart(dynamicLineChartManager);
        reference.addValueEventListener(detailValueEachDayListener);
    }



}