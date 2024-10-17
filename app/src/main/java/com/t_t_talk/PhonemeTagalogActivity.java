package com.t_t_talk;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PhonemeTagalogActivity extends AppCompatActivity {


    TextView txt_lvl;
    TextBoxComponent textBoxComponent;
    private CardView btn_mic;
    private CardView mic_animation;

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
        btn_mic = findViewById(R.id.btn_mic);
        mic_animation = findViewById(R.id.mic_animation);

        textBoxComponent = findViewById(R.id.text_box_component);
        textBoxComponent.setTypeRead();
        textBoxComponent.setHighlightedTextBySubstring(PhonemeTagalogActivity.this,"Ibabato ni Babols ang bato.", "Ba");

        txt_lvl.setText("Level 1");

        btn_mic.setOnClickListener(view -> {
            Toast.makeText(this, "Mic button...", Toast.LENGTH_SHORT).show();
            mic_animation.setVisibility(View.VISIBLE);
        });

        mic_animation.setOnClickListener(view -> {
            mic_animation.setVisibility(View.GONE);
        });
    }
}