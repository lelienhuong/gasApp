package com.lh.gasapp.chart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

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
    private String anotherDateToShowChart;
    private ArrayList<String> dateList = new ArrayList<>();

    private ImageView iconBack;
    private ImageView iconCalendar;

    DatePickerDialog.OnDateSetListener setListener;
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
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dateList.clear();
                Iterable<DataSnapshot> listOfDates = snapshot.getChildren();
                for (DataSnapshot eachDateAsKey : listOfDates) {
                    dateList.add(eachDateAsKey.getKey());
                }
                if (kt && dateList.size() > 0){
                    dateToShowChart = dateList.get(dateList.size()-1);
                    initChartData();
                    kt = false;
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
        anotherDateToShowChart = dateToShowChart.replace('_', '/');
        attachValueListenerToDate();
        dynamicLineChartManager.setDescription("Data of " + anotherDateToShowChart);
    }

//    private void getDataFromIncomingIntent() {
//        Intent intent = getIntent();
//        dateToShowChart = intent.getStringExtra("Date");
//        Log.d("DATE", dateToShowChart);
//        attachValueListenerToDate();
//    }

    private void attachValueListenerToDate(){
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        DetailValueEachDayListener detailValueEachDayListener = new DetailValueEachDayListener(dateToShowChart);
        detailValueEachDayListener.attachLineChart(dynamicLineChartManager);
        reference.addValueEventListener(detailValueEachDayListener);
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
        }
        return super.onOptionsItemSelected(item);
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