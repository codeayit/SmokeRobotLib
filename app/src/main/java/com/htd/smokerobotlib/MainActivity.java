package com.htd.smokerobotlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
            MbManager.getInstance().loadLibrary();
            MbManager.getInstance().init(this,1,"/dev/ttyS2",115200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void write(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {

//               MbManager.getInstance().write_registers(50,3);
                MbManager.getInstance().write_registers32(50,1245224);

            }
        }).start();
    }

    public void read(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {

                int i = MbManager.getInstance().read_registers(50);
                Log.d("klog","value = "+i);


            }
        }).start();
    }
}
