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

public class InfoBoxComponent extends RelativeLayout {
    private TextView title;
    private TextView content;
    private CardView card_title;

    public InfoBoxComponent(Context context) {
        super(context);
        init(context);
    }

    public InfoBoxComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InfoBoxComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.info_box_component, this, true);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        card_title = findViewById(R.id.card_title);
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public void setContent(String contentText) {
        content.setText(contentText);
        //might improve this later
    }

    public void setCardTitleColor(int color) {
        card_title.setCardBackgroundColor(color);
    }

}
