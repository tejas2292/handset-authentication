package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VendorEditDetailsRegisterdSerialOwnerName1 extends AppCompatActivity {
    EditText otp;
    Button submit;
    TextView resend, show;
    String number,id, input, lastFourDigits, serial, number2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit_details_registerd_serial_owner_name1);

        resend = findViewById(R.id.tv_resend);
        otp = findViewById(R.id.et_otp);
        submit = findViewById(R.id.btn_submit);
        show = findViewById(R.id.tv_show);
        mAuth = FirebaseAuth.getInstance();
        number = getIntent().getStringExtra("contactWithPlus");
        number2 = getIntent().getStringExtra("contactSecondWithPlus");
        serial = getIntent().getStringExtra("serial");

        input = number;
        if (input.length() > 4)
        {
            lastFourDigits = input.substring(input.length() - 4);
        }
        else
        {
            lastFourDigits = input;
        }
        show.setText("Otp has been sent on \n +91 XXXXX X"+lastFourDigits+" this number.");

        sendVerificationCode();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(otp.getText().toString())){
                    otp.setError("Enter OTP");
                }
                else {
                    try {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp.getText().toString() );
                        signInWithPhoneAuthCredential(credential);
                    }
                    catch (Exception e){
                        Toast.makeText(VendorEditDetailsRegisterdSerialOwnerName1.this, "wrong OTP", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

    }

    private void sendVerificationCode() {
        new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                resend.setText(""+l/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText("Resend");
                resend.setEnabled(true);
            }
        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                VendorEditDetailsRegisterdSerialOwnerName1.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        VendorEditDetailsRegisterdSerialOwnerName1.this.id = id;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VendorEditDetailsRegisterdSerialOwnerName1.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(VendorEditDetailsRegisterdSerialOwnerName1.this, VendorEditDetailsRegisterdSerialOwnerName2.class);
                            intent.putExtra("serial", serial);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            mAuth.signOut();
                            Toast.makeText(VendorEditDetailsRegisterdSerialOwnerName1.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VendorEditDetailsRegisterdSerialOwnerName1.this, SearchSerialNoVendor2.class);
        intent.putExtra("serialNo", serial);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void sendToRecoveryOwnerName(View view) {
        Intent intent = new Intent(VendorEditDetailsRegisterdSerialOwnerName1.this, VendorEditDetailsRegisterdSerialOwnerNameRecovery.class);
        intent.putExtra("contactWithPlus", number);
        intent.putExtra("contactSecondWithPlus", number2);
        intent.putExtra("serial", serial);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
