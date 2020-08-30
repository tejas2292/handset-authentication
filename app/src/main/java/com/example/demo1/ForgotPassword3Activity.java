package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgotPassword3Activity extends AppCompatActivity {
    EditText mPassword, mReEnterPassword;
    Button mBtnChangePassword;
    String password, rePassword,ref,phone,userNumber, type;
    DatabaseReference referenceMain, referenceVendor;
    FirebaseAuth fAuth;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Forgot Password");
        setSupportActionBar(toolbar);

        loadingDialog = new LoadingDialog(ForgotPassword3Activity.this);

        mPassword = findViewById(R.id.et_create_password);
        mReEnterPassword = findViewById(R.id.et_re_enter_password);
        mBtnChangePassword = findViewById(R.id.btn_changePassword);

        fAuth = FirebaseAuth.getInstance();

        ref= getIntent().getStringExtra("ref");
        phone = getIntent().getStringExtra("phone");

        if(ref.equals("1")){
            type = "vendor";
        }
        if(ref.equals("2")){
            type = "dealer";
        }
        if(ref.equals("3")){
            type = "personal";
        }

        referenceMain = FirebaseDatabase.getInstance().getReference("data");
        referenceVendor = FirebaseDatabase.getInstance().getReference("credentials").child(type);


        mBtnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = mPassword.getText().toString().trim();
                rePassword = mReEnterPassword.getText().toString().trim();

                if(password.equals("") || rePassword.equals("") || password.length() < 6){
                    if(password.equals("")){
                        mPassword.setError("Enter New Password");
                    }else if (password.length() < 6) {
                        mPassword.setError("Password must be greater than 6");
                    }
                    else if(rePassword.equals("")){
                        mReEnterPassword.setError("Confirm Password");
                    }
                }
                else {
                    if(rePassword.equals(password)){
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setText("Changing...");

                        referenceMain.child(phone).child(ref).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    userNumber = dataSnapshot.child("userNo").getValue().toString();
                                    String dbNewPass = mReEnterPassword.getText().toString().trim();
                                    HashMap<String, Object> profileMap = new HashMap<>();
                                    profileMap.put("password",dbNewPass);
                                    referenceMain.child(phone).child(ref).updateChildren(profileMap);
                                    referenceVendor.child(userNumber).updateChildren(profileMap);

                                    fAuth.signOut();
                                    Toast.makeText(ForgotPassword3Activity.this, "Password changed succesfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgotPassword3Activity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    loadingDialog.dismissDialog();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ForgotPassword3Activity.this, "Error: "+
                                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            }
                        });


                    }
                    else {
                        mReEnterPassword.setError("Password doesn't match");
                    }
                }
            }
        });
    }
}
