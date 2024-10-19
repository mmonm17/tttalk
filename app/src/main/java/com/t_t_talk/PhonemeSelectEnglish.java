package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DataTypes.Phoneme;

import java.util.ArrayList;

public class PhonemeSelectEnglish extends AppCompatActivity {
    RecyclerView recyclerView;
    CurvedTextView level_display;
    GridLayoutManager layoutManager;
    ArrayList<Phoneme> data;
    PhonemeAdapter adapter;

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

        level_display = findViewById(R.id.level_display);
        level_display.setText("Level 1");

        Intent i = getIntent();
        Bundle phonemes = i.getBundleExtra("Phonemes");
        assert phonemes != null;
        data = (ArrayList<Phoneme>) phonemes.getSerializable("Phonemes");

        setRecyclerView();
    }

    private void setRecyclerView() {
        this.recyclerView = findViewById(R.id.recycler_view);

        this.layoutManager = new GridLayoutManager(this, 2);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.adapter = new PhonemeAdapter(this.data);
        this.recyclerView.setAdapter(this.adapter);
    }
}