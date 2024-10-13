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

public class RegisterActivity extends AppCompatActivity {
    TextView txt_log_in;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_log_in = findViewById(R.id.txt_log_in);
        btn_register = findViewById(R.id.btn_register);

        txt_log_in.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
            intent.putExtra("isRegistered", false);
            startActivity(intent);
            finish();
        });

        btn_register.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, VerifyEmailActivity.class);
            startActivity(intent);
            finish();
        });
    }
}