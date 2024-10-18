package com.t_t_talk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DataTypes.Phoneme;

import java.util.ArrayList;

public class PhonemeAdapter extends RecyclerView.Adapter<PhonemeViewHolder> {
    private ArrayList<Phoneme> data;

    public PhonemeAdapter(ArrayList<Phoneme> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PhonemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.phoneme_box_component, parent, false);
        return new PhonemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhonemeViewHolder holder, int position) {
        Phoneme current = data.get(position);
        holder.setStar(current.getStarCount());
        holder.setText(current.getCode().getStr());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
