package com.lh.gasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.login.saveLogin;
import com.lh.gasapp.model.RasData;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Home extends AppCompatActivity {
    public static boolean isCheck = false;
    public TextView tv_gas,tv_level,tv_people,tv_status;
    private double oldData = -1;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private saveLogin SaveSharedPreference;
    ProgressBar progressBar;
    Button btnChart;
    ArrayList<Integer> gasValues = new ArrayList<>();
    ArrayList<Integer> analysisValuesArrays = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    public int tong = 0;
    private boolean changed;
    private long time1 = 0, time2 = 0,previous;
    private boolean saved;
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv_gas = findViewById(R.id.tv_gas);
        tv_level = findViewById(R.id.tv_level);
        tv_people = findViewById(R.id.tv_people);
        tv_status = findViewById(R.id.tv_status);
        btnChart = findViewById(R.id.btn_chart);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        Intent intent= getIntent();
        previous = intent.getLongExtra("previous",0);
        oldData = intent.getDoubleExtra("oldData",-1);
        Log.d("OLDDATAAAAAAA", String.valueOf(oldData));
        //them truong hop luc chua co sẵn dữ lieu

        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RasData rasData = snapshot.getValue(RasData.class);
                double value = rasData.getGasData();
//                Log.d("AddIntoList", String.valueOf((value/1023)*100));
                progressBar.setProgress((int) ((value)/1023*100));

//                progressBar.setProgress((int) ((rasData.getGasData())/1023*100));
                time.add(df.format(System.currentTimeMillis()));

                if(time1 == 0) {
                    time1 = System.currentTimeMillis();
                    time2 = 0;
                }else{
                    time2 = System.currentTimeMillis();
                }
                //else{
//                    isCheck = true;
//                }
//                if(rasData.getGasData() != oldData){
//                    isCheck = false;
//                    oldData = rasData.getGasData();
//                    progressBar.setProgress((int) oldData/1023*100);
//                }
//                if(rasData.getGasData() > 400 && isCheck == false){
//                    Intent intent = new Intent(getApplicationContext(), com.lh.gasapp.Notification.class);
//                    intent.putExtra("oldData", oldData);
//                    startActivity(intent);
//                    notificationDialog();
//                }
                tv_gas.setText(String.valueOf(rasData.getGasData()));
                if(rasData.getGasData() > 400){
                    tv_level.setText("Nguy hiểm");
                }else{
                    tv_level.setText("Bình thường");
                }
//                tv_level.setText("Bình thường");
//                if(rasData.getGasData() > 400){
//                    tv_level.setText("Nguy hiểm");
//                }else{
//                    tv_level.setText("Bình thường");
//                }
                if(rasData.isPeople()){ //true la co nguoi
                    tv_people.setText("Có người trong khu vực");
                }else{
                    tv_people.setText("Không có người trong khu vực");
                }
                if(rasData.isStatusHuman()){ // true là binh thuong, false la co van de
                    tv_status.setText("Bình thường");
                }else{
                    tv_status.setText("Không bình thường");
                }
//                if(oldData == -1 ) {
//                    oldData = rasData.getGasData();
//                    Log.d("ol", "ok");
//                }
                Log.d("OLDDATAAAAAAA", String.valueOf(rasData.getGasData()));
                int gasValue = (int) rasData.getGasData();

                if(gasValues.size() < 3 && (time2 == 0 ||  (time2 - time1) < 10000)) { // chọn khung là có 10 giá trị và thời gian thay đổi giữa 2 giá trị phải bé hơn 10s nếu ko thì sẽ được tính là không có sự biến đổi đột ngột
                   Log.d("ACCEPT","ACCCEPT");
                    if(time2 != 0){
                       time1 = time2;
                   }
                    // time1 = 0;
                    Log.d("Value", String.valueOf(gasValue));
                    tong = tong + gasValue;
                    gasValues.add(gasValue);
                    if(gasValues.size() == 5){ //10 giá trị
                        tong = tong/5; //chia bao nhieu gia tri
                        analysisValuesArrays.add(tong);
                        gasValues.clear();
                        tong = 0;
                        if(analysisValuesArrays.size() == 5){ // đủ 3 khung
                            Log.d("GIO", String.valueOf(System.currentTimeMillis()-time1));
                            for (int i = 0; i< analysisValuesArrays.size() - 1; i++){
                                if(analysisValuesArrays.get(i) < analysisValuesArrays.get(i+1)){
                                    continue;
                                }else{
                                    analysisValuesArrays.clear();
                                    break;
                                }
                            }
                            if(analysisValuesArrays.size() == 0){
                                tv_level.setText("Bình thường");
                            }else{
                                tv_level.setText("Nguy hiểm");
                                analysisValuesArrays.clear();
                                if(previous == 0 || System.currentTimeMillis() - previous > 30000 ) {
                                    Intent intent = new Intent(getApplicationContext(), com.lh.gasapp.Notification.class);
                                    intent.putExtra("oldData", oldData);
                                    startActivity(intent);
                                    notificationDialog();
                                }
                            }
                        }
                    }
                }else{
                    analysisValuesArrays.clear();
                    gasValues.clear();
                    gasValues.add(gasValue);
                    tong = 0;
                    tong = tong + gasValue;
                    Log.d("DENIED","ACCCEPT");
                    time1 = time2;
                }


//                int i = 1;
//                while(i <= 10){
//                    int j = 1;
//                    while (j <= 2){
//                        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                RasData rasData = snapshot.getValue(RasData.class);
//                                //Log.d("LIENHUONG", "huongggggg");
//                                Log.d("LIENHUONG", String.valueOf(rasData.getGasData()));
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                        j = j + 1;
//                    }
//                    i = i + 1;
////                    if(i == 10){
////                        i = 1;
////                    }
////                    sapXep();
////                    i = i + 1;
////                    if(i == 10){
////                        i = 1;
////                    }
//                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Success");
                Intent intent = new Intent(getApplicationContext(), DynamicLineChart.class);
                intent.putExtra("gas values", gasValues);
                intent.putExtra("time list", time);
                startActivity(intent);
            }
        });
    }
    public void sapXep(){
            CountDownTimer timer = new CountDownTimer(2000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("LIENHUONG", "huongggggg");
                }

                @Override
                public void onFinish() {
                }
            };
            timer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.logout:
                SaveSharedPreference.setUserName(this, "");
                startActivity(new Intent(this, MainActivity.class));
                //code xử lý khi bấm menu1
                break;
//            case R.id.menu2:
//                //code xử lý khi bấm menu2
//                break;
//            case R.id.menu3:
//                //code xử lý khi bấm menu3
//                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("WARNING GAS LEAK!")
                .setContentText("Khí gas ở mức NGUY HIỂM ! ")
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}

