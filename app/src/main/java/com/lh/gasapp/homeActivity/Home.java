package com.lh.gasapp.homeActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.lh.gasapp.ChangeHelperPhone;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.History;
import com.lh.gasapp.MainActivity;
import com.lh.gasapp.R;
import com.lh.gasapp.SensorValueDisplayer;
import com.lh.gasapp.StreamCamera;
import com.lh.gasapp.chart.DynamicLineChartActivity;
import com.lh.gasapp.firebase.FirebaseWrapper;
import com.lh.gasapp.firebase.valueEventListener.MyValueEventListener;
import com.lh.gasapp.login.saveLogin;
import com.lh.gasapp.model.SensorData;
import com.lh.gasapp.notification.Alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Home extends AppCompatActivity implements SensorValueDisplayer {
    public static Home instance = null;
    private TextView tv_gas, tv_gasLevel, tv_peoplePresentStatus;
    private ProgressBar progressBar;
    private Button button_showChart;
    private Button button_showCamera;
    private Button button_showHistory;

    private DatabaseReference firebaseReference;

    protected MyValueEventListener myValueEventListener;
    private saveLogin SaveSharedPreference;

    protected ArrayList<String> timeList = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private ButtonActionListener buttonListener_showChartOnClick;
    private boolean isRunningAlarm = true;
    public ArrayList<Integer> gasValues = new ArrayList<Integer>();
    public long previousTime;

    ArrayList<String> datelist = new ArrayList<>();
    String dateToShowChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = this;
        gasValues = getIntent().getIntegerArrayListExtra("gasValues");
        isRunningAlarm = getIntent().getBooleanExtra("stateRunningAlarm",true);
        previousTime = getIntent().getLongExtra("previousTime",-1);

        tv_gas = findViewById(R.id.tv_gas);
        tv_gasLevel = findViewById(R.id.tv_gasLevel);
        tv_peoplePresentStatus = findViewById(R.id.tv_people);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);

        attachListenerToViewDaysFirstTime();
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
//        else{
//            isRunningAlarm = true;
//        } bỏ tại vì k cần thiết
    }

    private void initFireBaseListener() {
        myValueEventListener = new MyValueEventListener();
        myValueEventListener.attachValueDisplayer(this);

        firebaseReference = FirebaseWrapper.getReferrence();
        firebaseReference.addValueEventListener(myValueEventListener);
    }

    private void initButton() {
//        button_showChart = findViewById(R.id.button_showChart);

//        buttonListener_showChartOnClick = new ButtonActionListener(this);
//        button_showChart.setOnClickListener(buttonListener_showChartOnClick);

//        button_showCamera = findViewById(R.id.button_showCamera);
//        button_showCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Home.this, StreamCamera.class);
//                startActivity(intent);
//            }
//        });

        button_showHistory = findViewById(R.id.button_showHistory);
        button_showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, DynamicLineChartActivity.class);
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

    public void startAlarm(SensorData sensorData) {
        sensorData.setBellOnRequired(true);
        permissionToAlarm();
        if(isRunningAlarm) {
            try {
                Alarm.instance.finish();
            }catch (Exception e){
                Log.d("NULL","null");
            }
//            finishActivity(111);
            isRunningAlarm = false;
            Intent alarmIntent = new Intent(getApplicationContext(), Alarm.class);
            alarmIntent.putExtra("stateRunningAlarm", isRunningAlarm);
            alarmIntent.putExtra("statePeople",sensorData.isPeople());
            startActivity(alarmIntent);
//              finish();
        }
    }

    @Override
    public void notifyGasStatusSafe() {
        tv_gasLevel.setText("An toàn");
    }

    @Override
    public void notifyHumanDetected() {
        tv_peoplePresentStatus.setText("Có người");
    }

    @Override
    public void notifyHumanNotDetected() {
        tv_peoplePresentStatus.setText("Không có người");
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
            case R.id.phoneHelper:
                startActivity(new Intent(this, ChangeHelperPhone.class));
                break;
            default:
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
                if (datelist.contains(dateToShowChart)) {
                    Log.d("status", "yes");
                    Intent intent = new Intent(getApplicationContext(), DynamicLineChartActivity.class);
                    intent.putExtra("Date", dateToShowChart);
                    startActivity(intent);
                } else {
                    Log.d("status", "no");
                    Toast.makeText(Home.this, "Không có dữ liệu", Toast.LENGTH_LONG);
                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Home.this, setListener, year, month, day);
        datePickerDialog.show();
    }

    public void attachListenerToViewDaysFirstTime(){
        DatabaseReference reference = FirebaseWrapper.getReferrence().child("gasValueHistory");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datelist.clear();
                Iterable<DataSnapshot> listOfDates = snapshot.getChildren();
                for (DataSnapshot eachDateAsKey : listOfDates){
                    datelist.add(eachDateAsKey.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

