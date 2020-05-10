package com.example.dailyplanner.child.list;

import android.graphics.Color;

import java.security.Timestamp;
import java.util.Date;
import java.util.Random;

public class childItemList implements Comparable<childItemList> {
    public childItemList(String title, String status, int recallDuration, long dateTime, int numberRecall, Boolean isLate, Boolean isParentRegistered) {
        this.color = color;
        this.title = title;
        this.status = status;
        this.recallDuration = recallDuration;
        this.dateTime = dateTime;
        this.numberRecall = numberRecall;
        this.occurancy = occurancy;
        this.duration = duration;
        this.isLate = isLate;
        this.isParentRegistered = isParentRegistered;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRecallDuration() {
        return recallDuration;
    }

    public void setRecallDuration(int recallDuration) {
        this.recallDuration = recallDuration;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) { System.out.println(dateTime); this.dateTime = dateTime;
    }

    public int getNumberRecall() {
        return numberRecall;
    }

    public void setNumberRecall(int numberRecall) {
        this.numberRecall = numberRecall;
    }

    public String[] getOccurancy() {
        return occurancy;
    }

    public void setOccurancy(String[] occurancy) {
        this.occurancy = occurancy;
    }

    public int getColor() {

        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int color;

    public String title;

    public String status;

    public int recallDuration;

    public long dateTime;

    public int numberRecall;

    public String[] occurancy;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int duration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

    public Boolean isLate;

    public Boolean getLate() {
        return isLate;
    }

    public void setLate(Boolean late) {
        isLate = late;
    }

    public Boolean getParentRegistered() {
        return isParentRegistered;
    }

    public void setParentRegistered(Boolean parentRegistered) {
        isParentRegistered = parentRegistered;
    }

    public Boolean isParentRegistered;

    @Override
    public int compareTo(childItemList childItemList) {

        if (new Date(this.getDateTime()).before(new Date(childItemList.getDateTime()))) {
            return -1;
        }
        else {
            return 1;
        }

    }
}
