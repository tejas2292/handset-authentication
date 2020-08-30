package com.example.demo1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DeleteMyAccount extends AppCompatActivity {
    String contact, userName, dbPass, pass,confirmPass, vendorID;
    DatabaseReference referenceData, referenceUserName, referenceVendor;
    EditText password, confirm;
    Button mBtnDeleteAccount;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_my_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Delete Account");
        setSupportActionBar(toolbar);

        loadingDialog = new LoadingDialog(DeleteMyAccount.this);

        password = findViewById(R.id.et_password1);
        confirm = findViewById(R.id.et_password1Confirm);
        mBtnDeleteAccount = findViewById(R.id.btn_deleteMyAccount);

        contact = getIntent().getStringExtra("contact");
        userName = getIntent().getStringExtra("userName");
        dbPass = getIntent().getStringExtra("pass");
        vendorID = getIntent().getStringExtra("vendorID");


        if(contact != null){
            referenceVendor = FirebaseDatabase.getInstance().getReference("credentials").child("vendor").child(vendorID);
            referenceData = FirebaseDatabase.getInstance().getReference("data").child(contact);
            referenceUserName = FirebaseDatabase.getInstance().getReference("username").child(userName);
        }
        else {
            Toast.makeText(this, "wait data is still loading..", Toast.LENGTH_SHORT).show();
            new UserCurrent(DeleteMyAccount.this).removeUser();
            startActivity(new Intent(DeleteMyAccount.this, MainActivityVendor.class));
            finish();
        }

        mBtnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = password.getText().toString().trim();
                confirmPass = confirm.getText().toString().trim();

                if(pass.equals("") || confirmPass.equals("")){
                    if(pass.equals("")) {
                        password.setError("Please Enter Your Password.");
                    }
                    if(confirmPass.equals("")){
                        confirm.setError("Please Confirm Your Password.");
                    }
                }
                else {
                    if(confirmPass.equals(pass)){
                        if(confirmPass.equals(dbPass)){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DeleteMyAccount.this);
                            builder.setMessage("Are you sure want to delete your account?");
                            builder.setCancelable(false);
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loadingDialog.startLoadingDialog();
                                    loadingDialog.setText("Deleting...");
                                    new UserCurrent(DeleteMyAccount.this).removeUser();

                                    HashMap<String, Object> profileMap = new HashMap<>();
                                    profileMap.put("activity", "deactivate");

                                    referenceVendor.updateChildren(profileMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    referenceData.child("1").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    referenceUserName.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(DeleteMyAccount.this, "Your account is deleted successfully...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    startActivity(new Intent(DeleteMyAccount.this, LoginActivity.class));
                                    finish();
                                    loadingDialog.dismissDialog();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                        else {
                            Toast.makeText(DeleteMyAccount.this, "You have entered wrong password..",
                                    Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }
                    else {
                        confirm.setError("Password does not match to above password.");
                        loadingDialog.dismissDialog();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(DeleteMyAccount.this, MainActivityVendor.class));
        finish();
    }
}
