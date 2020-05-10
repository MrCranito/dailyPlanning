package com.example.dailyplanner.service;

import android.os.AsyncTask;

import com.example.dailyplanner.helpers.HttpUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class update_event extends AsyncTask<String, Integer, String> {
    String server_response;

    @Override
    protected String doInBackground(String... strings) {

        final String[] responseServer = {new String()};
        HashMap<String, String> params = new HashMap<String, String>();

        for(String param : strings) {
            if(param != strings[0]) {
                params.put(param.split(":")[0], param.split(":")[1]);
            }
        }

        //server url
        String url = strings[0];

        // static class "HttpUtility" with static method "newRequest(url,method,callback)"
        HttpUtility.newRequest(url,HttpUtility.METHOD_POST,params, new HttpUtility.Callback() {
            @Override
            public void OnSuccess(String response) {
                // on success
                responseServer[0] = response;
                System.out.println("Server OnSuccess response="+response);
            }
            @Override
            public void OnError(int status_code, String message) {
                // on error
                System.out.println("Server OnError status_code="+status_code+" message="+message);
            }
        });

        return responseServer[0];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}



