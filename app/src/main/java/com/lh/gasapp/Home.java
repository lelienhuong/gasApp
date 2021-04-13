package com.lh.gasapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lh.gasapp.chart.DynamicLineChart;
import com.lh.gasapp.firebaseWrapper.MyValueEventListener;
import com.lh.gasapp.login.saveLogin;
import com.lh.gasapp.notification.Alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Home extends AppCompatActivity implements SensorValueDisplayer {
    private TextView tv_gas, tv_gasLevel, tv_peoplePresentStatus;
    private ProgressBar progressBar;
    private Button button_showChart;

    private String userId = "ERL43dCv3RXCMy8LRLzyIt6WtLH3";
    private DatabaseReference firebaseReference;
    private MyValueEventListener myValueEventListener;
    private saveLogin SaveSharedPreference;

    private ArrayList<String> formattedTimeList = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv_gas = findViewById(R.id.tv_gas);
        tv_gasLevel = findViewById(R.id.tv_gasLevel);
        tv_peoplePresentStatus = findViewById(R.id.tv_people);
        button_showChart = findViewById(R.id.button_showChart);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);

        myValueEventListener = new MyValueEventListener();
        myValueEventListener.attachValueDisplayer(this);

        //userId = FirebaseAuth.getInstance().getUid());
        firebaseReference = FirebaseDatabase.getInstance().getReference(userId);
        firebaseReference.addValueEventListener(myValueEventListener);


        button_showChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Success");
                showChart();
            }

            private void showChart() {
                Intent intent = new Intent(getApplicationContext(), DynamicLineChart.class);
                intent.putExtra("gas values", myValueEventListener.getGasValues());
                intent.putExtra("time list", formattedTimeList);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.logout:
                SaveSharedPreference.setUserName(this, "");
                startActivity(new Intent(this, MainActivity.class));
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void notifyGasValueChanged(int gasValue) {
        progressBar.setProgress((int) ((gasValue) / 1023 * 100));
        tv_gas.setText(String.valueOf(gasValue));
    }

    @Override
    public void notifyNewPointInTime() {
        formattedTimeList.add(simpleDateFormat.format(System.currentTimeMillis()));
    }

    @Override
    public void notifyGasStatusNotSafe() {
        tv_gasLevel.setText("Nguy hiểm");
        startAlarm();
    }

    private void startAlarm() {
        Intent alarmIntent = new Intent(getApplicationContext(), Alarm.class);
        startActivity(alarmIntent);
    }

    @Override
    public void notifyGasStatusSafe() {
        tv_gasLevel.setText("Bình thường");
    }

    @Override
    public void notifyHumanDetected() {
        tv_peoplePresentStatus.setText("Có người trong khu vực");
    }

    @Override
    public void notifyHumanNotDetected() {
        tv_peoplePresentStatus.setText("Không có người trong khu vực");

    }


}

