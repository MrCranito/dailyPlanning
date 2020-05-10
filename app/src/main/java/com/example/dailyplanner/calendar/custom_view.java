package com.example.dailyplanner.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyplanner.MainActivity;
import com.example.dailyplanner.R;
import com.example.dailyplanner.child.child_mainView;
import com.example.dailyplanner.child.list.childItemList;
import com.example.dailyplanner.child.list.childListAdapter;
import com.example.dailyplanner.service.events_service;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

public class custom_view extends AppCompatActivity
{
    View wrapperList;


    List<childItemList> listItems = new ArrayList<childItemList>();

    ListView mListView;

    TextView dateToday;

    private static final String DATE_FORMAT = "dd MM yyyy";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_calendar);

        HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        calendar_activity cv = ((calendar_activity)findViewById(R.id.calendar_view));

        mListView = (ListView) findViewById(R.id.eventToday);

        dateToday = (TextView) findViewById(R.id.todayDate);

        Intent myIntent = new Intent(custom_view.this, child_mainView.class);
        myIntent.putExtra("today", new String().valueOf(new Date().getTime()));

        Date date = new Date();

        dateToday.setText("Le " + DateFormat.getDateInstance(DateFormat.LONG).format(date));

        cv.updateCalendar(events);

        // assign event handler
        cv.setEventHandler(new calendar_activity.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(custom_view.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDayShortPress(Date date) {
                Intent myIntent = new Intent(custom_view.this, child_mainView.class);
                myIntent.putExtra("today", new String().valueOf(date.getTime()));
                custom_view.this.startActivity(myIntent);
            }


        });

        afficherListItem();
    }


    private List<childItemList> getEventsItem(){


        String item = null;
        try {
            item = new events_service().execute("http://192.168.1.46:3000/getEvents").get();
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

            listItems.add(itemList);
        }

        if(listItems.size() < 1 ) {
            childItemList newItem = new childItemList("Aucun Evenement pour cette journée", "none", 0, 0, 0, false, false);
            listItems.add(newItem);
        }

        return listItems;
    }

    private void afficherListItem(){
        List<childItemList> listItem = getEventsItem();

        childListAdapter adapter = new childListAdapter(custom_view.this, listItem);
        mListView.setAdapter(adapter);
    }

}
