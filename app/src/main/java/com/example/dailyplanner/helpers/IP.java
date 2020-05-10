package com.example.dailyplanner.helpers;

public class IP {

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address;
    public IP() {
         address = "http://172.20.10.6:3000";
    }

}
