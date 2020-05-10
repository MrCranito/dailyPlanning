package com.example.dailyplanner.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.dailyplanner.R;
import com.example.dailyplanner.child.child_mainView;
import com.example.dailyplanner.event.event;
import com.example.dailyplanner.helpers.BroadcastRecever;
import com.example.dailyplanner.service.update_event;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

public class alarm extends AppCompatActivity {

    TextInputEditText title;

    TimePicker timePicker;

    TextInputEditText numberRappel;

    TextInputEditText dateField;

    Button save;

    Spinner dropDownRecurrancy;

    String timePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addalarm);

        title = (TextInputEditText) findViewById(R.id.setTitleAlarm);

        timePicker = (TimePicker) findViewById(R.id.pickerTimeAlarm);

        numberRappel = (TextInputEditText) findViewById(R.id.recallDurationAlarm);

        dateField = (TextInputEditText) findViewById(R.id.setAlarmStartDate);

        save = (Button) findViewById(R.id.saveAlarm);

        dropDownRecurrancy = (Spinner) findViewById(R.id.spinnerAlarm);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                dateField.setText(""+hourOfDay+":"+minute+"");
                timePicker.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });

        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                timePicker.setVisibility(View.VISIBLE);
                timePicker.bringToFront();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String item = null;
                try {
                    long actualDate = new Date().getTime();
                    String[] hours_min = dateField.getText().toString().split(":");
                    long hours = (Integer.parseInt(hours_min[0]) * 3600 + Integer.parseInt(hours_min[1]) * 60) * 1000;
                    long concat = actualDate + hours;
                    item = new update_event().execute(
                            "http://192.168.1.46:3000/addAlarm",
                            "title:" + title.getText() + "",
                            "recurrancy:" + "coucou,ici"+ "",
                            "repetitingTime:"+ 10,
                            "isActived:" + false,
                            "dateTime:" + concat
                    ).get();

                    startAlert();
                    Intent myIntent = new Intent(alarm.this, alarm_mainView.class);
                    alarm.this.startActivity(myIntent);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

}
