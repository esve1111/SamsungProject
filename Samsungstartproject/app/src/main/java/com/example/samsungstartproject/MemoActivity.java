package com.example.samsungstartproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener {

    private final ArrayList<Boolean> answer = new ArrayList<>(START_COUNT_DEFAULT);
    private final ArrayList<Boolean> current = new ArrayList<>(START_COUNT_DEFAULT);
    private final int[] LEVEL_STARS_COUNT = {2, 3, 5};
    private int level = 0;

    private static final int START_COUNT_DEFAULT = 9;
    private static final int START_IN_ROW = 3;
    private static final int DELAY_DEFAULT = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        findViewById(R.id.start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLevel();
            }
        });
    }

    void startLevel() {

        generateStars(level);
        render(answer, false);

        new Handler().postDelayed(() -> render(current, true), DELAY_DEFAULT);

    }

    private void render(ArrayList<Boolean> array, boolean enableClick) {
        for (int i = 0; i < array.size(); i++) {
            int row = i / START_IN_ROW;
            int column = i % START_IN_ROW;

            LinearLayout field = findViewById(R.id.game_field);
            ImageButton star = (ImageButton) ((LinearLayout) field.getChildAt(row)).getChildAt(column);

            if (enableClick) {
                star.setOnClickListener(this);
            } else {
                star.setOnClickListener(null);
            }

            if (array.get(i)) {
                star.setImageResource(android.R.drawable.star_big_on);
            } else {
                star.setImageResource(android.R.drawable.star_big_off);
            }
        }
    }

    void generateStars(int level) {
        int stars = LEVEL_STARS_COUNT[level];

        answer.clear();
        current.clear();

        for (int i = 0; i < START_COUNT_DEFAULT; i++) {
            answer.add(i < stars);
            current.add(false);
        }

        Collections.shuffle(answer);
    }

    @Override
    public void onClick(View v) {
        int index = Integer.parseInt(v.getTag().toString());

        current.set(index, true);
        ((ImageButton) v).setImageResource(android.R.drawable.star_big_on);

        check();
    }

    private void check() {

        int starsCount = 0;
        for (int i = 0; i < current.size(); i++) {
            if (current.get(i)) {
                starsCount++;
            }
        }

        int starsAnswerCount = 0;
        for (int i = 0; i < answer.size(); i++) {
            if (answer.get(i)) {
                starsAnswerCount++;
            }
        }

        if (starsCount >= starsAnswerCount) {
            boolean isMarch = false;
            for (int i = 0; i < answer.size(); i++) {
                if (answer.get(i) == current.get(i)) {
                    isMarch = true;
                } else {
                    isMarch = false;
                    break;
                }
            }

            String message = "";
            boolean isFinish = false;

            if (isMarch) {
                if (LEVEL_STARS_COUNT.length - 1 == level) {
                    isFinish = true;
                    message = "ПРАВИЛЬНО! Вы прошли уровень";
                } else {
                    message = "ПРАВИЛЬНО! Теперь сложнее";
                }

            } else {
                message = "ОШИБКА";
            }

            boolean finalIsFinish = isFinish;
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (finalIsFinish) {
                                finish();
                            } else {
                                level++;
                                startLevel();
                            }
                        }
                    })
                    .show();

        }
    }
}