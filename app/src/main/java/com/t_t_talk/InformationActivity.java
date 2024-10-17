package com.t_t_talk;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.*;
import androidx.cardview.widget.CardView;

import java.util.Arrays;

public class InformationActivity extends AppCompatActivity {
    String[] bullets = {
            " Struggling to repeat",
            "  An oral stop consonant or cluster",
            "   e.g. /tr/ in ‘electr-tr-tronically’",
            " Whole syllable",
            "  e.g. /au/ in ‘au-au-australia’",
            " Short words",
            "  pronoun I in ‘I I I work’",
            " Stuttered block",
            "  e.g. elect : : : : : ronically",
            " Prolongations in stuttering",
            "  e.g. sssseven"
    };


    private InfoBoxComponent info_box_component;
    private TextView info_text;
    private TextView strategies_text;
    private TextView partners_text;

    private ImageView info_icon;
    private ImageView strategies_icon;
    private ImageView partners_icon;

    private LinearLayout info_section;
    private LinearLayout strategies_section;
    private LinearLayout partners_section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        info_box_component = findViewById(R.id.info_box);
        info_text = findViewById(R.id.info_text);
        strategies_text = findViewById(R.id.strategies_text);
        partners_text = findViewById(R.id.partners_text);

        info_icon = findViewById(R.id.info_icon);
        strategies_icon = findViewById(R.id.strategies_icon);
        partners_icon = findViewById(R.id.partners_icon);

        info_section = findViewById(R.id.info_section);
        strategies_section = findViewById(R.id.strategies_section);
        partners_section = findViewById(R.id.partners_section);

        //setTextBullets(setTextBullets(bullets);
        info_box_component.setTextBullets(bullets);

        info_section.setOnClickListener(v -> {
            //info_text.setVisibility(info_text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            //info_icon.setImageResource(info_text.getVisibility() == View.VISIBLE ? R.drawable.ic_baseline_keyboard_arrow_up_24 : R.drawable.ic_baseline_keyboard_arrow_down_24);
            Toast.makeText(this, "Info section clicked", Toast.LENGTH_SHORT).show();
        });

        strategies_section.setOnClickListener(v -> {
            //strategies_text.setVisibility(strategies_text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            //strategies_icon.setImageResource(strategies_text.getVisibility() == View.VISIBLE ? R.drawable.ic_baseline_keyboard_arrow_up_24 : R.drawable.ic_baseline_keyboard_arrow_down_24);
            Toast.makeText(this, "Strategies section clicked", Toast.LENGTH_SHORT).show();
        });

        partners_section.setOnClickListener(v -> {
            //partners_text.setVisibility(partners_text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            //partners_icon.setImageResource(partners_text.getVisibility() == View.VISIBLE ? R.drawable.ic_baseline_keyboard_arrow_up_24 : R.drawable.ic_baseline_keyboard_arrow_down_24);
            Toast.makeText(this, "Partners section clicked", Toast.LENGTH_SHORT).show();
        });

    }

    public void setTextBullets(String[] text) {
        info_box_component.setTextBullets(text);
    }
}