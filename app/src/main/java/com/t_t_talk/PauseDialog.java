package com.t_t_talk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

public class PauseDialog {
    private Dialog pauseDialog;
    private TextView txt_pause;
    private TextView txt_header_pause;

    public PauseDialog(Context context, String txt_pause_string, String txt_header_string) {
        this.pauseDialog = new Dialog(context);
        this.pauseDialog.setContentView(R.layout.dialog_pause);
        this.pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.txt_pause = pauseDialog.findViewById(R.id.txt_pause);
        this.txt_header_pause = pauseDialog.findViewById(R.id.txt_header_pause);
        this.txt_pause.setText(txt_pause_string);
        this.txt_header_pause.setText(txt_header_string);
    }
    public void show() {
        this.pauseDialog.show();
    }
    public void dismiss() { this.pauseDialog.dismiss(); }
}

