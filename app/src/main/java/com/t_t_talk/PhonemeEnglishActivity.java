package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PhonemeEnglishActivity extends AppCompatActivity {
    TextView txt_lvl;
    private CardView mic_animation;
    RecyclerView recyclerView;
    private CardView btn_mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phoneme_english);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_lvl = findViewById(R.id.txt_lvl);
        mic_animation = findViewById(R.id.mic_animation);
        btn_mic = findViewById(R.id.btn_mic);
        txt_lvl.setText("Level 1");

        btn_mic.setOnClickListener(view -> {
            mic_animation.setVisibility(View.VISIBLE);
        });

        mic_animation.setOnClickListener(view -> {
            mic_animation.setVisibility(View.GONE);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = getIntent();
        String[] sentences = i.getStringArrayExtra("Sentences");
        String highlighted = i.getStringExtra("PhonemeCode");

        SentenceAdapter sentenceAdapter = new SentenceAdapter(sentences, highlighted, "English", PhonemeEnglishActivity.this);
        recyclerView.setAdapter(sentenceAdapter);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(true, false))
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_flag_en, new FlagIconFragment(R.drawable.img_flag_us))
                .commit();
    }
}