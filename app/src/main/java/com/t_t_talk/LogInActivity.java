package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.intellij.lang.annotations.Language;

public class LogInActivity extends AppCompatActivity {
    Button btn_log_in;
    TextView txt_sign_up;
    TextView txt_forgot_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_log_in = findViewById(R.id.btn_log_in);

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, LanguagePickerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_log_in = findViewById(R.id.btn_log_in);

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, LanguageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txt_sign_up = findViewById(R.id.txt_sign_up);
        txt_forgot_pass = findViewById(R.id.txt_forgot_pass);

        txt_sign_up.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        txt_forgot_pass.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });

        Intent intent = getIntent();
        if(intent.getBooleanExtra("isRegistered", false))
            txt_forgot_pass.setVisibility(View.GONE);
    }
}