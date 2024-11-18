package com.t_t_talk;

import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceViewHolder> {
    MediaPlayer mediaPlayer;
    TextToSpeech textToSpeech;
    boolean playedAudio = false;
    String[] sentences;
    boolean[] sentenceCompletions;
    int[] mistakeCount;
    String highlighted;
    AppCompatActivity context;
    EventCallback callback;
    String language;
    String phoneme;
    String language_lowercase;
    private Map<String, String> phonemeAudioMap = new HashMap<>();

    public SentenceAdapter(String[] sentences, String highlighted, String language, AppCompatActivity activity){
        this.sentences = sentences;
        populatePhonemeAudioMap(sentences, highlighted, language.toLowerCase());

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

                if (checkAllForCompletion()) {
                    Intent i = new Intent(context, ProgressActivity.class);
                    i.putExtra("star_count", computeStars());
                    i.putExtra("language", language);
                    i.putExtra("phoneme", highlighted);
                    context.startActivity(i);
                    context.finish();
                }
            }

        };

        initializeTextToSpeech();
    }
    private void populatePhonemeAudioMap(String[] sentences, String phoneme, String language) {
        // Ensure phoneme and language are lowercase for consistency
        phoneme = phoneme.toLowerCase();
        language = language.toLowerCase();

        for (int j = 0; j < sentences.length; j++) {
            String sentence = sentences[j];
            String baseKey = language + "_" + phoneme;

            // Use the sentence index (starting from 1) as the variant index
            int variantIndex = j + 1; // Start with _1 for the first sentence
            String variantKey = baseKey + "_" + variantIndex;
            String audioFileName = variantKey + ".mp3";

            // Map the variantKey to its corresponding audio file
            phonemeAudioMap.put(variantKey, audioFileName);
            Log.d("Audio Map", "Mapped: " + variantKey + " -> " + audioFileName);
        }
    }

    private void initializeTextToSpeech() {
        // Initialize TTS only if it is not already initialized
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    Locale targetLocale = new Locale("tl", "PH"); // Tagalog (Philippines)
                    if (textToSpeech.isLanguageAvailable(targetLocale) >= TextToSpeech.LANG_AVAILABLE) {
                        textToSpeech.setLanguage(targetLocale);
                    } else {
                        Log.w("TTS", "Tagalog TTS not available, falling back to default locale");
                        textToSpeech.setLanguage(Locale.getDefault()); // Fallback to default language
                    }

                    Log.d("TTS", "TextToSpeech engine initialized successfully");
                } else {
                    Log.e("TTS", "TextToSpeech initialization failed");
                }
            });
        } else {
            // Already initialized, log it or update the state as needed
            Log.d("TTS", "TextToSpeech already initialized");
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
    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        String current = sentences[position];
        holder.sentenceViewBox.setText(current);
        holder.sentenceViewBox.setTypeRead();
        holder.sentenceViewBox.setHighlightedText(context, current, highlighted);
        holder.sentenceViewBox.setCallback(callback);
        holder.sentenceViewBox.setPosition(position);

        holder.sentenceViewBox.setPlayButtonListener(view -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            String phoneme = highlighted.toLowerCase();
            language = language.toLowerCase();
            String baseKey = language + "_" + phoneme;

            int variantIndex = position + 1;
            String variantKey = baseKey + "_" + variantIndex;
            String audioFileName = phonemeAudioMap.get(variantKey);

            Log.d("PlayButton", "Variant Key: " + variantKey + ", File: " + audioFileName);

            if (audioFileName != null) {
                int resId = context.getResources().getIdentifier(audioFileName.replace(".mp3", ""), "raw", context.getPackageName());
                if (resId != 0) {
                    mediaPlayer = MediaPlayer.create(context, resId);
                    mediaPlayer.setOnCompletionListener(mp -> {
                        mp.release();
                        mediaPlayer = null;
                    });
                    mediaPlayer.start();
                    playedAudio = true;
                } else {
                    Toast.makeText(context, "Audio file not found in resources: " + audioFileName, Toast.LENGTH_SHORT).show();
                    playedAudio = false;
                }
            } else {
                Toast.makeText(context, "Audio mapping not found for " + variantKey, Toast.LENGTH_SHORT).show();
                playedAudio = false;
            }

            if (!playedAudio) {
                Log.d("TTS", "Fallback to TextToSpeech");

                if (textToSpeech != null) {
                    textToSpeech.speak(current, TextToSpeech.QUEUE_FLUSH, null, "TTS_AudioUnavailable");
                    Log.d("TTS", "Speaking text: " + current);
                } else {
                    Log.e("TTS", "TextToSpeech not initialized yet.");
                }
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
