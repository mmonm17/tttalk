package com.t_t_talk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DataTypes.Phoneme;

import java.util.ArrayList;

public class PhonemeAdapter extends RecyclerView.Adapter<PhonemeViewHolder> {
    public Context context;
    private ArrayList<Phoneme> data;
    public String language;

    public PhonemeAdapter(AppCompatActivity activity, ArrayList<Phoneme> data, String language) {
        this.context = activity;
        this.data = data;
        this.language = language;
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
        holder.setText(current.getCode());
        holder.setOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if(language.equals("English")) {
                    i = new Intent(context, PhonemeEnglishActivity.class);
                } else {
                    i = new Intent(context, PhonemeTagalogActivity.class);
                }
                i.putExtra("Sentences", current.getSentences());
                i.putExtra("PhonemeCode", current.getCode());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
