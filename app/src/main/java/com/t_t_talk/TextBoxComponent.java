package com.t_t_talk;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.*;

import androidx.annotation.Nullable;

public class TextBoxComponent extends RelativeLayout {

    private TextView tvDynamicText;
    private EditText etUserInput;
    private Button btnReadText;
    private Button btnSubmit;
    private ImageView circularFeedback;

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

        // Initialize your views
        tvDynamicText = findViewById(R.id.tv_dynamic_text);
        etUserInput = findViewById(R.id.et_user_input);
        btnReadText = findViewById(R.id.btn_read_text);
        btnSubmit = findViewById(R.id.btn_submit);
        circularFeedback = findViewById(R.id.circular_feedback);

        btnReadText.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Reading text...", Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Submitting answer...", Toast.LENGTH_SHORT).show();
        });

    }

    // CUSTOM METHODS (Programmable from the caller)
    /*
    public void setReadTextClickListener(OnClickListener listener) {
        btnReadText.setOnClickListener(listener);
    }

    public void setSubmitClickListener(OnClickListener listener) {
        btnSubmit.setOnClickListener(listener);
    }

     */

    public void setTypeAnswer(){
        tvDynamicText.setVisibility(GONE);
        //btnSubmit.setVisibility(VISIBLE);
        //btnReadText.setVisibility(GONE);
    }

    public void setTypeRead(){
        etUserInput.setVisibility(GONE);
        //btnSubmit.setVisibility(GONE);
        //btnReadText.setVisibility(VISIBLE);
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

            // Set the SpannableString to the TextView
            tvDynamicText.setText(spannableString);
        }
    }

    // Add any other methods you need for your component
}
