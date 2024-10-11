package com.t_t_talk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CurvedTextView extends View {

    private Paint textPaint;
    private Path arcPath;
    private String text = "LANGUAGE";

    public CurvedTextView(Context context) {
        super(context);
        init();
    }

    public CurvedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.RED); // Set text color here
        textPaint.setTextSize(100f); // Adjust text size as needed
        textPaint.setStyle(Paint.Style.FILL);

        arcPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Define the arc (adjust values for curvature and position)
        arcPath.addArc(new RectF(100, 100, 800, 600), 180, 180); // You can change the RectF dimensions for different curves

        // Draw the text along the path
        canvas.drawTextOnPath(text, arcPath, 0, 0, textPaint);
    }
}
