package com.example.dailyplanner.child.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dailyplanner.R;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class childListAdapter extends ArrayAdapter<childItemList>{

    public childListAdapter(Context context, List<childItemList> listItem) {
        super(context, 0, listItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_eventdetail,parent, false);
        }

        ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ItemViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.description);
            viewHolder.color = (View) convertView.findViewById(R.id.icon);
            viewHolder.dateTime = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.endDate = (TextView) convertView.findViewById(R.id.textView3);
            viewHolder.isLate = (ImageView) convertView.findViewById(R.id.isLate);
            viewHolder.register = (CheckBox) convertView.findViewById(R.id.registerEvent);
            convertView.setTag(viewHolder);
        }

        childItemList item = getItem(position);

        viewHolder.title.setText(item.getTitle());

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        viewHolder.color.setBackgroundColor(color);

        if(item.getLate()) {
            viewHolder.isLate.setVisibility(View.VISIBLE);
        }
/*
        if(item.getParentRegistered()) {
            viewHolder.register.setBackgroundResource(R.drawable.tick);
        }*/

        viewHolder.dateTime.setText(new Date(item.getDateTime()).getHours() + ":" + new Date(item.getDateTime()).getMinutes());
        viewHolder.endDate.setText(new Date(item.getDateTime() + item.getDuration()).getHours() + ":" + new Date(item.getDateTime()+ item.getDuration()).getMinutes());

        return convertView;
    }

    private class ItemViewHolder{
        public View color;

        public TextView title;

        public String status;

        public String recallDuration;

        public TextView dateTime;

        public int numberRecall;

        public String[] occurancy;

        public TextView endDate;

        public ImageView isLate;

        public CheckBox register;

    }
}