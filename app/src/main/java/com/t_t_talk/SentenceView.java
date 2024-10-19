package com.t_t_talk;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SentenceView extends RecyclerView.ViewHolder {
    public final TextBoxComponent sentenceViewBox;

    public SentenceView(@NonNull View itemView) {
        super(itemView);
        sentenceViewBox = itemView.findViewById(R.id.sentence_view_box);
    }

    public boolean getSubmitted() {
        return sentenceViewBox.getSubmitted();
    }
}
