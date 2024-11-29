package com.example.cinemax;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class verification extends AppCompatActivity{
    private EditText otpEditText1, otpEditText2, otpEditText3, otpEditText4, otpEditText5, otpEditText6;
    private Button verifyOtpButton;
    private TextView mobileNumberTextView, timerTextView,resendOtpTextView;
    private String verificationId,mobileNumber;
    private FirebaseAuth mAuth;
    private static final long OTP_TIMEOUT = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification);

        otpEditText1 = findViewById(R.id.otpDigit1);
        otpEditText2 = findViewById(R.id.otpDigit2);
        otpEditText3 = findViewById(R.id.otpDigit3);
        otpEditText4 = findViewById(R.id.otpDigit4);
        otpEditText5 = findViewById(R.id.otpDigit5);
        otpEditText6 = findViewById(R.id.otpDigit6);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);
        timerTextView = findViewById(R.id.timerText);
        mobileNumberTextView = findViewById(R.id.otpInstructionText);
        resendOtpTextView = findViewById(R.id.resendOtpText);

        //verifyOtpButton = findViewById(R.id.verifyOtpButton);
        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");
        mobileNumber = getIntent().getStringExtra("mobileNumber");

        mobileNumberTextView.setText(mobileNumber);

        startTimer();

        addTextWatchers();

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpEditText1.getText().toString() +
                        otpEditText2.getText().toString() +
                        otpEditText3.getText().toString() +
                        otpEditText4.getText().toString() +
                        otpEditText5.getText().toString() +
                        otpEditText6.getText().toString();


                if (TextUtils.isEmpty(otp) || otp.length() != 6) {
                    Toast.makeText(verification.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    verifyCode(otp);
                }
            }
        });
        resendOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtpTextView.setVisibility(View.GONE);
                sendVerificationCode(mobileNumber);
                startTimer();
            }
        });

    }

    private void startTimer() {
        new CountDownTimer(OTP_TIMEOUT, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time remaining: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerTextView.setText("Time expired. Please request a new OTP.");
                verifyOtpButton.setEnabled(false);
                resendOtpTextView.setVisibility(View.VISIBLE);
            }
        }.start();
    }
    private void addTextWatchers() {
        otpEditText1.addTextChangedListener((TextWatcher) new GenericTextWatcher(otpEditText1, otpEditText2));
        otpEditText2.addTextChangedListener(new GenericTextWatcher(otpEditText2, otpEditText3));
        otpEditText3.addTextChangedListener(new GenericTextWatcher(otpEditText3, otpEditText4));
        otpEditText4.addTextChangedListener(new GenericTextWatcher(otpEditText4, otpEditText5));
        otpEditText5.addTextChangedListener(new GenericTextWatcher(otpEditText5, otpEditText6));
        otpEditText6.addTextChangedListener(new GenericTextWatcher(otpEditText6, null));
    }

    private class GenericTextWatcher implements TextWatcher {
        private EditText currentView;
        private EditText nextView;

        public GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(verification.this, reset_password.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(verification.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(OTP_TIMEOUT, TimeUnit.MILLISECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                // Auto-retrieval or Instant verification
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(verification.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationId = s;
                                Toast.makeText(verification.this, "OTP has been resent", Toast.LENGTH_SHORT).show();
                                verifyOtpButton.setEnabled(true);
                            }
                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}