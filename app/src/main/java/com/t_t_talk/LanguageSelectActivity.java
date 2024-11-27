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
        loading_bar = findViewById(R.id.loading_bar);

        Drawable bg = ContextCompat.getDrawable(LanguageSelectActivity.this, R.drawable.shape_rnd_rect_blue_thick);
        assert bg != null;

        GradientDrawable bg_alter = (GradientDrawable) bg.mutate();
        bg_alter.setColor(getColor(R.color.green));
        cl_tagalog.setBackground(bg_alter);

        Animation rotateAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.rotate);
        Animation scaleUpAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.scale);
        Animation scaleDownAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.scale_down);
        //Animation fadeAnimation = AnimationUtils.loadAnimation(LanguageSelectActivity.this, R.anim.fade_bg);



        cl_english.setOnClickListener(view -> {
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
            cl_tagalog.setClickable(false);
            //PauseDialog pauseDialog = new PauseDialog(LanguageSelectActivity.this, "We are downloading the levels, please wait a moment. Next time you open, it will be faster.", "Please Wait...");
            //pauseDialog.show();
            db.fetchLevels().thenAccept(levels -> {
                //pauseDialog.dismiss();
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


        cl_tagalog.setOnClickListener(view -> {
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
            cl_english.setClickable(false);
            //PauseDialog pauseDialog = new PauseDialog(LanguageSelectActivity.this, "Kinukuha lang po namin ang mga lebels, saglit lang po. Sa susunod, mas mabilis na!", "Saglit po...");
            //pauseDialog.show();
            db.fetchLevels().thenAccept(levels -> {
                //pauseDialog.dismiss();
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

        curved_language.setText("LANGUAGE");

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