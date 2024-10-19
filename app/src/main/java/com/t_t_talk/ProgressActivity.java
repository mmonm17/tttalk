package com.t_t_talk;

import static androidx.constraintlayout.motion.widget.Key.VISIBILITY;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        ConstraintLayout layout_star_1 = findViewById(R.id.layout_star_1);
        ConstraintLayout layout_star_2 = findViewById(R.id.layout_star_2);
        ConstraintLayout layout_star_3 = findViewById(R.id.layout_star_3);

        ImageView img_star_1_1 = findViewById(R.id.img_star_1_1);
        ImageView img_star_2_1 = findViewById(R.id.img_star_2_1);
        ImageView img_star_2_2 = findViewById(R.id.img_star_2_2);
        ImageView img_star_3_1 = findViewById(R.id.img_star_3_1);
        ImageView img_star_3_2 = findViewById(R.id.img_star_3_2);
        ImageView img_star_3_3 = findViewById(R.id.img_star_3_3);

        // Get the number of stars from intent
//        Intent intent = getIntent();
//        int starCount = intent.getIntExtra("star_count", 3); // Default to 3 stars

        int starCount = 3;

        // Add animation to star
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        bounce.setInterpolator(new BounceInterpolator());

        switch(starCount){
            case 1:
                layout_star_1.setVisibility(View.VISIBLE);
                layout_star_2.setVisibility(View.GONE);
                layout_star_3.setVisibility(View.GONE);
                img_star_1_1.startAnimation(bounce);
                break;
            case 2:
                layout_star_1.setVisibility(View.GONE);
                layout_star_2.setVisibility(View.VISIBLE);
                layout_star_3.setVisibility(View.GONE);
                img_star_2_1.startAnimation(bounce);
                img_star_2_2.startAnimation(bounce);
                break;
            case 3:
                layout_star_1.setVisibility(View.GONE);
                layout_star_2.setVisibility(View.GONE);
                layout_star_3.setVisibility(View.VISIBLE);
                img_star_3_1.startAnimation(bounce);
                img_star_3_2.startAnimation(bounce);
                img_star_3_3.startAnimation(bounce);
                break;
        }
    }
}