package com.t_t_talk;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.Map;
import java.util.Random;

public class SentenceBoxComponent extends LinearLayout {
    private boolean submitted;
    private int position;
    private TextView tv_dynamic_text;
    private EditText et_user_input;
    private CardView btn_play;
    private CardView btn_mic;
    private ImageView btn_play_icon;
    private ImageView btn_mic_icon;
    private SentenceAdapter.EventCallback callback;
    private ImageView circular_feedback;
    private CardView circular_feedback_check;
    private CardView circular_feedback_close;

    boolean isPlaying;
    boolean isRecording;

    private Map<String, String> phonemeAudioMap;

    public void setPhonemeAudioMap(Map<String, String> phonemeAudioMap) {
        this.phonemeAudioMap = phonemeAudioMap;
    }

    public SentenceBoxComponent(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SentenceBoxComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SentenceBoxComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.component_sentence_box, this, true);

        tv_dynamic_text = findViewById(R.id.tv_dynamic_text);
        et_user_input = findViewById(R.id.et_user_input);
        btn_play = findViewById(R.id.btn_play);
        btn_mic = findViewById(R.id.btn_mic);
        btn_play_icon = findViewById(R.id.btn_play_icon);
        btn_mic_icon = findViewById(R.id.btn_mic_icon);
        circular_feedback = findViewById(R.id.circular_feedback);
        circular_feedback_check = findViewById(R.id.circular_feedback_check);
        circular_feedback_close = findViewById(R.id.circular_feedback_close);

        submitted = false;
    }

    public void setMicButtonListener(OnClickListener listener) {
        btn_mic.setOnClickListener(listener);
    }

    public void setPlayButtonListener(OnClickListener listener) {
        btn_play.setOnClickListener(listener);
    }

    public void setText(String sentence) {
        tv_dynamic_text.setText(sentence);
    }

    public void setTypeAnswer(){
        tv_dynamic_text.setVisibility(GONE);
    }

    public void setTypeRead(){
        et_user_input.setVisibility(INVISIBLE);
        tv_dynamic_text.setVisibility(VISIBLE);
    }

    public void resetFeedback(){
        circular_feedback.setVisibility(GONE);
        circular_feedback_check.setVisibility(GONE);
        circular_feedback_close.setVisibility(GONE);
        submitted = false;
    }

    public void setCorrectFeedback(String levelCode, String phonemeCode){
        circular_feedback.setVisibility(VISIBLE);
        circular_feedback_check.setVisibility(VISIBLE);
        circular_feedback_close.setVisibility(GONE);
        submitted = true;

        callback.onClick(this.position, levelCode, phonemeCode);
    }

    public void setIncorrectFeedback(){
        circular_feedback.setVisibility(VISIBLE);
        circular_feedback_check.setVisibility(GONE);
        circular_feedback_close.setVisibility(VISIBLE);
        submitted = true;
    }

    public void setBtnPlayColor(int color){
        btn_play.setCardBackgroundColor(color);
    }

    public void setBtnMicColor(int color){
        btn_mic.setCardBackgroundColor(color);
    }

    public void switchPlayIcon(boolean isPlaying) {
        if (isPlaying) {
            btn_play_icon.setImageResource(R.drawable.ic_replay);
            //setBtnPlayColor(R.color.red);
        } else {
            btn_play_icon.setImageResource(R.drawable.ic_play_arrow);
            //setBtnPlayColor(R.color.green_light);
        }
    }


    public void switchMicIcon(boolean isRecording) {
        if (isRecording) {
            btn_mic_icon.setImageResource(R.drawable.ic_stop);
        } else {
            btn_mic_icon.setImageResource(R.drawable.ic_mic);
        }
    }


    public void setHighlightedText(Context context, String text, String substring) {
        SpannableString spannableString = new SpannableString(text);

        // Convert both text and substring to lower case for case-insensitive comparison
        String lowerCaseText = text.toLowerCase();
        String lowerCaseSubstring = substring.toLowerCase();

        int startIndex = lowerCaseText.indexOf(lowerCaseSubstring);

        // Highlight all occurrences of the substring
        while (startIndex >= 0) {
            spannableString.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(context, R.color.primary)),
                    startIndex, startIndex + substring.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            // Look for the next occurrence of the substring
            startIndex = lowerCaseText.indexOf(lowerCaseSubstring, startIndex + lowerCaseSubstring.length());
        }

        tv_dynamic_text.setText(spannableString);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean getSubmitted() {
        return submitted;
    }

    public void setCallback(SentenceAdapter.EventCallback callback) {
        this.callback = callback;
    }
}
