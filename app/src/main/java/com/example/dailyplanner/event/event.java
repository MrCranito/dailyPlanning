package com.example.dailyplanner.event;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dailyplanner.MainActivity;
import com.example.dailyplanner.R;
import com.example.dailyplanner.child.child_mainView;
import com.example.dailyplanner.child.list.childItemList;
import com.example.dailyplanner.helpers.BroadcastRecever;
import com.example.dailyplanner.service.events_service;
import com.example.dailyplanner.service.update_event;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

public class event extends AppCompatActivity {


    EditText title;
    Button startDateDay;
    EditText startDateHours;
    EditText duration;
    EditText recallDuration;
    EditText fieldDate;

    DatePicker startDate;

    TimePicker timePicker;


    Button save;

    update_event eventService;
    private Calendar cal;

    List<childItemList> listItems = new ArrayList<childItemList>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent);

        title = (EditText) findViewById(R.id.title);
        startDateHours = (EditText) findViewById(R.id.startDate);
        startDateHours.setInputType(InputType.TYPE_NULL);
        duration = (EditText) findViewById(R.id.duration);
        recallDuration = (EditText)findViewById(R.id.recallDuration);
        save = (Button) findViewById(R.id.save);
        startDate = (DatePicker) findViewById(R.id.pickerDayView);
        fieldDate = (EditText) findViewById(R.id.fieldDate);
        fieldDate.setInputType(InputType.TYPE_NULL);
        timePicker = (TimePicker) findViewById(R.id.pickerTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        startDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                System.out.println(month);
                month = month + 1;
                fieldDate.setText(""+month+"/"+dayOfMonth+"/"+year);
                fieldDate.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                startDate.setVisibility(View.INVISIBLE);
            }
        });


        listItems = getEventsItem();

        final AlertDialog.Builder dialog = new AlertDialog.Builder(event.this);
        dialog.setCancelable(false);
        dialog.setTitle("Erreur");
        dialog.setMessage("Vous avez déjà un événement de programmé durant cette période" );
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String item = null;
                try {

                    long date = new Date(fieldDate.getText().toString()).getTime();
                    System.out.println(date);
                    String[] hours_min = startDateHours.getText().toString().split(":");
                    long hours = (Integer.parseInt(hours_min[0]) * 3600 + Integer.parseInt(hours_min[1]) * 60) * 1000;
                    long concat = date + hours;
                    Integer durationInt = Integer.parseInt(duration.getText().toString()) * 60000;

                    if(!isAlreayEventInRange(concat, durationInt)) {
                        item = new update_event().execute(
                                "http://192.168.1.46:3000/addEvent",
                                "title:" + title.getText() + "",
                                "dateTime:" + concat + "",
                                "duration:" + durationInt + "",
                                "recallDuration:" + Integer.parseInt(recallDuration.getText().toString()) + "",
                                "numberRecall:" + Integer.parseInt("0"),
                                "status:notSarted",
                                "isLate:"+false,
                                "isParentRegistered:"+false
                        ).get();

                        startAlert();

                        Intent myIntent = new Intent(event.this, child_mainView.class);
                        myIntent.putExtra("today", new String().valueOf(new Date(concat).getTime()));
                        event.this.startActivity(myIntent);
                    } else {
                        dialog.show();
                    }


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        fieldDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                save.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.VISIBLE);
                startDate.bringToFront();
            }
        });

        startDateHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                save.setVisibility(View.INVISIBLE);

                timePicker.setVisibility(View.VISIBLE);
                timePicker.bringToFront();
            }
        });


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                startDateHours.setText(""+hourOfDay+":"+minute+"");
                timePicker.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });


        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                duration.setText("");
            }
        });

        recallDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                recallDuration.setText("");
            }
        });

    }

    public void startAlert() {
        Intent intent = new Intent(this, BroadcastRecever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000), pendingIntent);
        Toast.makeText(this, "Alarm set in 1 seconds", Toast.LENGTH_LONG).show();
    }


    private boolean isAlreayEventInRange(long concat, long durationInt) {
        Boolean isInRange = false;
        long before = 0;
        long after = 0;

        for(childItemList itemList : listItems) {
            System.out.println("coucou");
            before = itemList.getDateTime();
            after = itemList.getDateTime() + itemList.getDuration();

            System.out.println(isWithinRange(new Date(concat), before, after));
            System.out.println(isWithinRange(new Date(concat + durationInt), before, after));

            if(isWithinRange(new Date(before), concat, concat + durationInt) || isWithinRange(new Date(after), concat, concat + durationInt)) {
                System.out.println(""+concat+" "+before+ " "+after+"");
                System.out.print("here");
                isInRange = true;
            }
        }

        return isInRange;
    }

    boolean isWithinRange(Date testDate, long before, long after) {
        return !(testDate.before(new Date(before)) || testDate.after(new Date(after)));
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
}
