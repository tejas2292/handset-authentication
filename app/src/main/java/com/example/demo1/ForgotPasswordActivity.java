package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgotPasswordActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText number, otp;
    Button next;
    String ref,phone;
    DatabaseReference referenceMain, referenceVendor;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        loadingDialog = new LoadingDialog(ForgotPasswordActivity.this);

        countryCodePicker = findViewById(R.id.ccp);
        number = findViewById(R.id.et_phone_number);
        next = findViewById(R.id.btn_next);

        //countryCodePicker.registerCarrierNumberEditText(number);
        ref = getIntent().getStringExtra("ref");

        referenceMain = FirebaseDatabase.getInstance().getReference("data");
        referenceVendor = FirebaseDatabase.getInstance().getReference("credentials").child("vendor");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();

                if(TextUtils.isEmpty(number.getText().toString())){
                    loadingDialog.dismissDialog();
                    number.setError("Enter the phone number");
                }
                else if(number.getText().toString().replace(" ","").length()!=10) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(ForgotPasswordActivity.this, "Enter correct no..", Toast.LENGTH_SHORT).show();
                }
                else {
                    phone = number.getText().toString().replace(" ","");
                    referenceMain.child(phone).child(ref).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String phone2 = (countryCodePicker.getSelectedCountryCodeWithPlus() + number.getText().toString().replace(" ",""));

                                Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPassword2Activity.class);
                                intent.putExtra("number", phone2);
                                intent.putExtra("ref", ref);
                                intent.putExtra("phone",phone);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                loadingDialog.dismissDialog();
                            }
                            else {
                                loadingDialog.dismissDialog();
                                Toast.makeText(ForgotPasswordActivity.this, "You don't have account..", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }
}
