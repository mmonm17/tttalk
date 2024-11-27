package com.t_t_talk;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LevelViewHolder extends RecyclerView.ViewHolder {
    private LevelBoxComponent textBox;

    public LevelViewHolder(@NonNull View itemView) {
        super(itemView);
        this.textBox = itemView.findViewById(R.id.text_box);
    }

    public void setText(String text) {
        this.textBox.setText(text);
    }

    public void setBackgroundColor(int color) {
        this.textBox.setBackgroundColor(color);
    }

    public void setOnclickListener(View.OnClickListener listener) {
        textBox.setOnClickListener(listener);
    }
}
