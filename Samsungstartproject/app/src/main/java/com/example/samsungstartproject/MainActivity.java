package com.example.samsungstartproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_game1).setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);

        });

        findViewById(R.id.btn_tips).setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, TipsActivity.class);
            startActivity(intent);

        });
    }
}