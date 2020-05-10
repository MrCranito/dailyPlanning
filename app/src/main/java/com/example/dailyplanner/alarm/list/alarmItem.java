package com.example.dailyplanner.alarm.list;

import android.os.Bundle;

import com.example.dailyplanner.R;

import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class alarmItem implements Comparable<alarmItem> {


    public String title;
    public String[] recurrancy;
    public int repetitingTime;
    public boolean isActived;
    public long dateTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getRecurrancy() {
        return recurrancy;
    }

    public void setRecurrancy(String[] recurrancy) {
        this.recurrancy = recurrancy;
    }

    public int getRepetitingTime() {
        return repetitingTime;
    }

    public void setRepetitingTime(int repetitingTime) {
        this.repetitingTime = repetitingTime;
    }

    public boolean isActived() {
        return isActived;
    }

    public void setActived(boolean actived) {
        isActived = actived;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(alarmItem alarmItem) {
        
        int isBefore = 1;

        if(new Date(this.getDateTime()).getHours() <= new Date(alarmItem.getDateTime()).getHours()) {
            if(new Date(this.getDateTime()).getMinutes() <= new Date(alarmItem.getDateTime()).getMinutes()) {
                isBefore = 1;
            }
            isBefore = -1;
        } else {
            isBefore = 1;
        }
        
        return isBefore;
    }
}
