package com.t_t_talk;

import android.view.View;

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
}