package com.t_t_talk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DB.Models.Phoneme;

import java.util.ArrayList;

public class PhonemeAdapter extends RecyclerView.Adapter<PhonemeViewHolder> {
    public Context context;
    private ArrayList<Phoneme> data;
    public String language;
    private int levelNum;
    ProgressBar progress_bar;


    public PhonemeAdapter(AppCompatActivity activity, ArrayList<Phoneme> data, String language, int levelNum) {
        this.context = activity;
        this.data = data;
        this.language = language;
        this.levelNum = levelNum;
    }

    public PhonemeAdapter(AppCompatActivity activity, ArrayList<Phoneme> data, String language, int levelNum, ProgressBar progress_bar) {
        this.context = activity;
        this.data = data;
        this.language = language;
        this.levelNum = levelNum;
        this.progress_bar = progress_bar;
    }

    @NonNull
    @Override
    public PhonemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.component_phoneme_box, parent, false);
        return new PhonemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhonemeViewHolder holder, int position) {
        Phoneme current = data.get(position);
        holder.setStar(current.getStarCount());
        holder.setText(current.getCode());
        Animation fade = AnimationUtils.loadAnimation(context, R.anim.fade_bg);
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
        holder.setOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(fade);
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.startAnimation(rotate);
                Intent i;
                if(language.equals("English")) {
                    i = new Intent(context, PhonemeSoundEnglishActivity.class);
                } else {
                    i = new Intent(context, PhonemeSoundTagalogActivity.class);
                }
                i.putExtra("Sentences", current.getSentences().toArray(new String[0]));
                i.putExtra("PhonemeCode", current.getCode());
                i.putExtra("LevelNum", levelNum);
                progress_bar.setVisibility(View.INVISIBLE);
                progress_bar.clearAnimation();
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
