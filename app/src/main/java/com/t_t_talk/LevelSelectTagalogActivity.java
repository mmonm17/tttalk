package com.t_t_talk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class LevelSelectTagalogActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_level_select_tagalog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progress_bar = findViewById(R.id.progress_bar);

        Intent i = getIntent();
        List<Level> levels = (List<Level>) i.getSerializableExtra("levels");
        data = new ArrayList<>();
        for (Level level : levels) {
            data.add(new Level(level.getLevelNumber(), level.getAge(), level.getColor(), level.getLanguage(), level.getPhonemeList()));
        }

        curved_levels = findViewById(R.id.curved_levels);
        curved_levels.setText("MGA LEBEL");

        setupRecyclerView();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_flag_ta, new FlagIconFragment(R.drawable.img_flag_ph))
                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(LevelSelectTagalogActivity.this,true, false))
                .commit();
    }

    private void setupRecyclerView() {
        this.level_display = findViewById(R.id.level_display);

        this.layoutManager = new LinearLayoutManager(this);
        this.level_display.setLayoutManager(layoutManager);

        this.adapter = new LevelBoxAdapter(LevelSelectTagalogActivity.this, this.data, "Tagalog", progress_bar);
        this.level_display.setAdapter(this.adapter);
    }
}