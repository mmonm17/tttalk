package com.t_t_talk;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.t_t_talk.DB.AppDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceViewHolder> {
    MediaPlayer mediaPlayer;
    TextToSpeech textToSpeech;
    boolean playedAudio = false;
    String[] sentences;
    boolean[] sentenceCompletions;
    String highlighted;
    AppCompatActivity context;
    EventCallback callback;
    String language;
    private Map<String, String> phonemeAudioMap = new HashMap<>();
    int currentlyPlaying = -1;
    boolean isPlaying;
    int currentRecording = -1;
    boolean isRecording;
    private SentenceViewHolder currentlySpeakingHolder;
    private SentenceViewHolder previousSpeakingHolder;
    private Handler uiHandler;

    private MediaRecorder mediaRecorder;
    private Uri audioUri;

    String languageLower;
    String phonemeLower;
    AppDatabase db;
    String phonemeCode;
    String levelCode;

    public SentenceAdapter(String[] sentences, String highlighted, String language, AppCompatActivity activity, String levelCode, String phonemeCode){
        this.sentences = sentences;
        this.languageLower = language.toLowerCase();
        this.phonemeLower = highlighted.toLowerCase();
        populatePhonemeAudioMap(sentences, phonemeLower, languageLower);

        this.sentenceCompletions = new boolean[this.sentences.length];

        for(int i = 0; i < this.sentenceCompletions.length; i++) {
            sentenceCompletions[i] = false;
        }

        this.highlighted = highlighted;
        this.language = language;
        this.context = activity;
        this.db = new AppDatabase(context);
        this.phonemeCode = phonemeCode;
        this.levelCode = levelCode;
        this.callback = new EventCallback() {
            @Override
            public void onClick(int position, String levelCode, String phonemeCode) {
                sentenceCompletions[position] = true;
                int starCount = computeStars();
                db.updatePhonemeProgress(levelCode, phonemeCode, starCount);

                if (checkAllForCompletion()) {
                    Intent i = new Intent(context, ProgressActivity.class);
                    i.putExtra("star_count", starCount);
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
        phoneme = phoneme.toLowerCase();
        language = language.toLowerCase();

        for (int j = 0; j < sentences.length; j++) {
            String sentence = sentences[j];
            String baseKey = language + "_" + phoneme;

            int variantIndex = j + 1;
            String variantKey = baseKey + "_" + variantIndex;
            String audioFileName = variantKey + ".mp3";

            phonemeAudioMap.put(variantKey, audioFileName);
        }
    }

    private void initializeTextToSpeech() {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    if(language.equals("Tagalog")){
                        Locale targetLocale = new Locale("fil", "PH"); 
                        if (textToSpeech.isLanguageAvailable(targetLocale) >= TextToSpeech.LANG_AVAILABLE) {
                            textToSpeech.setLanguage(targetLocale);
                        } else {
                            Log.w("TTS", "Tagalog TTS not available, falling back to default locale");
                            textToSpeech.setLanguage(Locale.getDefault()); 
                        }
                    } else {
                        Locale targetLocale = new Locale("en", "US"); // English (United States)
                        if (textToSpeech.isLanguageAvailable(targetLocale) >= TextToSpeech.LANG_AVAILABLE) {
                            textToSpeech.setLanguage(targetLocale);
                        } else {
                            Log.w("TTS", "English TTS not available, falling back to default locale");
                            textToSpeech.setLanguage(Locale.getDefault()); 
                        }
                    }
                    uiHandler = new Handler(context.getMainLooper());
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            currentlySpeakingHolder.sentenceViewBox.switchPlayIcon(true);
                            currentlySpeakingHolder.sentenceViewBox.setBtnPlayColor(context.getColor(R.color.red));
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            // Reset button color or handle post-speech logic
                            context.runOnUiThread(() -> {
                                isPlaying = false;
                                currentlyPlaying = -1;
                                currentlySpeakingHolder.sentenceViewBox.switchPlayIcon(false);
                                currentlySpeakingHolder.sentenceViewBox.setBtnPlayColor(context.getColor(R.color.green_light));
                            });
                        }

                        @Override
                        public void onStop(String utteranceId, boolean interrupted) {
                            super.onStop(utteranceId, interrupted);

                            uiHandler.post(() -> {
                                isPlaying = false;
                                currentlyPlaying = -1;
                                previousSpeakingHolder.sentenceViewBox.switchPlayIcon(false);
                                previousSpeakingHolder.sentenceViewBox.setBtnPlayColor(context.getColor(R.color.green_light));
                            });
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.e("TTS", "Error occurred for utterance ID: " + utteranceId);
                        }
                    });
                } else {
                    Log.e("TTS", "TextToSpeech initialization failed");
                }
            });
        } else {
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
            if(isPlaying && mediaPlayer != null) {
                if (currentlyPlaying == holder.getAdapterPosition()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlaying = false;
                    currentlyPlaying = -1;
                    holder.sentenceViewBox.switchPlayIcon(false);
                    holder.sentenceViewBox.setBtnPlayColor(context.getColor(R.color.green_light));
                    return;
                } else {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlaying = false;
                    currentlyPlaying = -1;
                    currentlySpeakingHolder.sentenceViewBox.switchPlayIcon(false);
                    currentlySpeakingHolder.sentenceViewBox.setBtnPlayColor(context.getColor(R.color.green_light));
                }
            }

            previousSpeakingHolder = currentlySpeakingHolder;
            currentlySpeakingHolder = holder;
            isPlaying = true;
            currentlyPlaying = holder.getAdapterPosition();

            holder.sentenceViewBox.switchPlayIcon(true);
            holder.sentenceViewBox.setBtnPlayColor(context.getColor(R.color.red));
            String baseKey = languageLower + "_" + phonemeLower;

            int variantIndex = position + 1;
            String variantKey = baseKey + "_" + variantIndex;
            String audioFileName = phonemeAudioMap.get(variantKey);


            if (audioFileName != null) {
                int resId = context.getResources().getIdentifier(audioFileName.replace(".mp3", ""), "raw", context.getPackageName());
                if (resId != 0) {
                    mediaPlayer = MediaPlayer.create(context, resId);
                    mediaPlayer.setOnCompletionListener(mp -> {
                        mp.release();
                        mediaPlayer = null;
                        isPlaying = false;
                        currentlyPlaying = -1;
                        holder.sentenceViewBox.switchPlayIcon(false);
                        holder.sentenceViewBox.setBtnPlayColor(context.getColor(R.color.green_light));
                    });
                    mediaPlayer.start();
                    playedAudio = true;
                } else {
                    playedAudio = false;
                }
            } else {
                playedAudio = false;
            }

            if (!playedAudio) {
                Log.d("TTS", "Fallback to TextToSpeech");
                if (textToSpeech != null) {
                    textToSpeech.speak(current, TextToSpeech.QUEUE_FLUSH, null, "TTS_" + position);
                }
            }
        });

        holder.sentenceViewBox.setMicButtonListener(view -> {
            if(isPlaying || (isRecording && currentRecording != holder.getAdapterPosition()) ) { // || holder.sentenceViewBox.getSubmitted()
                return;
            }

            if (isRecording) {
                stopRecording();
                holder.sentenceViewBox.setBtnMicColor(context.getColor(R.color.orange));
                holder.sentenceViewBox.switchMicIcon(false);
                isRecording = false;
                holder.sentenceViewBox.setCorrectFeedback(levelCode, phonemeCode);
            } else {
                isRecording = true;
                currentRecording = holder.getAdapterPosition();
                holder.sentenceViewBox.setBtnMicColor(context.getColor(R.color.red));
                holder.sentenceViewBox.switchMicIcon(true);
                holder.sentenceViewBox.resetFeedback();
                startRecordingForSentence(position);
            }
        });
    }

    private void startRecordingForSentence(int position) {
        // Get the content resolver
        ContentValues values = new ContentValues();
        String filename = languageLower + "_" + phonemeLower + "_" + (position + 1) + ".3gp";
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename); 
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/3gpp"); 
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC); // Save in Music folder

        // Insert into MediaStore to get a Uri
        audioUri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);

        if (audioUri == null) {
            Log.e("Recording", "Failed to create new MediaStore entry");
            return;
        }

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioSamplingRate(44100); 
            mediaRecorder.setAudioEncodingBitRate(64000);
            mediaRecorder.setOutputFile(context.getContentResolver().openFileDescriptor(audioUri, "w").getFileDescriptor()); 

            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.d("Recording", "Recording started for sentence " + position);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Recording", "Error preparing or starting recording");
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e("Recording", "Permission denied for recording");
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            Log.d("Recording", "Recording stopped");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
            context.getContentResolver().update(audioUri, values, null, null);

            Toast.makeText(context, "Recording saved", Toast.LENGTH_SHORT).show();
        }
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
        int completed_sentences = 0;
        for(boolean b: this.sentenceCompletions) {
            if(b) {
                completed_sentences++;
            }
        }

        float progress_percentage = (float) completed_sentences / (float) this.sentenceCompletions.length;
        if (progress_percentage > 0.99) {
            return 3;
        } else if (progress_percentage > 0.66) {
            return 2;
        } else if (progress_percentage > 0.33) {
            return 1;
        } else {
            return 0;
        }
    }

    interface EventCallback {
        void onClick(int position, String levelCode, String phonemeCode);
    }
}
