package com.t_t_talk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class CurvedLanguageTextView extends View {

    private Paint textPaint;
    private Paint strokePaint;
    private Path arcPath;
    private String text = "LANGUAGE";
    private Typeface candyBeansFont;

    public CurvedLanguageTextView(Context context) {
        super(context);
        init(context);
    }

    public CurvedLanguageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        candyBeansFont = ResourcesCompat.getFont(context, R.font.candy_beans);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ContextCompat.getColor(context, R.color.primary)); // Set text color here
        textPaint.setTextSize(180f);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(candyBeansFont);
        textPaint.setLetterSpacing(0.1f);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(ContextCompat.getColor(context, R.color.primary_stroke));  // Set stroke color
        strokePaint.setTextSize(180f);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setTypeface(candyBeansFont);
        strokePaint.setStrokeWidth(10f);
        strokePaint.setLetterSpacing(0.1f);

        arcPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        arcPath.addArc(new RectF(50, 150, 950, 400), 180, 180);

        canvas.drawTextOnPath(text, arcPath, 30, 0, strokePaint);

        canvas.drawTextOnPath(text, arcPath, 30, 0, textPaint);
    }
}
