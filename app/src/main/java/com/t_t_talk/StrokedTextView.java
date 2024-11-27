package com.t_t_talk;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class StrokedTextView extends androidx.appcompat.widget.AppCompatTextView {

    private static final float DEFAULT_STROKE_WIDTH = 1.2f;

    // Dields
    private int _strokeColor;
    private float _strokeWidth;

    // Constructors
    public StrokedTextView(Context context) {
        this(context, null, 0);
    }

    public StrokedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokedTextView);
            _strokeColor = a.getColor(R.styleable.StrokedTextView_textStrokeColor,
                    getCurrentTextColor());
            _strokeWidth = a.getFloat(R.styleable.StrokedTextView_textStrokeWidth,
                    DEFAULT_STROKE_WIDTH);

            a.recycle();
        }
        else {
            _strokeColor = getCurrentTextColor();
            _strokeWidth = DEFAULT_STROKE_WIDTH;
        }
        // Convert values specified in dp in XML layout to
        // px, otherwise stroke width would appear different
        // on different screens
        _strokeWidth = dpToPx(context, _strokeWidth);
    }

    // Getters + Setters
    public void setStrokeColor(int color) {
        _strokeColor = color;
    }

    public void setStrokeWidth(int width) {
        _strokeWidth = width;
    }

    // Overridden Methods
    @Override
    protected void onDraw(Canvas canvas) {
        if(_strokeWidth > 0) {
            // Set paint to fill mode
            Paint p = getPaint();
            p.setStyle(Paint.Style.FILL);
            // Draw the fill part of text
            super.onDraw(canvas);
            // Save the text color
            int currentTextColor = getCurrentTextColor();
            // Set paint to stroke mode and specify
            // Stroke color and width
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(_strokeWidth);
            setTextColor(_strokeColor);
            // Draw text stroke
            super.onDraw(canvas);
            // Revert the color back to the one
            // Initially specified
            setTextColor(currentTextColor);
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * Convenience method to convert density independent pixel(dp) value
     * into device display specific pixel value.
     * @param context Context to access device specific display metrics
     * @param dp density independent pixel value
     * @return device specific pixel value.
     */
    public static int dpToPx(Context context, float dp)
    {
        final float scale= context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}