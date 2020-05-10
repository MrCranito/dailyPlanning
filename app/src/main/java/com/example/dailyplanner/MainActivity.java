package com.example.dailyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dailyplanner.alarm.alarm_mainView;
import com.example.dailyplanner.child.child_mainView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    Button goToChild;

    Button goToParent;

    Button goToAlarms;

    TextView todaydate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToChild = (Button)findViewById(R.id.goToChildView);

        goToParent = (Button) findViewById(R.id.goToParentView);

        goToAlarms = (Button) findViewById(R.id.alarm);

        todaydate = (TextView) findViewById(R.id.todaydate);

        goToChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, child_mainView.class);
                myIntent.putExtra("today", new String().valueOf(new Date().getTime()));
                MainActivity.this.startActivity(myIntent);
            }
        });

        goToAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, alarm_mainView.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        Date date = new Date();
        String stringDate = DateFormat.getDateInstance().format(date);

        todaydate.setText(stringDate);
    }



}
