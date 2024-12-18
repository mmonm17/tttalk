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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class LanguageSelectActivity extends AppCompatActivity {
    ConstraintLayout cl_english, cl_tagalog;
    CurvedTextView curved_language;
    AppDatabase db;
    ProgressBar loading_bar;

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

        // Declare a new DB
        db = new AppDatabase(LanguageSelectActivity.this);
        // Get the views from the layouts
        cl_english = findViewById(R.id.cl_english);
        cl_tagalog = findViewById(R.id.cl_tagalog);
        curved_language = findViewById(R.id.curved_language);
        loading_bar = findViewById(R.id.loading_bar);

        // Ensure background is present
        Drawable bg = ContextCompat.getDrawable(LanguageSelectActivity.this, R.drawable.shape_rnd_rect_blue_thick);
        assert bg != null;

        // Set the color for Tagalog
        GradientDrawable bg_alter = (GradientDrawable) bg.mutate();
        bg_alter.setColor(getColor(R.color.green));
        cl_tagalog.setBackground(bg_alter);

        // Set the text for the header
        curved_language.setText("LANGUAGE");

        // Animations
        Animation rotateAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.rotate);
        Animation scaleUpAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.scale);
        Animation scaleDownAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.scale_down);

        // On Click Listener for English
        cl_english.setOnClickListener(view -> {
            // Animations
            loading_bar.setVisibility(ProgressBar.VISIBLE);
            loading_bar.startAnimation(rotateAnimation);
            view.startAnimation(scaleUpAnimation);

            scaleUpAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.startAnimation(scaleDownAnimation); // Using the passed view
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            // Set Tagalog Clickable to False
            cl_tagalog.setClickable(false);
            // Fetch the levels for English and go to the next activity
            db.fetchLevels().thenAccept(levels -> {
                List<Level> englishLevels = new ArrayList<>();
                for (Level level : levels) {
                    if (level.getLanguage().equals("English")) {
                        englishLevels.add(level);
                    }
                }
                Intent intent = new Intent(LanguageSelectActivity.this, LevelSelectEnglishActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("levels", (Serializable) englishLevels);
                loading_bar.setVisibility(ProgressBar.INVISIBLE);
                loading_bar.clearAnimation();
                startActivity(intent);
            });
        });

        // On Click Listener for Tagalog
        cl_tagalog.setOnClickListener(view -> {
            // Animations
            loading_bar.setVisibility(ProgressBar.VISIBLE);
            loading_bar.startAnimation(rotateAnimation);
            view.startAnimation(scaleUpAnimation);
            scaleUpAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.startAnimation(scaleDownAnimation); // Using the passed view
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            // Set English Clickable to False
            cl_english.setClickable(false);
            // Set Tagalog Clickable to False
            db.fetchLevels().thenAccept(levels -> {
                List<Level> tagalogLevels = new ArrayList<>();
                for (Level level : levels) {
                    if (level.getLanguage().equals("Tagalog")) {
                        tagalogLevels.add(level);
                    }
                }
                Intent intent = new Intent(LanguageSelectActivity.this, LevelSelectTagalogActivity.class);
                intent.putExtra("levels", (Serializable) tagalogLevels);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loading_bar.setVisibility(ProgressBar.INVISIBLE);
                loading_bar.clearAnimation();
                startActivity(intent);
            });
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(LanguageSelectActivity.this, true, false))
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cl_english.setClickable(true);
        cl_tagalog.setClickable(true);
    }
}