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

public class PhonemeSoundTagalogActivity extends AppCompatActivity {
    TextView txt_lvl;
    TextView txt_title;
    private CardView mic_animation;
    RecyclerView recyclerView;
    private CardView btn_mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phoneme_sound_tagalog);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get views from layout
        txt_lvl = findViewById(R.id.txt_lvl);
        txt_title = findViewById(R.id.txt_title);
        mic_animation = findViewById(R.id.mic_animation);
        btn_mic = findViewById(R.id.btn_mic);
        recyclerView = findViewById(R.id.recyclerView);

        // Set listeners
        btn_mic.setOnClickListener(view -> {
            mic_animation.setVisibility(View.VISIBLE);
        });

        mic_animation.setOnClickListener(view -> {
            mic_animation.setVisibility(View.GONE);
        });
        // Get intent
        Intent i = getIntent();
        String[] sentences = i.getStringArrayExtra("Sentences");
        String highlighted = i.getStringExtra("PhonemeCode");
        String levelCode = i.getStringExtra("LevelCode");
        int levelNum = Integer.valueOf(levelCode.split("-")[1]);

        // Set headers
        txt_lvl.setText("Lebel " + levelNum);
        txt_title.setText(String.format("\" %s \" ", highlighted.toUpperCase()));

        // Set recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SentenceAdapter sentenceAdapter = new SentenceAdapter(sentences, highlighted, "Tagalog", PhonemeSoundTagalogActivity.this, levelCode, highlighted);
        recyclerView.setAdapter(sentenceAdapter);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(PhonemeSoundTagalogActivity.this,true, false))
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_flag_ta, new FlagIconFragment(R.drawable.img_flag_ph))
                .commit();
    }
}