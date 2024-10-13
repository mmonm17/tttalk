package com.t_t_talk;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

<<<<<<<< HEAD:app/src/main/java/com/t_t_talk/EnglishTest.java
public class EnglishTest extends AppCompatActivity {
========
public class LevelsActivityEnglish extends AppCompatActivity {
    CurvedTextView curved_levels;
>>>>>>>> main:app/src/main/java/com/t_t_talk/LevelsActivityEnglish.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
<<<<<<<< HEAD:app/src/main/java/com/t_t_talk/EnglishTest.java
        setContentView(R.layout.activity_english_test);
========
        setContentView(R.layout.activity_levels_english);
>>>>>>>> main:app/src/main/java/com/t_t_talk/LevelsActivityEnglish.java
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
<<<<<<<< HEAD:app/src/main/java/com/t_t_talk/EnglishTest.java
========

        curved_levels = findViewById(R.id.curved_levels);
        curved_levels.setText("LEVELS");
>>>>>>>> main:app/src/main/java/com/t_t_talk/LevelsActivityEnglish.java
    }
}