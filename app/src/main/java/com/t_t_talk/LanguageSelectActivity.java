package com.t_t_talk;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;

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

        db = new AppDatabase(LanguageSelectActivity.this);

        cl_english = findViewById(R.id.cl_english);
        cl_tagalog = findViewById(R.id.cl_tagalog);
        curved_language = findViewById(R.id.curved_language);

        Drawable bg = ContextCompat.getDrawable(LanguageSelectActivity.this, R.drawable.shape_rnd_rect_blue_thick);
        assert bg != null;

        GradientDrawable bg_alter = (GradientDrawable) bg.mutate();
        bg_alter.setColor(getColor(R.color.green));
        cl_tagalog.setBackground(bg_alter);

        /*cl_english.setOnClickListener(view -> {
//            Intent intent = new Intent(LanguageSelectActivity.this, LevelSelectEnglishActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Toast.makeText(LanguageActivity.this, "English", Toast.LENGTH_SHORT).show();
//            startActivity(intent);
            db.fetchLevels();
        });*/

        cl_english.setOnClickListener(view -> {
            db.fetchLevels(new AppDatabase.LevelsCallback() {
                @Override
                public void onLevelsFetched(List<Level> levels) {
                    // Handle the levels here
                    Log.d("LanguageSelectActivity", "Fetched levels: " + levels.size());
                    for (Level level : levels) {
                        Log.d("LanguageSelectActivity", "Fetched level: " + level.toString());
                    }
                    Intent intent = new Intent(LanguageSelectActivity.this, LevelSelectEnglishActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("levels", (Serializable) levels);
                    startActivity(intent);
                }
            });

        });


        cl_tagalog.setOnClickListener(view -> {
            Intent intent = new Intent(LanguageSelectActivity.this, LevelSelectTagalogActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        curved_language.setText("LANGUAGE");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(LanguageSelectActivity.this, true, false))
                .commit();
    }
}