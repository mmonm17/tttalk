package com.t_t_talk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DB.Models.Level;
import com.t_t_talk.DB.Models.Phoneme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelSelectEnglishActivity extends AppCompatActivity {
    CurvedTextView curved_levels;
    RecyclerView level_display;
    LinearLayoutManager layoutManager;
    LevelBoxAdapter adapter;
    ArrayList<Level> data;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level_select_english);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progress_bar = findViewById(R.id.progress_bar);

        List<Integer> colors = Arrays.asList(
                Color.rgb(249, 222, 104),
                Color.rgb(179, 179, 179),
                Color.rgb(238, 118, 24),
                Color.rgb(112, 176, 69),
                Color.rgb(182, 213, 240),
                Color.rgb(135, 162, 122),
                Color.rgb(219, 153, 5)
        );

        Intent i = getIntent();
        List<Level> levels = (List<Level>) i.getSerializableExtra("levels");
        data = new ArrayList<>();
        int colorIndex = 0;

        assert levels != null;
        for (Level level : levels) {
            data.add(new Level(level.getLevelNumber(), level.getAge(), colors.get(colorIndex), level.getLanguage(), level.getPhonemeList()));
            colorIndex = (colorIndex + 1) % colors.size();
        }

        curved_levels = findViewById(R.id.curved_levels);
        curved_levels.setText("LEVELS");

        setupRecyclerView();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_flag_en, new FlagIconFragment(R.drawable.img_flag_us))
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(LevelSelectEnglishActivity.this,true, false))
                .commit();
    }

    private void setupRecyclerView() {
        this.level_display = findViewById(R.id.level_display);

        this.layoutManager = new LinearLayoutManager(this);
        this.level_display.setLayoutManager(layoutManager);

        this.adapter = new LevelBoxAdapter(LevelSelectEnglishActivity.this, this.data, "English", progress_bar);
        this.level_display.setAdapter(this.adapter);
    }
}