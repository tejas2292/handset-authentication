package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SignUpPersonal extends AppCompatActivity {
    EditText mOwnerFirstName, mOwnerLastName, mEmail, mRecoveryEmail, mPassword, mConfirmPassword,
            mCreateUserName;
    TextView mContactNo;
    Button mBtnSubmit, mBtnSignIN;
    String loginId = "3";
    int maxIDPersonal = 0;
    FirebaseAuth fAuth;
    String OwnerFirstName, OwnerLastName, Email, RecoveryEmail, Password, ContactNo, ConfirmPassword,
            UserNo, points = null, UserName;
    DatabaseReference referenceMain, referencePersonal, reference2;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_personal);

        loadingDialog = new LoadingDialog(SignUpPersonal.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Register Personal Account");
        setSupportActionBar(toolbar);

        mCreateUserName = findViewById(R.id.et_create_username);
        mOwnerFirstName = findViewById(R.id.et_owner_first_name);
        mOwnerLastName = findViewById(R.id.et_owner_last_name);
        mEmail = findViewById(R.id.et_email);
        mRecoveryEmail = findViewById(R.id.et_recovery_email);
        mPassword = findViewById(R.id.et_create_password);
        mConfirmPassword = findViewById(R.id.et_re_enter_password);
        mContactNo = findViewById(R.id.et_contact_no);
        fAuth = FirebaseAuth.getInstance();


        fAuth.signOut();
        ContactNo = getIntent().getStringExtra("phone");
        mContactNo.setText(ContactNo);

        mBtnSubmit = findViewById(R.id.buttonSubmit);
        mBtnSignIN = findViewById(R.id.alreadyAccount);

        referenceMain = FirebaseDatabase.getInstance().getReference("data");
        reference2 = FirebaseDatabase.getInstance().getReference("username");
        referencePersonal = FirebaseDatabase.getInstance().getReference("credentials").child("personal");

        referencePersonal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxIDPersonal = (int) dataSnapshot.getChildrenCount();
                //Toast.makeText(SignUp.this, "maxID" + maxIDVendor, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBtnSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPersonal.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Creating Account");

                OwnerFirstName = mOwnerFirstName.getText().toString();
                OwnerLastName = mOwnerLastName.getText().toString();
                Email = mEmail.getText().toString();
                RecoveryEmail = mRecoveryEmail.getText().toString();
                Password = mPassword.getText().toString();
                ContactNo = mContactNo.getText().toString();
                UserName = mCreateUserName.getText().toString();
                ConfirmPassword = mConfirmPassword.getText().toString();

                reference2.child(UserName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            loadingDialog.dismissDialog();
                            mCreateUserName.setError("This username is already taken, try diffrent.");
                        } else {
                            if (OwnerFirstName.equals("") || OwnerLastName.equals("") || Email.equals("") || RecoveryEmail.equals("") || UserName.equals("") ||
                                    Password.equals("") || ConfirmPassword.equals("") || Password.length() < 6) {
                                loadingDialog.dismissDialog();
                                if (OwnerFirstName.equals("")) {
                                    mOwnerFirstName.setError("Please provide owner first name");
                                } else if (OwnerLastName.equals("")) {
                                    mOwnerLastName.setError("Please provide owner last name");
                                } else if (Email.equals("")) {
                                    mEmail.setError("Please E-mail address");
                                } else if (RecoveryEmail.equals("")) {
                                    mRecoveryEmail.setError("Please provide Recovery E-mail");
                                } else if (UserName.equals("")) {
                                    mRecoveryEmail.setError("Please provide username");
                                } else if (Password.equals("")) {
                                    mPassword.setError("Please provide password");
                                } else if (Password.length() < 6) {
                                    mPassword.setError("Password must be greater than 6");
                                } else if (ConfirmPassword.equals("")) {
                                    mConfirmPassword.setError("Please confirm the password");
                                }
                            } else {
                                if (!ConfirmPassword.equals(Password)) {
                                    loadingDialog.dismissDialog();
                                    mConfirmPassword.setError("Password Dosen't Match");

                                } else {
                                    furtherUpload();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loadingDialog.dismissDialog();
                    }
                });

            }
        });
    }

    private void furtherUpload() {
        UserNo = String.valueOf(maxIDPersonal + 1);

        final PersonalUpload v1 = new PersonalUpload(OwnerFirstName, OwnerLastName, Email, RecoveryEmail, ConfirmPassword,
                ContactNo, UserNo, loginId, UserName, points, "active");

        final Upload u1 = new Upload(ConfirmPassword, ContactNo, loginId, UserNo, UserName);

        referencePersonal.child(String.valueOf(maxIDPersonal + 1)).setValue(v1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                referenceMain.child(ContactNo).child(loginId).setValue(u1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        reference2.child(UserName).setValue(u1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                fAuth.signOut();
                                Toast.makeText(SignUpPersonal.this, "Successfully registered..", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpPersonal.this, LoginActivity.class));
                                finish();
                                loadingDialog.dismissDialog();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fAuth.signOut();
                loadingDialog.dismissDialog();
                Toast.makeText(SignUpPersonal.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SignUpPersonal.this);
        builder.setMessage("Are you sure want to exit from app?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }
}
