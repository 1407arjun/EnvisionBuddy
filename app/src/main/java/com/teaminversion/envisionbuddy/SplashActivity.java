package com.teaminversion.envisionbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    TextView en,bud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        en = findViewById(R.id.textView3);
        bud = findViewById(R.id.textView2);
        en.animate().translationXBy(45f).alpha(1f).setDuration(1000);
        bud.animate().translationXBy(-45f).alpha(1f).setDuration(1000);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(com.teaminversion.envisionbuddy.SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}