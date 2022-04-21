package com.example.samsungstartproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViewById(R.id.btn_level1).setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MemoActivity.class);

            intent.putExtra(MemoActivity.LEVEL_STARS_COUNT, 9);
            intent.putExtra(MemoActivity.STAR_COUNT_TOTAL, 9);
            intent.putExtra(MemoActivity.STAR_IN_ROW, 3);
            intent.putExtra(MemoActivity.LEVEL_STARS_COUNT, new int[]{2, 3, 5});
            intent.putExtra(MemoActivity.LEVEL_NUMBER, v.getTag().toString());

            startActivity(intent);
        });

        findViewById(R.id.btn_level2).setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MemoActivity.class);

            intent.putExtra(MemoActivity.STAR_COUNT_TOTAL, 12);
            intent.putExtra(MemoActivity.STAR_IN_ROW, 4);
            intent.putExtra(MemoActivity.LEVEL_STARS_COUNT, new int[]{5, 6, 7});
            intent.putExtra(MemoActivity.LEVEL_NUMBER, v.getTag().toString());

            startActivity(intent);
        });

        findViewById(R.id.btn_level3).setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MemoActivity.class);

            intent.putExtra(MemoActivity.STAR_COUNT_TOTAL, 25);
            intent.putExtra(MemoActivity.STAR_IN_ROW, 5);
            intent.putExtra(MemoActivity.LEVEL_STARS_COUNT, new int[]{7, 8, 9});
            intent.putExtra(MemoActivity.LEVEL_NUMBER, v.getTag().toString());

            startActivity(intent);
        });

        findViewById(R.id.btn_level4).setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MemoActivity.class);

            intent.putExtra(MemoActivity.STAR_COUNT_TOTAL, 30);
            intent.putExtra(MemoActivity.STAR_IN_ROW, 5);
            intent.putExtra(MemoActivity.LEVEL_STARS_COUNT, new int[]{8, 9, 15});
            intent.putExtra(MemoActivity.LEVEL_NUMBER, v.getTag().toString());

            startActivity(intent);
        });

        drawIcons();
    }

    void drawIcons() {
        LinearLayout container = findViewById(R.id.container);

        for (int i = 0; i < container.getChildCount(); i++) {
            Button button = (Button) container.getChildAt(i);

            boolean isDone = getSharedPreferences(MemoActivity.COMPLETED_LEVELS, MODE_PRIVATE)
                    .getBoolean("level_" + button.getTag().toString(), false);

            Drawable img;
            if (isDone) {
                img = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_done_outline_24, getTheme());
            } else {
                img = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_close_24, getTheme());
            }
            img.setTint(Color.BLACK);
            button.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawIcons();
    }
}