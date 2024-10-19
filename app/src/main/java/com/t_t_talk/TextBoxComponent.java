package com.t_t_talk;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class TextBoxComponent extends LinearLayout {
    private boolean submitted;
    private int position;
    private TextView tv_dynamic_text;
    private EditText et_user_input;
    private CardView btn_play;
    private CardView btn_mic;
    private SentenceAdapter.EventCallback callback;
    private ImageView circular_feedback;
    private CardView circular_feedback_check;
    private CardView circular_feedback_close;

    public TextBoxComponent(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TextBoxComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextBoxComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.text_box_component_template, this, true);

        tv_dynamic_text = findViewById(R.id.tv_dynamic_text);
        et_user_input = findViewById(R.id.et_user_input);
        btn_play = findViewById(R.id.btn_play);
        btn_mic = findViewById(R.id.btn_mic);
        circular_feedback = findViewById(R.id.circular_feedback);
        circular_feedback_check = findViewById(R.id.circular_feedback_check);
        circular_feedback_close = findViewById(R.id.circular_feedback_close);

        btn_play.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Reading text...", Toast.LENGTH_SHORT).show();
        });

        btn_mic.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Mic button for 3 secs", Toast.LENGTH_SHORT).show();

            resetFeedback();

            // Set the button tint to red
            btn_mic.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.red)));

            new CountDownTimer(3000, 50) {

                @Override
                public void onTick(long arg0) {
                    // No action needed on each tick
                }

                @Override
                public void onFinish() {
                    // Change the tint to orange after the timer finishes
                    btn_mic.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.orange)));
                    // Change the feedback to check
                    setCorrectFeedback();
                    Toast.makeText(getContext(), "Complete all 4 to star up", Toast.LENGTH_SHORT).show();
                }
            }.start();

        });
        submitted = false;
    }

    // CUSTOM METHODS (Programmable from the caller)
    /*
    public void setReadTextClickListener(OnClickListener listener) {
        btn_play.setOnClickListener(listener);
    }

    public void setSubmitClickListener(OnClickListener listener) {
        btn_mic.setOnClickListener(listener);
    }
     */

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

    public void setCorrectFeedback(){
        circular_feedback.setVisibility(VISIBLE);
        circular_feedback_check.setVisibility(VISIBLE);
        circular_feedback_close.setVisibility(GONE);
        submitted = true;

        Random random = new Random();
        int mistakes = random.nextInt(2);
        callback.onClick(this.position, mistakes);
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
