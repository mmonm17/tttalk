package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView txt_register;
    TextInputEditText input_email;
    Button btn_send_link;
    private FirebaseAuth mAuth;
    TextInputLayout layout_input_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get the views from the layout
        txt_register = findViewById(R.id.txt_register);
        input_email = findViewById(R.id.input_email);
        btn_send_link = findViewById(R.id.btn_send_link);
        layout_input_email = findViewById(R.id.layout_input_email);

        // Set click listener for the "Register" text
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set click listener for the "Send Link" button
        btn_send_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                if (email.isEmpty()) {
                    layout_input_email.setError("Email is required");
                    return;
                }

                // Send password reset link to the user's email
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showToast("Password reset link sent to your email.");
                        } else {
                            String errorMessage = task.getException() != null
                                    ? "\"Forgot Password failed: \"" + task.getException().getMessage()
                                    : "\"Forgot Password failed: \" Unknown error";
                            showToast(errorMessage);
                        }
                    });
            }
        });
    }

    // Helper method to show a toast message
    protected void showToast(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }
}