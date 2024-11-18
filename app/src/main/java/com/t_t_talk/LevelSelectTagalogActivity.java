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

        String[] sentences = new String[]{
                "Ibabato ni Babols ang bato.",
                "Ibabato ni Babols ang bato.",
                "Ibabato ni Babols ang bato.",
                "Ibabato ni Babols ang bato.",
        };

        ArrayList<Phoneme> phonemes = new ArrayList<>();
        phonemes.add(new Phoneme(sentences, 2, "BA"));
        phonemes.add(new Phoneme(sentences, 1, "BE"));
        phonemes.add(new Phoneme(sentences, 3, "BI"));
        phonemes.add(new Phoneme(sentences, 0, "BO"));
        phonemes.add(new Phoneme(sentences, 1, "BU"));

        data = new ArrayList<>();
        data.add(new Level(1, 3, Color.rgb(249, 222, 104), "Tagalog", phonemes));
        data.add(new Level(2, 3, Color.rgb(179, 179, 179), "Tagalog", phonemes));
        data.add(new Level(3, 4, Color.rgb(238, 118, 24), "Tagalog", phonemes));
        data.add(new Level(4, 4, Color.rgb(112, 176, 69), "Tagalog", phonemes));
        data.add(new Level(5, 4, Color.rgb(182, 213, 240), "Tagalog", phonemes));
        data.add(new Level(6, 5, Color.rgb(135, 162, 122), "Tagalog", phonemes));
        data.add(new Level(7, 5, Color.rgb(219, 153, 5), "Tagalog", phonemes));

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