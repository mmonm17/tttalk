package com.t_t_talk;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

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
        LayoutInflater.from(context).inflate(R.layout.component_info_box, this, true);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        card_title = findViewById(R.id.card_title);
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public void setCardTitleColor(int color) {
        card_title.setCardBackgroundColor(color);
    }

    public void setTextBullets(String[] bullets) {
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();

        for (String bullet : bullets) {
            int indentLevel = 0;
            while (bullet.startsWith(" ")) {
                indentLevel++;
                bullet = bullet.substring(1);
            }

            SpannableString spannableBullet = new SpannableString(bullet.trim());
            int bulletMargin = 40 * indentLevel;

            if (indentLevel > 0) {
                spannableBullet.setSpan(new LeadingMarginSpan.Standard(bulletMargin), 0, bullet.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            spannableBullet.setSpan(new BulletSpan(20), 0, bullet.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        content.setText(spannableBuilder);
    }

}
