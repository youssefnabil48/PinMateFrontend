package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MResponse<T> {
    @SerializedName("ok")
    Boolean ok;
    ArrayList<T> data;
    @SerializedName("message")
    String message;
    @SerializedName("error")
    String error;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
