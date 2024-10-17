package com.t_t_talk;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class LevelBoxComponent extends androidx.appcompat.widget.AppCompatTextView {
    private Context context;

    public LevelBoxComponent(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public LevelBoxComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LevelBoxComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setBackgroundColor(int color) {
        Drawable bg = ContextCompat.getDrawable(this.context, R.drawable.shape_level_box);
        assert bg != null;

        GradientDrawable bg_alter = (GradientDrawable) bg.mutate();
        bg_alter.setColor(color);
        this.setBackground(bg_alter);
    }
}
