package com.mentarii.groovix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText,nameEditText;
    private AppCompatButton signUpButton;
    private FirebaseAuth mAuth;
    TextView txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txt_login = findViewById(R.id.txt_login);
        txt_login.setOnClickListener(view -> {
            Intent Go = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(Go);
        });

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);


        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register(); // Panggil metode Register() ketika tombol sign-up diklik
            }
        });
    }

    private void Register() {
        String user = emailEditText.getText().toString().trim();
        String pass = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim(); // Tambahkan EditText untuk nama pengguna di layout Anda

        if (user.isEmpty()) {
            emailEditText.setError("Email can not be empty");
        } else if (pass.isEmpty()) {
            passwordEditText.setError("Password can not be empty");
        } else if (name.isEmpty()) {
            nameEditText.setError("Name can not be empty");
        } else {
            mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Set nama pengguna ke profil Firebase
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> profileTask) {
                                if (profileTask.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
