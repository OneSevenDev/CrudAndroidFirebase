package com.begincodes.app.crudandroidfirebase.security;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by manuelguarniz on 16/11/16.
 */
public class CloudFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
