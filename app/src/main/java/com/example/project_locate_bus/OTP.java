package com.example.project_locate_bus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {
    EditText one, two, three, four, five, six;
    TextView show, resend;

    String getOtpBackend,phoneNumber;

    Button verify;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
//        getSupportActionBar().hide();
        one = findViewById(R.id.input1);
        two = findViewById(R.id.input2);
        three = findViewById(R.id.input3);
        four = findViewById(R.id.input4);
        five = findViewById(R.id.input5);
        six = findViewById(R.id.input6);

        show=findViewById(R.id.show);

        resend = findViewById(R.id.resend);

        show.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));
        Bundle bundle = getIntent().getExtras();
        getOtpBackend = bundle.getString("backendotp");
        phoneNumber=bundle.getString("mobile");

        final ProgressBar progressBarVerifyOtp = findViewById(R.id.progressbar_verify_otp);


        verify = (Button) findViewById(R.id.verifysubmit);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!one.getText().toString().trim().isEmpty() && !two.getText().toString().trim().isEmpty()) {
                    String entercodeotp = one.getText().toString() + two.getText().toString() + three.getText().toString() + four.getText().toString() + five.getText().toString() + six.getText().toString();

                    if (getOtpBackend != null) {
                        progressBarVerifyOtp.setVisibility(View.VISIBLE);
                        verify.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getOtpBackend, entercodeotp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBarVerifyOtp.setVisibility(View.GONE);
                                verify.setVisibility(View.VISIBLE);

                                if (task.isSuccessful()) {
                                    Intent i = new Intent(getApplicationContext(), Change_Password.class);
                                    i.putExtra("mobile",phoneNumber);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Enter correct otp", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }

                    //Toast.makeText(getApplicationContext(), "otp verify", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter all number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        numberotpmove();
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + getIntent().getStringExtra("mobile"), 60, TimeUnit.SECONDS, OTP.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        getOtpBackend = newbackendotp;
                        Toast.makeText(getApplicationContext(), "OTP send successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void numberotpmove() {
        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    three.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    four.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    five.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    six.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}