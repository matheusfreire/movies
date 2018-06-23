package com.msf.moveis;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SplashActivity.this, "Teste", Toast.LENGTH_SHORT).show();
            }
        };
        handler.postDelayed(r, 2500);

    }
}
