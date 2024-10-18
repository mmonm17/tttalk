package com.t_t_talk;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PhonemeTagalogActivity extends AppCompatActivity {
    TextView txt_lvl;
    private CardView mic_animation;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phoneme_tagalog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_lvl = findViewById(R.id.txt_lvl);
        mic_animation = findViewById(R.id.mic_animation);
        txt_lvl.setText("Lebel 1");

        mic_animation.setOnClickListener(view -> {
            mic_animation.setVisibility(View.GONE);
        });

        String[] sentences = new String[]{
                "Ibabato ni Babols ang bato.",
                "Ibabato ni Babols ang bato.",
                "Ibabato ni Babols ang bato.",
                "Ibabato ni Babols ang bato.",
        };

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SentenceAdapter sentenceAdapter = new SentenceAdapter(sentences, "Ba", PhonemeTagalogActivity.this);
        recyclerView.setAdapter(sentenceAdapter);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_navigation_panel, new NavigationPanelFragment(true, false))
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_flag_ta, new FlagIconFragment(R.drawable.img_flag_ph))
                .commit();
    }
}