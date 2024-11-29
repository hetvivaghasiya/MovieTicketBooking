package com.example.cinemax;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration_page  extends AppCompatActivity{
    private EditText editTextEmail, editTextPassword, editTextRetypePassword;
    private CheckBox checkBoxTerms;
    private Button buttonRegister;
    TextView txtloginhere;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_page);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextRetypePassword = findViewById(R.id.repeatpassword);
        checkBoxTerms = findViewById(R.id.cbTerms);
        buttonRegister = findViewById(R.id.signin);
        txtloginhere = findViewById(R.id.txtlogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
                Intent intent=new Intent(registration_page.this,MainActivity2.class);
                startActivity(intent);
            }
        });
        txtloginhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(registration_page.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String retypePassword = editTextRetypePassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(retypePassword)) {
            editTextRetypePassword.setError("Passwords do not match");
            editTextRetypePassword.requestFocus();
            return;
        }

        if (!checkBoxTerms.isChecked()) {
            Toast.makeText(registration_page.this, "Please accept the terms and conditions.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registered successfully, now store additional information in Firebase Realtime Database
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            // Store user information in Firebase Realtime Database under "users" node
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                            usersRef.child("email").setValue(email);

                            // Optionally, you can store more information about the user here if needed

                            Toast.makeText(registration_page.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(registration_page.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(registration_page.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}



