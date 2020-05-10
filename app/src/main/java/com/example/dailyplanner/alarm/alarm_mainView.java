package com.example.dailyplanner.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.dailyplanner.R;
import com.example.dailyplanner.alarm.list.alarmItem;
import com.example.dailyplanner.alarm.list.alarmItemAdapter;
import com.example.dailyplanner.child.child_mainView;
import com.example.dailyplanner.child.list.childItemList;
import com.example.dailyplanner.child.list.childListAdapter;
import com.example.dailyplanner.event.event;
import com.example.dailyplanner.service.events_service;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

public class alarm_mainView extends AppCompatActivity {


    List<alarmItem> alarmList = new ArrayList<alarmItem>();

    ListView alarmListView;

    FloatingActionButton addAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout);

        alarmListView = (ListView) findViewById(R.id.alarmListView);

        addAlarm = (FloatingActionButton) findViewById(R.id.addAlarm);


        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent myIntent = new Intent(alarm_mainView.this, alarm.class);
                alarm_mainView.this.startActivity(myIntent);
            }
        });

        alarmList = getAlarmsItem();

        afficherListItem();
        Collections.sort(alarmList);


    }

    private List<alarmItem> getAlarmsItem(){

        String item = null;
        try {
            item = new events_service().execute("http://192.168.1.46:3000/getAlarms").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ArrayList<Object> p = gson.fromJson(item, ArrayList.class);

        for (Object o : p) {
            String objectAsString = gson.toJson(o);
            alarmItem itemList = gson.fromJson(objectAsString, alarmItem.class);
            alarmList.add(itemList);
        }

        return alarmList;
    }

    private void afficherListItem(){
        alarmItemAdapter adapter = new alarmItemAdapter(alarm_mainView.this, alarmList);
        alarmListView.setAdapter(adapter);
    }
}
