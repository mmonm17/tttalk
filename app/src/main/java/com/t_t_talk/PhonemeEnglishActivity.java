package com.t_t_talk;

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
        txt_lvl.setText("Level 1");

        mic_animation.setOnClickListener(view -> {
            mic_animation.setVisibility(View.GONE);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // String[] sentences = Phoneme.getSentences();
        String[] sentences = new String[]{
                "Sam the cat saw a snake in the grass",
                "The snake was small and slid fast",
                "Sam sat and watched the snake go by",
                "The snake said, \"sss,\" and Sam said, \"Hi!\""
        };

        SentenceAdapter sentenceAdapter = new SentenceAdapter(sentences, 's', PhonemeEnglishActivity.this);
        recyclerView.setAdapter(sentenceAdapter);

    }
}