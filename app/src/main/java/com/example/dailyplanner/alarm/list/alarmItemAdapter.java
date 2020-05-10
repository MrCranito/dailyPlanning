package com.example.dailyplanner.alarm.list;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dailyplanner.R;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;

public class alarmItemAdapter extends ArrayAdapter<alarmItem> {

    public alarmItemAdapter(Context context, List<alarmItem> listItem) {
        super(context, 0, listItem);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_row,parent, false);
        }

        alarmItemAdapter.AlarmViewHolder viewHolder = (alarmItemAdapter.AlarmViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new alarmItemAdapter.AlarmViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.descriptionAlarm);
            viewHolder.color = (View) convertView.findViewById(R.id.iconAlarm);
            viewHolder.date = (TextView) convertView.findViewById(R.id.alarmDate);
            viewHolder.recurrancy = (TextView) convertView.findViewById(R.id.recurrancyAlarm);
            viewHolder.isActived = (Switch) convertView.findViewById(R.id.isActived);
            convertView.setTag(viewHolder);
        }

        alarmItem item = getItem(position);

        viewHolder.title.setText(item.getTitle());

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        viewHolder.color.setBackgroundColor(color);

        viewHolder.date.setText(""+new Date(item.getDateTime()).getHours()+":"+new Date(item.getDateTime()).getMinutes());


        StringBuilder builder = new StringBuilder();

        int index = 0;
        for(String s : item.getRecurrancy()) {
            if(item.getRecurrancy().length - 1 > index) {
                builder.append(s + ", ");
                index++;
            } else {
                builder.append(s);
            }
        }
        String recurrancyAsString = builder.toString();


        viewHolder.recurrancy.setText(recurrancyAsString);

        viewHolder.isActived.setChecked(item.isActived());

        return convertView;
    }

    private class AlarmViewHolder{
        public View color;

        public TextView title;

        public TextView recurrancy;

        public TextView date;

        public Switch isActived;

    }
}