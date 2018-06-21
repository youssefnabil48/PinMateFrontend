package com.example.saraelsheemi.pinmate.controllers;

public interface EventListener<T> {
     void onSuccess(T object);
     void onFailure(Exception e);
}
