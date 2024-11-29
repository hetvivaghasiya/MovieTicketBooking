package com.example.cinemax;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class foreget_password extends AppCompatActivity{
    private EditText phoneEditText;
    private Button sendOtpButton;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_foreget_password);

        phoneEditText = findViewById(R.id.phonenumber);
        sendOtpButton = findViewById(R.id.forget);
        mAuth = FirebaseAuth.getInstance();

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneEditText.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(foreget_password.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                } else {
                    String fullPhoneNumber = "+91" + phoneNumber;
                    sendVerificationCode(fullPhoneNumber);
                }
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                // Auto-retrieval or Instant verification
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(foreget_password.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationId = s;
                                Intent intent = new Intent(foreget_password.this, verification.class);
                                intent.putExtra("verificationId", verificationId);
                                intent.putExtra("mobileNumber", phoneNumber); // Pass the mobile number here
                                startActivity(intent);
                            }
                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}

