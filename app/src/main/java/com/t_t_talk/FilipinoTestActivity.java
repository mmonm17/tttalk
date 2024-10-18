package com.t_t_talk;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.t_t_talk.FlagIconFragment;

public class FilipinoTestActivity extends AppCompatActivity {


    TextView txt_lvl;
    TextBoxComponent textBoxComponent;
    private CardView btn_mic;
    private CardView mic_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filipino_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_lvl = findViewById(R.id.txt_lvl);
        btn_mic = findViewById(R.id.btn_mic);
        mic_animation = findViewById(R.id.mic_animation);
        // Assuming you are in an AppCompatActivity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FlagIconFragment flagIconFragment = (FlagIconFragment) fragmentManager.findFragmentById(R.id.fragment_flag);

        if (flagIconFragment != null && flagIconFragment.getView() != null) {
            ImageView imgFlagContent = flagIconFragment.getView().findViewById(R.id.img_flag_content);
            if (imgFlagContent != null) {
                // Call the public method to set the image
                flagIconFragment.setImage(imgFlagContent, R.drawable.img_flag_us);
            } else {
                Toast.makeText(this, "ImageView not found in fragment", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Fragment not found or not initialized", Toast.LENGTH_SHORT).show();
        }
        textBoxComponent = findViewById(R.id.text_box_component);
        textBoxComponent.setTypeRead();
        textBoxComponent.setHighlightedTextBySubstring("Ibabato ni Babols ang bato.", "Ba");

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