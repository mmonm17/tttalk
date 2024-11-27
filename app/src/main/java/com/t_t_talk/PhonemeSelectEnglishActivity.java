package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.util.Log;

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

public class PhonemeSelectEnglishActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CurvedTextView level_display;
    GridLayoutManager layoutManager;
    ArrayList<Phoneme> data;
    PhonemeAdapter adapter;
    String levelCode;
    AppDatabase db;
    ProgressBar loading_bar;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phoneme_select_english);
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
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(PhonemeSelectEnglishActivity.this,true, false))
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_flag, new FlagIconFragment(R.drawable.img_flag_us))
                .commit();

        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setProgress(computeStarProgress());
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new AppDatabase(PhonemeSelectEnglishActivity.this);
        this.data = db.localFetchPhonemes(this.levelCode);
        setRecyclerView();

        progress_bar.setProgress(computeStarProgress());
    }

    private void setRecyclerView() {
        this.recyclerView = findViewById(R.id.recycler_view);

        this.layoutManager = new GridLayoutManager(this, 2);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.adapter = new PhonemeAdapter(PhonemeSelectEnglishActivity.this, this.data, "English", levelCode, loading_bar);
        this.recyclerView.setAdapter(this.adapter);
    }

    private int computeStarProgress(){
        int totalStars = 0;
        for(Phoneme phoneme:this.data){
            totalStars += phoneme.getStarCount();
        }
        return (int) ((float) totalStars / (float) (data.size() * 3) * 100);
    }
}