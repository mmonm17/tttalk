package com.t_t_talk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

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

        /*
        Intent i = getIntent();
        List<Level> levels = (List<Level>) i.getSerializableExtra("levels");
        data = new ArrayList<>();

        for (Level level : levels) {
            int color;
            switch (level.getLevelNumber() % 7) {  // Cycle through 7 different colors
                case 0: color = Color.rgb(249, 222, 104); break;
                case 1: color = Color.rgb(179, 179, 179); break;
                case 2: color = Color.rgb(238, 118, 24); break;
                case 3: color = Color.rgb(112, 176, 69); break;
                case 4: color = Color.rgb(182, 213, 240); break;
                case 5: color = Color.rgb(135, 162, 122); break;
                case 6: color = Color.rgb(219, 153, 5); break;
                default: color = Color.rgb(0, 0, 0);  // Default color
            }
            data.add(new Level(level.getLevelNumber(), level.getAge(), color, level.getLanguage(), level.getPhonemeList()));
        }

        */


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

        this.adapter = new LevelBoxAdapter(LevelSelectTagalogActivity.this, this.data, "Tagalog");
        this.level_display.setAdapter(this.adapter);
    }
}