package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.saraelsheemi.pinmate.models.MResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AsynchTaskPost extends AsyncTask<String, Void, String> {
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String jsonData;
    private EventListener<String> mCallBack;
    private Context mContext;
    public Exception mException;


    public AsynchTaskPost(String jsonData, Context mContext, EventListener mCallBack) {
        this.jsonData = jsonData;
        this.mContext = mContext;
        this.mCallBack = mCallBack;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(strings[0]);
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, jsonData);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Log.e("response status",String.valueOf(response.code()));
        //   Log.e("response status",response.body().toString());

//            Gson gson = new Gson();
//            MResponse mResponse = gson.fromJson(response.body().string(),MResponse.class);
//            Log.e("mresponse",mResponse.getMessage());

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            mException = e;
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String response) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(response);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }

}
