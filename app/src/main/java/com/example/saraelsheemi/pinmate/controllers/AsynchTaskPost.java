package com.example.saraelsheemi.pinmate.controllers;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Sara ElSheemi on 6/3/2018.
 */

public class AsynchTaskPost extends AsyncTask<String, Void, String> {


    // This is the JSON body of the post
    JSONObject postData;
    String result;
    String inputLine;

    public AsynchTaskPost(Map<String, String> postData) {
        if (postData != null) {
            this.postData = new JSONObject(postData);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            URL url = new URL(params[0]);
        // Create the connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "application/json");

        connection.setRequestMethod("POST");

        // OPTIONAL - Sets an authorization header
        connection.setRequestProperty("Authorization", "someAuthString");

        // Send the post body
        if (this.postData != null) {
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(postData.toString());
            writer.flush();
        }
        int statusCode = connection.getResponseCode();

        if (statusCode ==  200) {
            //Create a new InputStreamReader
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
        }
        else {
            // Status code is not 200
            // Do something to handle the error
        }
    } catch (Exception e) {
        Log.d("EXCEPTION", e.getLocalizedMessage());
    }
        return result;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
