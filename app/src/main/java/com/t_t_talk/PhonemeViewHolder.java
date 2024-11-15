package com.t_t_talk;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class PhonemeViewHolder extends RecyclerView.ViewHolder {
    private CardView phoneme_card;
    private StrokedTextView phoneme_display;
    private ConstraintLayout[] layout_stars = new ConstraintLayout[3];

    public PhonemeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.phoneme_card = itemView.findViewById(R.id.phoneme_card);
        this.phoneme_display = itemView.findViewById(R.id.phoneme_display);
        this.layout_stars[0] = itemView.findViewById(R.id.layout_star_1);
        this.layout_stars[1] = itemView.findViewById(R.id.layout_star_2);
        this.layout_stars[2] = itemView.findViewById(R.id.layout_star_3);
    }

    public void setStar(int starNum) {
        for(int i = 0; i < 3; i++) {
            if(i + 1 == starNum) {
                this.layout_stars[i].setVisibility(View.VISIBLE);
            } else {
                this.layout_stars[i].setVisibility(View.GONE);
            }
        }
    }

    public void setText(String text) {
        this.phoneme_display.setText(text);
    }

    public void setOnclick(View.OnClickListener listener) {
        this.phoneme_card.setOnClickListener(listener);
    }
}
