package com.t_t_talk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DB.Models.Level;

import java.io.Serializable;
import java.util.ArrayList;
import android.widget.ProgressBar;

public class LevelBoxAdapter extends RecyclerView.Adapter<LevelViewHolder> {
    public Context context;
    private ArrayList<Level> data;
    private String language;
    private ProgressBar progress_bar;

    public LevelBoxAdapter(AppCompatActivity context, ArrayList<Level> data, String language, ProgressBar progress_bar) {
        this.context = context;
        this.data = data;
        this.language = language;
        this.progress_bar = progress_bar;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.component_level_box, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        Level current = data.get(position);
        holder.setBackgroundColor(current.getColor());
        if(language.equals("English")) {
            holder.setText("LEVEL " + current.getLevelNumber() + " (Age " + current.getAge() + ")");
        } else {
            holder.setText("LEBEL " + current.getLevelNumber() + " (" + current.getAge() + " Anyos)");
        }

        Animation scale = AnimationUtils.loadAnimation(context, R.anim.scale);
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
        holder.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(scale);
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.startAnimation(rotate);
                Intent i;
                if(language.equals("English")) {
                    i = new Intent(context, PhonemeSelectEnglishActivity.class);
                } else {
                    i = new Intent(context, PhonemeSelectTagalogActivity.class);
                }

                Bundle phonemes = new Bundle();
                phonemes.putSerializable("Phonemes", (Serializable) current.getPhonemeList());
                i.putExtra("Phonemes", phonemes);
                i.putExtra("LevelCode", current.getCode());
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
