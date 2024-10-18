package com.t_t_talk;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
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
        if(contentText.length == 1) {
            SpannableString spannableBullet = new SpannableString(contentText[0]);
            spannableBuilder.append(spannableBullet);
            spannableBuilder.append("\n");
            content.setText(spannableBuilder);
            return;
        }

        for (String bullet : contentText) {
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

            spannableBuilder.append(spannableBullet).append("\n");
        }

        content.setText(spannableBuilder);
    }
    /*

    public void setContent(String[] contentText) {
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
        if (contentText.length == 1) {
            content.setText(contentText[0]);
            return;
        }

        for (String bullet : contentText) {
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

            // Apply bold, italics, and underline spans
            if (bullet.contains("**")) {
                int start = bullet.indexOf("**");
                int end = bullet.lastIndexOf("**");
                spannableBullet.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (bullet.contains("*")) {
                int start = bullet.indexOf("*");
                int end = bullet.lastIndexOf("*");
                spannableBullet.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (bullet.contains("__")) {
                int start = bullet.indexOf("__");
                int end = bullet.lastIndexOf("__");
                spannableBullet.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            spannableBuilder.append(spannableBullet).append("\n");
        }

        content.setText(spannableBuilder);
    }*/
}
