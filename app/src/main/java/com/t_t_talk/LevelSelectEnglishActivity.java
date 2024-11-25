package com.t_t_talk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

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

        Intent intent = getIntent();
        List<Level> levels = (List<Level>) intent.getSerializableExtra("levels");

        data = new ArrayList<>();
        assert levels != null;
        for(Level level : levels) {
            data.add(new Level(level.getLevelNumber(), level.getAge(), Color.rgb(249, 222, 104), level.getLanguage(), level.getPhonemeList()));
        }

        /*ArrayList<String> sentences = new ArrayList<String>(Arrays.asList(new String[]{
               "Sam the cat saw a snake in the grass",
                "The snake slid fast",
                "Sam sat and watched the snake go by",
                "The snake said, \"sss,\" and Sam said, \"Hi!\""
        }));

        ArrayList<Phoneme> phonemes = new ArrayList<>();
        phonemes.add(new Phoneme(sentences, 2, "S"));
        phonemes.add(new Phoneme(sentences, 1, "A"));
        phonemes.add(new Phoneme(sentences, 3, "T"));
        phonemes.add(new Phoneme(sentences, 0, "P"));*/

        /*data = new ArrayList<>();
        data.add(new Level(1, 3, Color.rgb(249, 222, 104), "English", phonemes));
        data.add(new Level(2, 3, Color.rgb(179, 179, 179), "English", phonemes));
        data.add(new Level(3, 4, Color.rgb(238, 118, 24), "English", phonemes));
        data.add(new Level(4, 4, Color.rgb(112, 176, 69), "English", phonemes));
        data.add(new Level(5, 4, Color.rgb(182, 213, 240), "English", phonemes));
        data.add(new Level(6, 5, Color.rgb(135, 162, 122), "English", phonemes));
        data.add(new Level(7, 5, Color.rgb(219, 153, 5), "English", phonemes));*/

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

        this.adapter = new LevelBoxAdapter(LevelSelectEnglishActivity.this, this.data, "English");
        this.level_display.setAdapter(this.adapter);
    }
}