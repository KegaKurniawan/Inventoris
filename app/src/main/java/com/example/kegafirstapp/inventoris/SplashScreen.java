package com.example.kegafirstapp.inventoris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent main = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(main);
                }
            }
        };
        timer.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}