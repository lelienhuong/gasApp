package com.lh.gasapp;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.chart.DynamicLineChartActivity;
import com.lh.gasapp.firebase.FirebaseWrapper;
import com.lh.gasapp.firebase.valueEventListener.DetailValueEachDayListener;
import com.lh.gasapp.model.DetailValue;
import com.lh.gasapp.model.DynamicLineChartManager;

import java.util.ArrayList;
import java.util.Calendar;

public class History extends AppCompatActivity {

    ListView listView_date;
    ArrayList<String> dateList;
    private ArrayAdapter adapter;
    ArrayList<DetailValue> detailValueList ;

    private DynamicLineChartManager dynamicLineChartManager;
    private ArrayList<Integer> gasValues = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ValueEventListener lineChartValueListener;
    private LineChart lineChart_widget;

    private DatabaseReference firebaseReference;
    private String dateToShowChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Danh sách ngày");

        listView_date = findViewById(R.id.lv_date);
        dateList = new ArrayList<>();
        if (dateList.size() == 0)  {
            attachListenerToViewDaysFirstTime();
        } else {
            attachListenerToViewDays();;
        }
          dateToShowChart = "20_4";
//
        adapter = new ArrayAdapter(History.this,
                                    android.R.layout.simple_list_item_1,
                                    dateList);

        listView_date.setAdapter(adapter);

        listView_date.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showChart(position);
            }

            private void showChart(int position) {
                Intent intent = new Intent(getApplicationContext(), DynamicLineChartActivity.class);
                intent.putExtra("Date", dateList.get(position));
                startActivity(intent);
            }
        });

//        lineChart_widget = (LineChart) findViewById(R.id.dynamic_chart_history);
//
//        initChartData();
    }

    public void attachListenerToViewDaysFirstTime(){
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dateList.clear();
                Iterable<DataSnapshot> listOfDates = snapshot.getChildren();
                for (DataSnapshot eachDateAsKey : listOfDates){
                    dateList.add(eachDateAsKey.getKey());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void attachListenerToViewDays(){
        Query reference = FirebaseWrapper.getReferrence().child("gasValueHistory").limitToLast(1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dateList.add(snapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    Intent intent = new Intent(getApplicationContext(), DynamicLineChartActivity.class);
                    intent.putExtra("Date", dateToShowChart);
                    startActivity(intent);
                } else {
                    Log.d("status", "no");
                    Toast.makeText(History.this, "Không có dữ liệu", Toast.LENGTH_LONG);
                }
            }
        } ;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                History.this, setListener, year, month, day);
        datePickerDialog.show();
    }

    private void initChartData() {
        dynamicLineChartManager = new DynamicLineChartManager(lineChart_widget);
        dynamicLineChartManager.setYAxis(1100, 0, 100);
        attachValueListenerToDate();
        //dynamicLineChartManager.setDescription("Data of " + dateToShowChart);
    }

    private void attachValueListenerToDate(){
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        DetailValueEachDayListener detailValueEachDayListener = new DetailValueEachDayListener(dateToShowChart);
        detailValueEachDayListener.attachLineChart(dynamicLineChartManager);
        reference.addValueEventListener(detailValueEachDayListener);
    }

}

