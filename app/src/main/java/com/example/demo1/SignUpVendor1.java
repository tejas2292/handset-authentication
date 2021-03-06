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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class SignUpVendor1 extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    EditText number;
    Button next;
    String phone;
    DatabaseReference referenceMain;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_vendor1);

        loadingDialog = new LoadingDialog(SignUpVendor1.this);

        countryCodePicker = findViewById(R.id.ccp);
        number = findViewById(R.id.et_phone_number);
        next = findViewById(R.id.btn_next);

        referenceMain = FirebaseDatabase.getInstance().getReference("data");

        //Toast.makeText(this, ""+countryCodePicker.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(SignUpVendor1.this, "Enter correct no..", Toast.LENGTH_SHORT).show();
                }
                else {
                    phone = number.getText().toString().replace(" ","");

                    referenceMain.child(phone).child("1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                number.setError("You have already registered this number");
                                loadingDialog.dismissDialog();
                            }
                            else {
                                String phone2 = (countryCodePicker.getSelectedCountryCodeWithPlus() + number.getText().toString().replace(" ",""));
                                //Toast.makeText(SignUpVendor1.this, ""+phone2, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpVendor1.this, SignUpVendor2.class);
                                intent.putExtra("number", phone2);
                                intent.putExtra("phone",phone);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                loadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(SignUpVendor1.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpVendor1.this, SignUpByCategory.class));
        finish();
    }
}
