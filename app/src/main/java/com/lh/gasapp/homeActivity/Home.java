package com.lh.gasapp.homeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.lh.gasapp.MainActivity;
import com.lh.gasapp.R;
import com.lh.gasapp.SensorValueDisplayer;
import com.lh.gasapp.firebase.FirebaseWrapper;
import com.lh.gasapp.firebase.valueEventListener.MyValueEventListener;
import com.lh.gasapp.login.saveLogin;
import com.lh.gasapp.notification.Alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Home extends AppCompatActivity implements SensorValueDisplayer {
    private TextView tv_gas, tv_gasLevel, tv_peoplePresentStatus;
    private ProgressBar progressBar;
    private Button button_showChart;

    private DatabaseReference firebaseReference;

    protected MyValueEventListener myValueEventListener;
    private saveLogin SaveSharedPreference;

    protected ArrayList<String> timeList = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private ButtonActionListener buttonListener_showChartOnClick;
    private boolean isRunningAlarm = true;
    public ArrayList<Integer> gasValues = new ArrayList<Integer>();
    public long previousTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gasValues = getIntent().getIntegerArrayListExtra("gasValues");
        isRunningAlarm = getIntent().getBooleanExtra("stateRunningAlarm",true);
        previousTime = getIntent().getLongExtra("previousTime",-1);

        tv_gas = findViewById(R.id.tv_gas);
        tv_gasLevel = findViewById(R.id.tv_gasLevel);
        tv_peoplePresentStatus = findViewById(R.id.tv_people);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);

        initFireBaseListener();
        initButton();
    }

    private void permissionToAlarm() {
        if(previousTime != -1){
            long currentTime = System.currentTimeMillis();
            long time_since_alarm_closed_before = currentTime - previousTime;
            if(time_since_alarm_closed_before > 60000){
                isRunningAlarm = true;
            }
        }
    }

    private void initFireBaseListener() {
        myValueEventListener = new MyValueEventListener();
        myValueEventListener.attachValueDisplayer(this);

        firebaseReference = FirebaseWrapper.getReferrence();
        firebaseReference.addValueEventListener(myValueEventListener);
    }

    private void initButton() {
        button_showChart = findViewById(R.id.button_showChart);

        buttonListener_showChartOnClick = new ButtonActionListener(this);
        button_showChart.setOnClickListener(buttonListener_showChartOnClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }


    @Override
    public void notifyGasValueChanged(double gasValue) {
        progressBar.setProgress((int) ((gasValue) / 1023 * 100));
        tv_gas.setText(String.valueOf((int)gasValue));
    }

    @Override
    public void notifyNewPointInTime() {
        timeList.add(simpleDateFormat.format(System.currentTimeMillis()));
    }

    @Override
    public void notifyGasStatusNotSafe() {
        tv_gasLevel.setText("Nguy hiểm");
    }

    public void startAlarm() {
        permissionToAlarm();
        if(isRunningAlarm) {
            isRunningAlarm = false;
            Intent alarmIntent = new Intent(getApplicationContext(), Alarm.class);
            alarmIntent.putExtra("stateRunningAlarm", isRunningAlarm);
            startActivity(alarmIntent);
        }
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

}

