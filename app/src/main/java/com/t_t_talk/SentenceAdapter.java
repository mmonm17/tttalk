package com.t_t_talk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceView> {
    String[] sentences;
    char highlighted;
    Context context;

    public SentenceAdapter(String[] sentences, char highlighted, PhonemeEnglishActivity activity){
        this.sentences = sentences;
        this.highlighted = highlighted;
        this.context = activity;
    }

    @NonNull
    @Override
    public SentenceView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sentence_view, parent,false);
        SentenceView viewHolder = new SentenceView(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceView holder, int position) {
        String current = sentences[position];
        holder.sentenceViewBox.setText(current);
        holder.sentenceViewBox.setTypeRead();
        holder.sentenceViewBox.setHighlightedTextByChar(context, current, highlighted);
    }

    @Override
    public int getItemCount() {
        return sentences.length;
    }
}
