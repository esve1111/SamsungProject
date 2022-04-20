package com.example.samsungstartproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViewById(R.id.btn_level1).setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MemoActivity.class);

            intent.putExtra(MemoActivity.STAR_COUNT_TOTAL, 9);
            intent.putExtra(MemoActivity.STAR_IN_ROW, 3);
            intent.putExtra(MemoActivity.LEVEL_STARS_COUNT, new int[]{2, 3, 5});

            startActivity(intent);
        });

        findViewById(R.id.btn_level2).setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MemoActivity.class);

            intent.putExtra(MemoActivity.STAR_COUNT_TOTAL, 12);
            intent.putExtra(MemoActivity.STAR_IN_ROW, 4);
            intent.putExtra(MemoActivity.LEVEL_STARS_COUNT, new int[]{5, 6, 7});

            startActivity(intent);
        });

        findViewById(R.id.btn_level3).setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MemoActivity.class);

            intent.putExtra(MemoActivity.STAR_COUNT_TOTAL, 25);
            intent.putExtra(MemoActivity.STAR_IN_ROW, 5);
            intent.putExtra(MemoActivity.LEVEL_STARS_COUNT, new int[]{7, 8, 9});

            startActivity(intent);
        });
    }
}