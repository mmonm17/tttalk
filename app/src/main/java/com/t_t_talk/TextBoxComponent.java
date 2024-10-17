package com.t_t_talk;

import android.content.Context;
import android.graphics.Color;
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

public class TextBoxComponent extends RelativeLayout {
    private TextView tv_dynamic_text;
    private EditText et_user_input;
    private CardView btn_play;
    private CardView btn_mic;

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
        LayoutInflater.from(context).inflate(R.layout.text_box_component, this, true);

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
            Toast.makeText(getContext(), "Mic button...", Toast.LENGTH_SHORT).show();
            setCorrectFeedback();
        });

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

    public void setTypeAnswer(){
        tv_dynamic_text.setVisibility(GONE);
    }

    public void setTypeRead(){
        et_user_input.setVisibility(INVISIBLE);
        tv_dynamic_text.setVisibility(VISIBLE);
    }

    public void setCorrectFeedback(){
        circular_feedback.setVisibility(VISIBLE);
        circular_feedback_check.setVisibility(VISIBLE);
        circular_feedback_close.setVisibility(GONE);
    }

    public void setIncorrectFeedback(){
        circular_feedback.setVisibility(VISIBLE);
        circular_feedback_check.setVisibility(GONE);
        circular_feedback_close.setVisibility(VISIBLE);
    }

    public void setBtnPlayColor(int color){
        btn_play.setCardBackgroundColor(color);
    }


    public void setHighlightedTextByChar(Context context, String text, char c) {
        {
            SpannableString spannableString = new SpannableString(text);

            for (int i = 0; i < text.length(); i++) {
                char currentChar = text.charAt(i);
                if (currentChar == c || currentChar == c - 32 || currentChar == c + 32) {
                    spannableString.setSpan(
                            new ForegroundColorSpan(ContextCompat.getColor(context, R.color.primary)),
                            i, i + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }
            tv_dynamic_text.setText(spannableString);
        }
    }

    public void setHighlightedTextBySubstring(Context context, String text, String substring) {
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
}
