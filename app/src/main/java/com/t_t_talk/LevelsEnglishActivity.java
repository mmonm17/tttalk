package com.t_t_talk;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DataTypes.Level;
import com.t_t_talk.DataTypes.Phoneme;

import java.util.ArrayList;

public class LevelsEnglishActivity extends AppCompatActivity {
    CurvedTextView curved_levels;
    RecyclerView level_display;
    LinearLayoutManager layoutManager;
    LevelBoxAdapter adapter;
    ArrayList<Level> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_levels_english);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] sentences = new String[]{
                "Sam the cat saw a snake in the grass",
                "The snake was small and slid fast",
                "Sam sat and watched the snake go by",
                "The snake said, \"sss,\" and Sam said, \"Hi!\""
        };

        ArrayList<Phoneme> phonemes = new ArrayList<>();
        phonemes.add(new Phoneme(sentences, 2, "S"));
        phonemes.add(new Phoneme(sentences, 1, "A"));
        phonemes.add(new Phoneme(sentences, 3, "T"));
        phonemes.add(new Phoneme(sentences, 0, "P"));

        data = new ArrayList<>();
        data.add(new Level(1, 3, Color.rgb(249, 222, 104), phonemes));
        data.add(new Level(2, 3, Color.rgb(179, 179, 179), phonemes));
        data.add(new Level(3, 4, Color.rgb(238, 118, 24), phonemes));
        data.add(new Level(4, 4, Color.rgb(112, 176, 69), phonemes));
        data.add(new Level(5, 4, Color.rgb(182, 213, 240), phonemes));
        data.add(new Level(6, 5, Color.rgb(135, 162, 122), phonemes));
        data.add(new Level(7, 5, Color.rgb(219, 153, 5), phonemes));

        curved_levels = findViewById(R.id.curved_levels);
        curved_levels.setText("LEVELS");

        setupRecyclerView();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_flag_en, new FlagIconFragment(R.drawable.img_flag_us))
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(true, false))
                .commit();
    }

    private void setupRecyclerView() {
        this.level_display = findViewById(R.id.level_display);

        this.layoutManager = new LinearLayoutManager(this);
        this.level_display.setLayoutManager(layoutManager);

        this.adapter = new LevelBoxAdapter(LevelsEnglishActivity.this, this.data, "English");
        this.level_display.setAdapter(this.adapter);
    }
}