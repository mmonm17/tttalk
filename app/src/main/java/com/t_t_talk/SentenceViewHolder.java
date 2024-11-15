package com.t_t_talk;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SentenceViewHolder extends RecyclerView.ViewHolder {
    public final SentenceBoxComponent sentenceViewBox;

    public SentenceViewHolder(@NonNull View itemView) {
        super(itemView);
        sentenceViewBox = itemView.findViewById(R.id.sentence_view_box);
    }

    public boolean getSubmitted() {
        return sentenceViewBox.getSubmitted();
    }
}
