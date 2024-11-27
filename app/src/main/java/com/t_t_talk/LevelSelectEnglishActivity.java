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
    ProgressBar loading_bar;

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

        // Get the view for the loading bar
        loading_bar = findViewById(R.id.loading_bar);

        // Get the intent from previous activity
        Intent intent = getIntent();
        List<Level> levels = (List<Level>) intent.getSerializableExtra("levels");

        // Create a new ArrayList for the data
        data = new ArrayList<>();
        assert levels != null;
        for(Level level : levels) {
            data.add(new Level(level.getLevelNumber(), level.getAge(), level.getColor(), level.getLanguage(), level.getPhonemeList()));
        }

        // Set the header text to levels
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

    /**
     * Sets up the RecyclerView for displaying levels.
     */
    private void setupRecyclerView() {
        this.level_display = findViewById(R.id.level_display);

        this.layoutManager = new LinearLayoutManager(this);
        this.level_display.setLayoutManager(layoutManager);

        this.adapter = new LevelBoxAdapter(LevelSelectEnglishActivity.this, this.data, "English", loading_bar);
        this.level_display.setAdapter(this.adapter);
    }
}