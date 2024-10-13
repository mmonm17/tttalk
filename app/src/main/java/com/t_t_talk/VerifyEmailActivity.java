package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerifyEmailActivity extends AppCompatActivity {
    Button btn_verify;
    TextView txt_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_verify = findViewById(R.id.btn_verify);

        btn_verify.setOnClickListener(view -> {
            Intent intent = new Intent(VerifyEmailActivity.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("isRegistered", true);

            // Start MainActivity in the back stack
            Intent mainIntent = new Intent(VerifyEmailActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            startActivity(intent);
            finish();
        });

        txt_sign_up = findViewById(R.id.txt_sign_up);
        txt_sign_up.setOnClickListener(view -> {
            Intent intent = new Intent(VerifyEmailActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }


}