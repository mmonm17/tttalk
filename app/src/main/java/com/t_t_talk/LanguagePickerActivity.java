package com.t_t_talk;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LanguagePickerActivity extends AppCompatActivity {

    ImageView img_bg_overlay_en;
    ImageView getImg_bg_overlay_fil;
    TextView txt_en;
    TextView txt_fil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_language_picker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        img_bg_overlay_en = findViewById(R.id.img_bg_overlay_en);
        getImg_bg_overlay_fil = findViewById(R.id.img_bg_overlay_fil);
        txt_en = findViewById(R.id.txt_en);
        txt_fil = findViewById(R.id.txt_fil);

        img_bg_overlay_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagePickerActivity.this, "English", Toast.LENGTH_SHORT).show();
            }
        });

        getImg_bg_overlay_fil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagePickerActivity.this, "Filipino", Toast.LENGTH_SHORT).show();
            }
        });

        txt_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagePickerActivity.this, "English", Toast.LENGTH_SHORT).show();
            }
        });

        txt_fil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagePickerActivity.this, "Tagalog", Toast.LENGTH_SHORT).show();
            }
        });

        
    }


}