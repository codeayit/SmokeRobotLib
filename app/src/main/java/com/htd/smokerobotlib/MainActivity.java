package com.htd.smokerobotlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codeayit.devicelib.MbManager;

import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            MbManager.getInstance().init(this,1,"",9600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
