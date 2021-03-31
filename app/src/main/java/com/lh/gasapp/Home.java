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
    ArrayList<String> time = new ArrayList<>();
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
        oldData = intent.getDoubleExtra("oldData",-1);
        Log.d("OLDDATAAAAAAA", String.valueOf(oldData));
        //them truong hop luc chua co sẵn dữ lieu
        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RasData rasData = snapshot.getValue(RasData.class);
                int gasValue = (int) rasData.getGasData();
                gasValues.add(gasValue);
                time.add(df.format(System.currentTimeMillis()));
                Log.d("AddIntoList", "Success");
                if(oldData == -1 ) {
                    oldData = rasData.getGasData();
                    Log.d("ol", "ok");
                }else{
                    isCheck = true;
                }
                if(rasData.getGasData() != oldData){
                    isCheck = false;
                    oldData = rasData.getGasData();
                    progressBar.setProgress((int) oldData/1023*100);
                }

                if(rasData.getGasData() > 400 && isCheck == false){
                    Intent intent = new Intent(getApplicationContext(), com.lh.gasapp.Notification.class);
                    intent.putExtra("oldData", oldData);
                    startActivity(intent);
                    notificationDialog();
                }
                tv_gas.setText(String.valueOf(rasData.getGasData()));
                if(rasData.getGasData() > 400){
                    tv_level.setText("Nguy hiểm");
                }else{
                    tv_level.setText("Bình thường");
                }
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

