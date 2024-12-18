package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    TextView txt_log_in;
    Button btn_register;
    TextInputEditText input_email;
    TextInputEditText input_pass;
    TextInputEditText input_confirm_pass;
    private FirebaseAuth mAuth;

    TextInputLayout layout_input_email;
    TextInputLayout layout_input_pass;
    TextInputLayout layout_input_confirm_pass;

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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get views from layout
        txt_log_in = findViewById(R.id.txt_log_in);
        btn_register = findViewById(R.id.btn_register);
        input_email = findViewById(R.id.input_email);
        layout_input_email = findViewById(R.id.layout_input_email);
        input_pass = findViewById(R.id.input_pass);
        input_confirm_pass = findViewById(R.id.input_confirm_pass);
        layout_input_pass = findViewById(R.id.layout_input_pass);
        layout_input_confirm_pass = findViewById(R.id.layout_input_confirm_pass);

        // Set listeners
        txt_log_in.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
            intent.putExtra("isRegistered", false);
            startActivity(intent);
            finish();
        });

        // Registering
        btn_register.setOnClickListener(view -> {
            String email = input_email.getText().toString();
            String password = input_pass.getText().toString();
            String confirm_pass = input_confirm_pass.getText().toString();
            boolean inputError = false;

            if (email.isEmpty()) {
                layout_input_email.setError("Email is required");
                inputError = true;
            }

            if (password.isEmpty()) {
                layout_input_pass.setError("Password is required");
                inputError = true;
            }

            if (confirm_pass.isEmpty()) {
                if(password.isEmpty()) {
                    layout_input_confirm_pass.setError("Set your password first");
                }
                else {
                    layout_input_confirm_pass.setError("Confirm your password");
                    inputError = true;
                }
            }

            if (inputError) {
                return;
            }

            if (!password.equals(confirm_pass)) {
                input_confirm_pass.setError("Passwords do not match");
                return;
            }

            // Create user with email and password with auth
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        user.sendEmailVerification()
                            .addOnCompleteListener(verificationTask -> {
                                if (verificationTask.isSuccessful()) {
                                    Intent intent = new Intent(RegisterActivity.this, VerifyEmailActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String errorMessage = verificationTask.getException() != null
                                            ? verificationTask.getException().getMessage()
                                            : "Unknown error";
                                    handleRegistrationError(errorMessage);
                                }
                            });
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        assert errorMessage != null;
                        if(errorMessage.contains("email address is already in use")) {
                            layout_input_email.setError("Email address is already in use");
                        }
                        else if (errorMessage.contains("Password should be at least 6 characters")){
                            layout_input_pass.setError("Password should be at least 6 characters");
                        }
                        else {
                            handleRegistrationError(errorMessage);
                        }
                    }
                });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent intent = new Intent(RegisterActivity.this, LanguageSelectActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Handle Registration Error Messages
     * @param errorMessage
     */
    protected void handleRegistrationError(String errorMessage) {
        Toast toast = Toast.makeText(this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }
}
