package com.lh.gasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.chart.DynamicLineChartActivity;
import com.lh.gasapp.firebase.FirebaseWrapper;
import com.lh.gasapp.model.DetailValue;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    ListView listView_date;
    ArrayList<String> dateList;
    private ArrayAdapter adapter;
    ArrayList<DetailValue> detailValueList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView_date = findViewById(R.id.lv_date);
        dateList = new ArrayList<>();
        attachListenerToViewDays();

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
    }

    public void attachListenerToViewDays(){
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
}