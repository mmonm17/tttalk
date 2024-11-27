package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.t_t_talk.DB.AppDatabase;

public class LogInActivity extends AppCompatActivity {
    Button btn_log_in;
    TextView txt_register;
    TextView txt_forgot_pass;
    TextInputEditText input_email;
    TextInputEditText input_pass;
    LinearLayout layout_register;
    private FirebaseAuth mAuth;
    TextInputLayout layout_input_email;
    TextInputLayout layout_input_pass;
    AppDatabase db;

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
        db = new AppDatabase(LogInActivity.this);
        mAuth = FirebaseAuth.getInstance();
        input_email = findViewById(R.id.input_email);
        input_pass = findViewById(R.id.input_pass);
        layout_input_email = findViewById(R.id.layout_input_email);
        layout_input_pass = findViewById(R.id.layout_input_pass);

        btn_log_in = findViewById(R.id.btn_log_in);

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                String password = input_pass.getText().toString();

                if (email.isEmpty()) {
                    layout_input_email.setError("Email is required");
                    return;
                }

                if (password.isEmpty()) {
                    layout_input_pass.setError("Password is required");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user == null) {
                            layout_input_email.setError(" ");
                            layout_input_pass.setError("That combination does not exist");
                            return;
                        }
                        if (task.isSuccessful() && user != null && user.isEmailVerified()) {
                            //Clear localDB as that data is acting as a cache associated with an individual user
                            db.clearLocalDB();
                            db.createRemoteUser();
                            db.createRemoteUserVersion();
                            Intent intent = new Intent(LogInActivity.this, LanguageSelectActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        if (user.isEmailVerified()) {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            handleLoginError(errorMessage);
                        } else {
                            layout_input_email.setError("Please verify your email");
                            user.sendEmailVerification()
                                .addOnCompleteListener(verificationTask -> {
                                    if (verificationTask.isSuccessful()) {
                                        Intent intent = new Intent(LogInActivity.this, VerifyEmailActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        String errorMessage = verificationTask.getException() != null
                                                ? verificationTask.getException().getMessage()
                                                : "Unknown error";
                                        handleLoginError(errorMessage);
                                    }
                                });
                        }
                        mAuth.signOut();
                    });
            }
        });

        txt_register = findViewById(R.id.txt_register);
        txt_forgot_pass = findViewById(R.id.txt_forgot_pass);

        txt_register.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        txt_forgot_pass.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });

        layout_register = findViewById(R.id.layout_register);

        Intent intent = getIntent();
        if(intent.getBooleanExtra("isRegistered", false))
            layout_register.setVisibility(View.GONE);
    }

    protected void handleLoginError(String errorMessage) {
        Toast toast = Toast.makeText(this, "LogIn failed: " + errorMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent intent = new Intent(LogInActivity.this, LanguageSelectActivity.class);
            startActivity(intent);
            finish();
        }
        mAuth.signOut();
    }
}