package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DB.AppDatabase;
import com.t_t_talk.DB.Models.Phoneme;

import java.util.ArrayList;

public class PhonemeSelectTagalogActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CurvedTextView level_display;
    GridLayoutManager layoutManager;
    ArrayList<Phoneme> data;
    PhonemeAdapter adapter;
    String levelCode;
    AppDatabase db;
    ProgressBar loading_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phoneme_select_tagalog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loading_bar = findViewById(R.id.loading_bar);

        level_display = findViewById(R.id.level_display);

        Intent i = getIntent();
        Bundle phonemes = i.getBundleExtra("Phonemes");
        assert phonemes != null;
        data = (ArrayList<Phoneme>) phonemes.getSerializable("Phonemes");
        levelCode = i.getStringExtra("LevelCode");
        level_display.setText("Level " + levelCode.split("-")[1]);

        setRecyclerView();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(PhonemeSelectTagalogActivity.this,true, false))
                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_flag, new FlagIconFragment(R.drawable.img_flag_ph))
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new AppDatabase(PhonemeSelectTagalogActivity.this);
        this.data = db.localFetchPhonemes(this.levelCode);
        setRecyclerView();
    }

    private void setRecyclerView() {
        this.recyclerView = findViewById(R.id.recycler_view);

        this.layoutManager = new GridLayoutManager(this, 2);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.adapter = new PhonemeAdapter(PhonemeSelectTagalogActivity.this, this.data, "Tagalog", levelCode, loading_bar);
        this.recyclerView.setAdapter(this.adapter);
    }
}