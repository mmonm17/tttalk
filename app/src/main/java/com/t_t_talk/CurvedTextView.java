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

public class CurvedTextView extends View {

    private Paint textPaint;
    private Paint strokePaint;
    private Path arcPath;
    private String text = "LANGUAGE";
    private Typeface candyBeansFont;
    private RectF rectangle = new RectF(75, 150, 713, 400);

    public CurvedTextView(Context context) {
        super(context);
        init(context);
    }

    public CurvedTextView(Context context, AttributeSet attrs) {
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

        arcPath.addArc(rectangle, 180, 180);

        canvas.drawTextOnPath(text, arcPath, 30, 0, strokePaint);

        canvas.drawTextOnPath(text, arcPath, 30, 0, textPaint);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();

        float textLen = (float) text.length();
        int left = (int) (150.0 - textLen * 12.5);
        int right = (int) (textLen / 8.0 * 950.0);

        this.rectangle = new RectF(left, 150, right, 400);
    }

    public void setColor(int textColor, int strokeColor){
        textPaint.setColor(textColor);
        strokePaint.setColor(strokeColor);
    }
}
