package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LanguageActivity extends AppCompatActivity {
    TextView txt_english;
    TextView txt_tagalog;
    CurvedTextView curved_language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_language);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_english = findViewById(R.id.txt_english);
        txt_tagalog = findViewById(R.id.txt_tagalog);
        curved_language = findViewById(R.id.curved_language);

        txt_english.setOnClickListener(view -> {
            Intent intent = new Intent(LanguageActivity.this, LevelsActivityEnglish.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Toast.makeText(LanguageActivity.this, "English", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

        txt_tagalog.setOnClickListener(view -> {
            Intent intent = new Intent(LanguageActivity.this, LevelsActivityTagalog.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Toast.makeText(LanguageActivity.this, "Filipino", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

        curved_language.setText("LANGUAGE");

    }
}