package com.t_t_talk;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.*;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import com.t_t_talk.DataTypes.Info;

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
    String[] sampleText = {
            "This is a sample text",
            " This is another sample text in a bullet"
    };
    String sampleText2 = "Speaking slowly can be helpful for stuttering because it allows individuals to have more control over their speech. When speaking at a slower pace, it gives the person more time to plan and coordinate their words, which can reduce the likelihood of stuttering. It can also help decrease the tension and anxiety that often accompany stuttering.";


     InfoBoxComponent info_box_component;
     TextView info_text;
     TextView strategies_text;
     TextView partners_text;

     ImageView info_icon;
     ImageView strategies_icon;
     ImageView partners_icon;

     LinearLayout info_section;
     LinearLayout strategies_section;
     LinearLayout partners_section;

     RecyclerView recycler_view;
     LinearLayoutManager layoutManager;

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

        ArrayList<Info> data_info = new ArrayList<>();
        data_info.add(new Info("What are the characteristics of stuttered speech?", bullets));
        data_info.add(new Info("What are the main causes of stuttering?", sampleText));
        data_info.add(new Info("Question 3?", sampleText));
        data_info.add(new Info("Question 4?", sampleText));

        ArrayList<Info> data_strategies = new ArrayList<>();
        data_strategies.add(new Info("Avoid Trigger Words", sampleText));
        data_strategies.add(new Info("Practice Speaking Slowly", sampleText2));

        ArrayList<Info> data_partners = new ArrayList<>();
        data_partners.add(new Info("Caloocan, NCR, Philippines", sampleText2));
        data_partners.add(new Info("Makati, NCR, Philippines", sampleText2));


        //info_box_component = findViewById(R.id.info_box);
        info_text = findViewById(R.id.info_text);
        strategies_text = findViewById(R.id.strategies_text);
        partners_text = findViewById(R.id.partners_text);

        info_icon = findViewById(R.id.info_icon);
        strategies_icon = findViewById(R.id.strategies_icon);
        partners_icon = findViewById(R.id.partners_icon);

        info_section = findViewById(R.id.info_section);
        strategies_section = findViewById(R.id.strategies_section);
        partners_section = findViewById(R.id.partners_section);

        setUpRecyclerView(data_info);

        info_section.setOnClickListener(v -> {
            clearRecyclerView();
            setUpRecyclerView(data_info);

            info_text.setTextColor(ContextCompat.getColor(this, R.color.primary));
            info_icon.setColorFilter(ContextCompat.getColor(this, R.color.primary), PorterDuff.Mode.SRC_IN);
            strategies_text.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            strategies_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
            partners_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
            partners_text.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        });

        strategies_section.setOnClickListener(v -> {
           clearRecyclerView();
           setUpRecyclerView(data_strategies);

            info_text.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            info_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
            strategies_text.setTextColor(ContextCompat.getColor(this, R.color.primary));
            strategies_icon.setColorFilter(ContextCompat.getColor(this, R.color.primary), PorterDuff.Mode.SRC_IN);
            partners_text.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            partners_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
        });

        partners_section.setOnClickListener(v -> {
            clearRecyclerView();
            setUpRecyclerView(data_partners);
            info_text.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            info_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
            strategies_text.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            strategies_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
            partners_text.setTextColor(ContextCompat.getColor(this, R.color.primary));
            partners_icon.setColorFilter(ContextCompat.getColor(this, R.color.primary), PorterDuff.Mode.SRC_IN);

        });

    }

    private void setUpRecyclerView(ArrayList<Info> data) {
        this.recycler_view = findViewById(R.id.recycler_view);
        this.layoutManager = new LinearLayoutManager(this);
        this.recycler_view.setLayoutManager(layoutManager);

        InfoBoxAdapter adapter = new InfoBoxAdapter(data);
        this.recycler_view.setAdapter(adapter);
    }

    private void clearRecyclerView() {
        this.recycler_view.setAdapter(null);
    }

}