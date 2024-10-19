package com.t_t_talk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceView> {
    String[] sentences;
    boolean[] sentenceCompletions;
    int[] mistakeCount;
    String highlighted;
    AppCompatActivity context;
    EventCallback callback;
    String language;

    public SentenceAdapter(String[] sentences, String highlighted, String language, AppCompatActivity activity){
        this.sentences = sentences;
        this.sentenceCompletions = new boolean[this.sentences.length];

        for(int i = 0; i < this.sentenceCompletions.length; i++) {
            sentenceCompletions[i] = false;
        }

        this.mistakeCount = new int[this.sentences.length];

        for(int i = 0; i < this.mistakeCount.length; i++) {
            mistakeCount[i] = 0;
        }

        this.highlighted = highlighted;
        this.language = language;
        this.context = activity;
        this.callback = new EventCallback() {
            @Override
            public void onClick(int position, int mistakes) {
                sentenceCompletions[position] = true;
                mistakeCount[position] = mistakes;

                if(checkAllForCompletion()) {
                    Intent i = new Intent(context, ProgressActivity.class);
                    i.putExtra("star_count", computeStars());
                    i.putExtra("language", language);
                    i.putExtra("phoneme", highlighted);
                    context.startActivity(i);
                    context.finish();
                }
            }
        };
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
        holder.sentenceViewBox.setHighlightedText(context, current, highlighted);
        holder.sentenceViewBox.setCallback(callback);
        holder.sentenceViewBox.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return sentences.length;
    }

    public boolean checkAllForCompletion() {
        for(boolean b: this.sentenceCompletions) {
            if(!b) {
                return false;
            }
        }

        return true;
    }

    public int computeStars() {
        int total = 0;
        for(int mistake: mistakeCount) {
            total += mistake;
        }

        if(total < 2) {
            return 3;
        } else if(total < 4) {
            return 2;
        } else if(total < 6) {
            return 1;
        } else {
            return 0;
        }
    }

    interface EventCallback {
        void onClick(int position, int mistakes);
    }
}
