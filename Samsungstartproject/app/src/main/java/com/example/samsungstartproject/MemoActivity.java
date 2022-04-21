package com.example.samsungstartproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String STAR_COUNT_TOTAL = "STAR_COUNT_TOTAL";
    public static final String STAR_IN_ROW = "STAR_IN_ROW";
    public static final String LEVEL_STARS_COUNT = "LEVEL_STARS_COUNT";
    public static final String LEVEL_NUMBER = "LEVEL_NUMBER";
    public static final String COMPLETED_LEVELS = "COMPLETED_LEVELS";

    private ArrayList<Boolean> answer;
    private ArrayList<Boolean> current;

    private int[] level_stars_count;
    private int star_in_row = 0;
    private int level = 0;
    private int star_count_total = 0;
    private int error_count = 0;
    private String level_number = "";

    private static final int DELAY_DEFAULT = 5000;

    private TextView title;

    private Timer timer = new Timer();
    private int lastTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        star_count_total = getIntent().getIntExtra(STAR_COUNT_TOTAL, 9);
        star_in_row = getIntent().getIntExtra(STAR_IN_ROW, 3);
        level_stars_count = getIntent().getIntArrayExtra(LEVEL_STARS_COUNT);
        level_number = getIntent().getStringExtra(LEVEL_NUMBER);

        answer = new ArrayList<>(star_count_total);
        current = new ArrayList<>(star_count_total);

        title = findViewById(R.id.title);
        title.setText("Для начала игры нажмите START");

        generateField();

        findViewById(R.id.start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 0;
                startLevel();
            }
        });

        findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLevel();
            }
        });
    }

    void generateField() {
        LinearLayout field = findViewById(R.id.game_field);
        field.removeAllViewsInLayout();

        for (int i = 0; i < star_count_total; i++) {
            int row = i / star_in_row;
            int column = i % star_in_row;

            if (column == 0) {
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setPadding(0, 0, 0, 0);
                field.addView(linearLayout);
            }

            ImageButton star = new ImageButton(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.star_size),
                    getResources().getDimensionPixelSize(R.dimen.star_size)
            );
            layoutParams.setMargins(0, 0, 0, 0);
            star.setLayoutParams(layoutParams);
            star.setPadding(0, 0, 0, 0);
            star.setImageResource(android.R.drawable.star_big_off);
            star.setTag(String.valueOf(i));

            ((LinearLayout) field.getChildAt(row)).addView(star);
        }
    }

    void startLevel() {
        generateStars(level);
        render(answer, false);

        lastTime = DELAY_DEFAULT;

        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()->{
                    if (lastTime < 0) {
                        timer.cancel();
                        timer.purge();
                        title.setText("Вспоминай");
                        render(current, true);
                    } else {
                        title.setText("Осталось " + (lastTime / 1000) + " секунд");
                        lastTime -= 1000;
                    }
                });

            }
        }, 1000, 1000);
    }

    private void render(ArrayList<Boolean> array, boolean enableClick) {
        for (int i = 0; i < array.size(); i++) {
            int row = i / star_in_row;
            int column = i % star_in_row;

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

        ((TextView) findViewById(R.id.error_counter)).setText(String.format("Количество ошибок: %d", error_count));
    }

    void generateStars(int level) {
        int stars = level_stars_count[level];

        answer.clear();
        current.clear();

        for (int i = 0; i < star_count_total; i++) {
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

            if (isMarch) {
                if (level_stars_count.length - 1 == level) {
                    getSharedPreferences(COMPLETED_LEVELS, MODE_PRIVATE)
                            .edit()
                            .putBoolean("level_" + level_number, true)
                            .apply();
                    showFinishDialog();
                    return;
                } else {
                    message = "ПРАВИЛЬНО! Теперь сложнее!";
                    level++;
                    startLevel();
                }
            } else {
                message = "ОШИБКА! \nПопробуй снова!";
                error_count++;
                render(current, false);
            }

            title.setText(message);
        }
    }

    void showFinishDialog() {
        new AlertDialog.Builder(this)
                .setMessage("ПРАВИЛЬНО! Вы прошли уровень!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }
}