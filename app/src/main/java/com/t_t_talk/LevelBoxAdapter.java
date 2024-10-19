package com.t_t_talk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DataTypes.Level;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelBoxAdapter extends RecyclerView.Adapter<LevelViewHolder> {
    public Context context;
    private ArrayList<Level> data;
    private String language;

    public LevelBoxAdapter(AppCompatActivity context, ArrayList<Level> data, String language) {
        this.context = context;
        this.data = data;
        this.language = language;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.level_box_component, parent, false);
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

        holder.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if(language.equals("English")) {
                    i = new Intent(context, PhonemeSelectEnglish.class);
                } else {
                    i = new Intent(context, PhonemeSelectTagalog.class);
                }

                Bundle phonemes = new Bundle();
                phonemes.putSerializable("Phonemes", (Serializable) current.getPhonemeList());
                i.putExtra("Phonemes", phonemes);

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
