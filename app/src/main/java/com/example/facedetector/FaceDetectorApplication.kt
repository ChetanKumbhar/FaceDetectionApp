package com.example.facedetector;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class FaceDetectorApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
