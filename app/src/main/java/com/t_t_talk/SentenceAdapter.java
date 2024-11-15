package com.t_t_talk;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceViewHolder> {
    String[] sentences;
    boolean[] sentenceCompletions;
    int[] mistakeCount;
    String highlighted;
    AppCompatActivity context;
    EventCallback callback;
    String language;
    private Map<String, String> phonemeAudioMap = new HashMap<>();

    public SentenceAdapter(String[] sentences, String highlighted, String language, AppCompatActivity activity){
        this.sentences = sentences;
        populatePhonemeAudioMap(sentences, language);

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
    private void populatePhonemeAudioMap(String[] sentences, String language) {
        for (int j=0; j<sentences.length; j++) {
            String sentence = sentences[j];
            String[] words = sentence.split(" ");
            if (words.length > 0) {
                String phoneme = words[0]; // Example logic: First word as phoneme
                //set language first letter to lowercase
                language = language.substring(0, 1).toLowerCase() + language.substring(1);
                phoneme = phoneme.toLowerCase();
                String baseKey = language + "_" + phoneme;

                // Map all possible variants for this phoneme
                for (int i = 1; i <= 10; i++) { // Adjust max index as needed
                    String variantKey = baseKey + "_" + i;
                    String audioFileName = variantKey + ".mp3";
                    phonemeAudioMap.put(variantKey, audioFileName);
                    Log.d("Audio Map", "Phoneme: " + phoneme + ", Audio File: " + audioFileName);
                }
            }
            //remove the first word
            //sentences[j] = sentence.substring(sentence.indexOf(" ") + 1);
        }
    }

    @NonNull
    @Override
    public SentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_holder_sentence, parent,false);
        SentenceViewHolder viewHolder = new SentenceViewHolder(view);
        return viewHolder;
    }

    @Override
    /*public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        String current = sentences[position];
        holder.sentenceViewBox.setText(current);
        holder.sentenceViewBox.setTypeRead();
        holder.sentenceViewBox.setHighlightedText(context, current, highlighted);
        holder.sentenceViewBox.setCallback(callback);
        holder.sentenceViewBox.setPosition(position);
        //int audioResId = sentenceAudioMap.getOrDefault(current, -1); // Use -1 if no audio file is found.
        holder.sentenceViewBox.setPhonemeAudioMap(phonemeAudioMap);
    }*/

    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        String current = sentences[position];
        holder.sentenceViewBox.setText(current);
        holder.sentenceViewBox.setTypeRead();
        holder.sentenceViewBox.setHighlightedText(context, current, highlighted);
        holder.sentenceViewBox.setCallback(callback);
        holder.sentenceViewBox.setPosition(position);

        holder.sentenceViewBox.setPlayButtonListener(view -> {
            String phoneme = current.split(" ")[0]; // First word as phoneme
            language = language.substring(0, 1).toLowerCase() + language.substring(1);
            phoneme = phoneme.toLowerCase();
            String baseKey = language + "_" + phoneme;

            Log.d("setPlayButtonListener", "Phoneme: " + phoneme + ", Base Key: " + baseKey);

            boolean audioPlayed = false;

            // Check for available audio variants (e.g., A_1, A_2, ...)
            for (int i = 1; i <= 10; i++) { // Adjust max index as needed
                String variantKey = baseKey + "_" + i;
                String audioFileName = phonemeAudioMap.get(variantKey);

                if (audioFileName != null) {
                    int resId = context.getResources().getIdentifier(audioFileName.replace(".mp3", ""), "raw", context.getPackageName());
                    if (resId != 0) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, resId);
                        mediaPlayer.setOnCompletionListener(mp -> mp.release());
                        mediaPlayer.start();
                        audioPlayed = true;
                        break; // Play the first valid variant and exit loop
                    }
                }
            }

            if (!audioPlayed) {
                Toast.makeText(context, "Audio file not found for " + phoneme, Toast.LENGTH_SHORT).show();
            }
        });
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
