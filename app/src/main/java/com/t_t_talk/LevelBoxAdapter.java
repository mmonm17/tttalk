package com.t_t_talk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DataTypes.Level;

import java.util.ArrayList;

public class LevelBoxAdapter extends RecyclerView.Adapter<LevelViewHolder> {
    private ArrayList<Level> data;
    private String language;

    public LevelBoxAdapter(ArrayList<Level> data, String language) {
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
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
