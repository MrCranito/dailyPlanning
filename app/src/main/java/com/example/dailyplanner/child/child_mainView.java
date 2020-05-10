package com.example.dailyplanner.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.dailyplanner.MainActivity;
import com.example.dailyplanner.R;
import com.example.dailyplanner.calendar.calendar_activity;
import com.example.dailyplanner.calendar.custom_view;
import com.example.dailyplanner.child.list.childItemList;
import com.example.dailyplanner.child.list.childListAdapter;
import com.example.dailyplanner.event.event;
import com.example.dailyplanner.helpers.IP;
import com.example.dailyplanner.helpers.JavaHelpers;
import com.example.dailyplanner.service.events_service;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

public class child_mainView extends AppCompatActivity {

    events_service eventService;
    ListView mListView;

    IP ip = new IP();

    FloatingActionButton goToCreateEvent;
    FloatingActionButton goBackToCalendar;

    ArrayList<childItemList> list = new ArrayList<childItemList>();
    Date rangeDate;
    List<childItemList> listItems = new ArrayList<childItemList>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_mainview);
        mListView = (ListView) findViewById(R.id.listView);

        goToCreateEvent = (FloatingActionButton) findViewById(R.id.addEvent);
        goBackToCalendar = (FloatingActionButton) findViewById(R.id.goBackToCalendar);


        long rangeDateAsLong = Long.valueOf(getIntent().getStringExtra("today"));

        rangeDate = new Date(rangeDateAsLong);

        afficherListItem();


        goToCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent myIntent = new Intent(child_mainView.this, event.class);
                child_mainView.this.startActivity(myIntent);
            };
        });

        goBackToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent myIntent = new Intent(child_mainView.this, custom_view.class);
                child_mainView.this.startActivity(myIntent);
            };
        });


        Collections.sort(listItems);

    }

    private List<childItemList> getEventsItem(){


        String item = null;
        try {
            item = new events_service().execute(ip.getAddress()+"/getEvents").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ArrayList<Object> p = gson.fromJson(item, ArrayList.class);

        for (Object o : p) {
            String objectAsString = gson.toJson(o);
            childItemList itemList = gson.fromJson(objectAsString, childItemList.class);

            if(
                    rangeDate.getYear() == new Date(itemList.getDateTime()).getYear() &&
                    rangeDate.getMonth() == new Date(itemList.getDateTime()).getMonth() &&
                    rangeDate.getDay() == new Date(itemList.getDateTime()).getDay()
            ) {
                listItems.add(itemList);
            }
        }

        if(listItems.size() < 1 ) {
            childItemList newItem = new childItemList("Aucun Evenement pour cette journée", "none", 0, 0, 0, false, false);
            listItems.add(newItem);
        }

        return listItems;
    }

    boolean isWithinRange(Date testDate) {
        long before = rangeDate.getTime() - 86400000;
        long after = rangeDate.getTime() + 86400000;
        return !(testDate.before(new Date(before)) || testDate.after(new Date(after)));
    }

    private void afficherListItem(){
        List<childItemList> listItem = getEventsItem();

        childListAdapter adapter = new childListAdapter(child_mainView.this, listItem);
        mListView.setAdapter(adapter);
    }

}
