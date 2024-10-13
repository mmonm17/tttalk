package com.t_t_talk;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.*;
import androidx.cardview.widget.CardView;

import androidx.annotation.Nullable;

public class TextBoxComponent extends RelativeLayout {

    private TextView tv_dynamic_text;
    private EditText et_user_input;
    private CardView btn_play;
    private CardView btn_mic;

    private ImageView circular_feedback;
    private CardView circular_feedback_check;
    private CardView circular_feedback_close;

    public TextBoxComponent(Context context) {
        super(context);
        init(context);
    }

    public TextBoxComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextBoxComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Inflate the layout
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
            Toast.makeText(getContext(), "Submitting answer...", Toast.LENGTH_SHORT).show();
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
        //btnSubmit.setVisibility(VISIBLE);
        //btnReadText.setVisibility(GONE);
    }

    public void setTypeRead(){
        et_user_input.setVisibility(GONE);
        //btnSubmit.setVisibility(GONE);
        //btnReadText.setVisibility(VISIBLE);
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


    public void setHighlightedText(String text, char c, String color) {
        {
            SpannableString spannableString = new SpannableString(text);

            for (int i = 0; i < text.length(); i++) {
                char currentChar = text.charAt(i);
                if (currentChar == c || currentChar == c - 32 || currentChar == c + 32) {
                    spannableString.setSpan(
                            new ForegroundColorSpan(Color.parseColor(color)), // Orange color
                            i, i + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }
            tv_dynamic_text.setText(spannableString);
        }
    }

}
