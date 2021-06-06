package com.lh.gasapp.chart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.R;
import com.lh.gasapp.firebase.FirebaseWrapper;
import com.lh.gasapp.firebase.valueEventListener.DetailValueEachDayListener;
import com.lh.gasapp.model.DetailValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class DynamicLineChartActivity extends AppCompatActivity {

    private DynamicLineChartManager dynamicLineChartManager;
    private ArrayList<Integer> gasValues = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ValueEventListener lineChartValueListener;
    private LineChart lineChart_widget;

    private DatabaseReference firebaseReference;
    private ArrayList<DetailValue> detailValueList = new ArrayList<>();
    private String dateToShowChart;
    private String anotherDateToShowChart;
    private ArrayList<String> dateList = new ArrayList<>();

    private ImageView iconBack;
    private ImageView iconCalendar;

    String timeToShowChart = "";
    boolean kt = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_line_chart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Biểu đồ");

        lineChart_widget = (LineChart) findViewById(R.id.dynamic_chart);

        attachListenerToViewDays();
    }

    public void attachListenerToViewDays(){
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dateList.clear();
                    Iterable<DataSnapshot> listOfDates = snapshot.getChildren();
                    for (DataSnapshot eachDateAsKey : listOfDates) {
                        dateList.add(eachDateAsKey.getKey());
                    }
                    if (dateList.size() > 0){
                        dateToShowChart = dateList.get(dateList.size()-1);
                        attachValueListenerToDateHere();
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void attachValueListenerToDateHere() {
        detailValueList.clear();
        dynamicLineChartManager = new DynamicLineChartManager(lineChart_widget);
        dynamicLineChartManager.setYAxis(1100, 0, 100);
        anotherDateToShowChart = dateToShowChart.replace('_', '/');
        dynamicLineChartManager.setDescription("Data of " + anotherDateToShowChart);
        Query secondQuery = FirebaseWrapper.getReferrence().child("gasValueHistory").child(dateToShowChart).limitToLast(60);
        secondQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> listOfDetailValue = snapshot.getChildren();
                DetailValue value = new DetailValue();
                Set<String> timeList;
                for (DataSnapshot eachDetailValue : listOfDetailValue) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) eachDetailValue.getValue();
                    timeList = hashMap.keySet();

                    for (String time : timeList) {
                        String test = String.valueOf(hashMap.get(time));
                        Log.d("fetch", "success" + time + test);
                        value = new DetailValue(time, Integer.parseInt(test));
                        detailValueList.add(value);
                    }

                }
                dynamicLineChartManager.setEntryList(detailValueList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initChartData() {
        dynamicLineChartManager = new DynamicLineChartManager(lineChart_widget);
        dynamicLineChartManager.setYAxis(1100, 0, 100);
        anotherDateToShowChart = dateToShowChart.replace('_', '/');
        attachValueListenerToDateOnceAdvance();
        dynamicLineChartManager.setDescription("Data of " + anotherDateToShowChart);
    }

    private void  attachValueListenerToDateOnce() {
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        DetailValueEachDayListener detailValueEachDayListener = new DetailValueEachDayListener(dateToShowChart);
        detailValueEachDayListener.attachLineChart(dynamicLineChartManager);
        reference.addListenerForSingleValueEvent(detailValueEachDayListener);
    }

    private void  attachValueListenerToDateOnceAdvance() {
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        DetailValueEachDayListener detailValueEachDayListener = new DetailValueEachDayListener(dateToShowChart, timeToShowChart);
        detailValueEachDayListener.attachLineChart(dynamicLineChartManager);
        reference.addListenerForSingleValueEvent(detailValueEachDayListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.calendar:
                chooseDate();
                break;
            case R.id.clock:
                chooseTime();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseTime() {
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener setListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeToShowChart = hourOfDay + ":" + minute;
                initChartData();
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                DynamicLineChartActivity.this, setListener, hour, minute, false);
        timePickerDialog.show();
    }

    private void chooseDate() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                dateToShowChart = dayOfMonth + "_" + month;
                if (dateList.contains(dateToShowChart)) {
                    Log.d("status", "yes");
                    initChartData();
                } else {
                    Log.d("status", "no");
                    Toast.makeText(DynamicLineChartActivity.this, "Không có dữ liệu", Toast.LENGTH_LONG);
                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DynamicLineChartActivity.this, setListener, year, month, day);
        datePickerDialog.show();
    }
}