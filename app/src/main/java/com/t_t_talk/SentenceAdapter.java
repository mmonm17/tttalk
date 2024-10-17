package com.t_t_talk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.ViewHolder> {
    String[] sentences;
    Context context;

    public SentenceAdapter(String[] sentences, PhonemeEnglishActivity activity){
        this.sentences = sentences;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.text_box_component,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String current = sentences.get(position);
        final MyMovieData myMovieDataList = myMovieData[position];
        holder.textViewName.setText(myMovieDataList.getMovieName());
        holder.textViewDate.setText(myMovieDataList.getMovieDate());
        holder.movieImage.setImageResource(myMovieDataList.getMovieImage());
        // itemView can be changed to specific element u want to be clickable
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, myMovieDataList.getMovieName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MovieActivity.class);
                i.putExtra("image",myMovieDataList.getMovieImage());
                i.putExtra("name",myMovieDataList.getMovieName());
                i.putExtra("date",myMovieDataList.getMovieDate());
                i.putExtra("summary",myMovieDataList.getMovieSummary());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sentences.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView movieImage;
        TextView textViewName;
        TextView textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageview);
            textViewName = itemView.findViewById(R.id.textName);
            textViewDate = itemView.findViewById(R.id.textdate);

        }
    }
}
