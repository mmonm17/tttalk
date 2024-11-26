package com.t_t_talk;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.t_t_talk.DB.AppDatabase;
import com.t_t_talk.DB.Models.Level;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LanguageSelectActivity extends AppCompatActivity {
    ConstraintLayout cl_english, cl_tagalog;
    CurvedTextView curved_language;
    AppDatabase db;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_language_select);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /*ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.1f,
                1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setDuration(500);*/

        db = new AppDatabase(LanguageSelectActivity.this);

        cl_english = findViewById(R.id.cl_english);
        cl_tagalog = findViewById(R.id.cl_tagalog);
        curved_language = findViewById(R.id.curved_language);
        progress_bar = findViewById(R.id.progress_bar);

        Drawable bg = ContextCompat.getDrawable(LanguageSelectActivity.this, R.drawable.shape_rnd_rect_blue_thick);
        assert bg != null;

        GradientDrawable bg_alter = (GradientDrawable) bg.mutate();
        bg_alter.setColor(getColor(R.color.green));
        cl_tagalog.setBackground(bg_alter);

        Animation rotateAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.rotate);
        Animation scaleAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.scale);
        //Animation fadeAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.fade_bg);
        cl_english.setOnClickListener(view -> {
            progress_bar.setVisibility(ProgressBar.VISIBLE);
            progress_bar.startAnimation(rotateAnimation);
            view.startAnimation(scaleAnimation);
            db.fetchLevels(new AppDatabase.LevelsCallback() {
                @Override
                public void onLevelsFetched(List<Level> levels) {
                    Intent intent = new Intent(LanguageSelectActivity.this, LevelSelectEnglishActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("levels", (Serializable) levels);
                    progress_bar.clearAnimation();
                    progress_bar.setVisibility(ProgressBar.INVISIBLE);
                    startActivity(intent);
                }
            });

        });


        cl_tagalog.setOnClickListener(view -> {
            progress_bar.setVisibility(ProgressBar.VISIBLE);
            progress_bar.startAnimation(rotateAnimation);
            view.startAnimation(scaleAnimation);
            db.fetchLevels(new AppDatabase.LevelsCallback() {
                @Override
                public void onLevelsFetched(List<Level> levels) {
                    Intent intent = new Intent(LanguageSelectActivity.this, LevelSelectTagalogActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra("levels", (Serializable) levels);
                    progress_bar.clearAnimation();
                    progress_bar.setVisibility(ProgressBar.INVISIBLE);
                    startActivity(intent);
                }
            });
        });

        curved_language.setText("LANGUAGE");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(LanguageSelectActivity.this, true, false))
                .commit();
    }
}