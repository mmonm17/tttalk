package com.t_t_talk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsDialog {
    private Dialog settingsDialog;
    private ConstraintLayout cl_setMusic;
    private Button btn_setMusic;
    private ImageView muteImage;
    private Button logOutButton;
    private Button closeButton;
    private FirebaseAuth mAuth;

    public SettingsDialog(Context context) {
        this.settingsDialog = new Dialog(context);
        this.settingsDialog.setContentView(R.layout.dialog_settings);
        this.settingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.cl_setMusic = settingsDialog.findViewById(R.id.cl_set_music);
        this.btn_setMusic = settingsDialog.findViewById(R.id.btn_set_music);
        this.muteImage = settingsDialog.findViewById(R.id.img_set_music);
        this.logOutButton = settingsDialog.findViewById(R.id.btn_logOut);
        this.closeButton = settingsDialog.findViewById(R.id.btn_close_settings);

        this.cl_setMusic.setOnClickListener(v -> {
            if (this.btn_setMusic.getText().toString().equals("Music: ON")) {
                this.btn_setMusic.setText("Music: OFF");
                this.muteImage.setImageResource(R.drawable.ic_mute);
                this.muteImage.setRotationY(180);
            } else {
                this.btn_setMusic.setText("Music: ON");
                this.muteImage.setImageResource(R.drawable.ic_unmute);
                this.muteImage.setRotationY(180);
            }
        });

        this.logOutButton.setOnClickListener(v -> {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
        this.closeButton.setOnClickListener(v -> {
            this.settingsDialog.dismiss();
        });
    }
    public void show() {
        this.settingsDialog.show();
    }
}
