package com.t_t_talk;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InfoViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView content;

    public InfoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.title);
        this.content = itemView.findViewById(R.id.content);
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public void setContent(String[] contentText) {
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();

        for (String bullet : contentText) {
            int indentLevel = 0;
            while (bullet.startsWith(" ")) {
                indentLevel++;
                bullet = bullet.substring(1);
            }

            SpannableStringBuilder spannableBullet = new SpannableStringBuilder(bullet.trim());
            int bulletMargin = 40 * indentLevel;

            // Apply leading margin span for indentation
            if (indentLevel > 0) {
                spannableBullet.setSpan(new LeadingMarginSpan.Standard(bulletMargin), 0, spannableBullet.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // Apply bullet span only if indentLevel is greater than 0
            if (indentLevel > 0) {
                spannableBullet.setSpan(new BulletSpan(20), 0, spannableBullet.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // Apply bold and underline spans
            applyFormattingSpans(spannableBullet);

            spannableBuilder.append(spannableBullet).append("\n");
        }

        content.setText(spannableBuilder);
    }

    private void applyFormattingSpans(SpannableStringBuilder spannableBullet) {
        String text = spannableBullet.toString();

        // Apply bold formatting
        int start = text.indexOf("**");
        while (start != -1) {
            int end = text.indexOf("**", start + 2);
            if (end != -1) {
                spannableBullet.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableBullet.replace(end, end + 2, "");
                spannableBullet.replace(start, start + 2, "");
                text = spannableBullet.toString(); // Update text after deletion
            }
            start = text.indexOf("**", start);
        }

        // Apply underline formatting
        start = text.indexOf("__");
        while (start != -1) {
            int end = text.indexOf("__", start + 2);
            if (end != -1) {
                spannableBullet.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableBullet.replace(end, end + 2, "");
                spannableBullet.replace(start, start + 2, "");
                text = spannableBullet.toString(); // Update text after deletion
            }
            start = text.indexOf("__", start);
        }
    }



}
