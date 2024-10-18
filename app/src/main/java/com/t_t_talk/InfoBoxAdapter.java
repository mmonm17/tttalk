package com.t_t_talk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DataTypes.Info;

import java.util.ArrayList;


public class InfoBoxAdapter extends RecyclerView.Adapter<InfoViewHolder> {
    private ArrayList<Info> data;

    public InfoBoxAdapter(ArrayList<Info> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.info_box_component, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        Info current = data.get(position);
        holder.setTitle(current.getTitle());
        holder.setContent(current.getContent());
    }


    @Override
    public int getItemCount() {
        return this.data.size();
    }

}
