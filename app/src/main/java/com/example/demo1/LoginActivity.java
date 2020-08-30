package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView mForgotPassword;
    EditText mUserName, mPassword;
    Button mBtnSignIn, mBtnSignUp;
    String UserName, UserName2, Password, id1, id2, id3, pass, checkID;
    DatabaseReference reference, reference2;
    FirebaseAuth fAuth;
    String ref = "1";
    Button r1, r2, r3;
    LoadingDialog loadingDialog;
    private static final int STORAGE_PERMISSION_CODE = 101;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingDialog = new LoadingDialog(LoginActivity.this);

        mForgotPassword = findViewById(R.id.tv_forgot);
        mUserName = findViewById(R.id.et_username);
        mPassword = findViewById(R.id.et_password);
        mBtnSignIn = findViewById(R.id.buttonSignIn);
        mBtnSignUp = findViewById(R.id.buttonSignUp);

        loadingDialog = new LoadingDialog(LoginActivity.this);

        r1 = findViewById(R.id.rd_vendor);
        r2 = findViewById(R.id.rd_dealer);
        r3 = findViewById(R.id.rd_personal);


        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);


        reference = FirebaseDatabase.getInstance().getReference().child("data");
        reference2 = FirebaseDatabase.getInstance().getReference().child("username");

        firebaseDatabase = FirebaseDatabase.getInstance();

        fAuth = FirebaseAuth.getInstance();
        checkSession();

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = "1";
                r1.setBackground(getResources().getDrawable(R.drawable.button2));
                r2.setBackground(getResources().getDrawable(R.drawable.button3));
                r3.setBackground(getResources().getDrawable(R.drawable.button3));
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = "2";
                r2.setBackground(getResources().getDrawable(R.drawable.button2));
                r1.setBackground(getResources().getDrawable(R.drawable.button3));
                r3.setBackground(getResources().getDrawable(R.drawable.button3));
            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = "3";
                r3.setBackground(getResources().getDrawable(R.drawable.button2));
                r2.setBackground(getResources().getDrawable(R.drawable.button3));
                r1.setBackground(getResources().getDrawable(R.drawable.button3));
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent.putExtra("ref", ref);
                startActivity(intent);
                finish();
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpByCategory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Signing In...");
                UserName = mUserName.getText().toString();
                Password = mPassword.getText().toString();

                if (UserName.equals("") || Password.equals("")) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(LoginActivity.this, "fill details", Toast.LENGTH_SHORT).show();
                } else {
                    if (UserName.equals("admin") && Password.equals("admin123")) {
                        UserCurrent user = new UserCurrent(LoginActivity.this);
                        user.setUsername(UserName);
                        user.setPass(Password);
                        String id = "4";
                        loginByDesignation(id);
                    } else {
                        validateUserName();
                    }
                }
            }
        });

    }

    private void validateUserName() {
        if (ref == "1") {
            reference2.child(UserName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserName2 = dataSnapshot.child("contact").getValue().toString();
                        checkID = dataSnapshot.child("loginID").getValue().toString();
                        if (checkID.equals(ref)) {
                            UserName = UserName2;
                            UserCurrent user = new UserCurrent(LoginActivity.this);
                            user.setUsername(UserName);
                            user.setPass(Password);
                            validateLogin();
                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(LoginActivity.this, "Wrong User", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        UserCurrent user = new UserCurrent(LoginActivity.this);
                        user.setUsername(UserName);
                        user.setPass(Password);
                        validateLogin();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (ref == "2") {
            reference2.child(UserName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserName2 = dataSnapshot.child("contact").getValue().toString();
                        checkID = dataSnapshot.child("loginID").getValue().toString();
                        if (checkID.equals(ref)) {
                            UserName = UserName2;
                            UserCurrent user = new UserCurrent(LoginActivity.this);
                            user.setUsername(UserName);
                            user.setPass(Password);
                            validateLogin();
                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(LoginActivity.this, "Wrong User", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        UserCurrent user = new UserCurrent(LoginActivity.this);
                        user.setUsername(UserName);
                        user.setPass(Password);
                        validateLogin();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (ref == "3") {
            reference2.child(UserName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserName2 = dataSnapshot.child("contact").getValue().toString();
                        checkID = dataSnapshot.child("loginID").getValue().toString();
                        if (checkID.equals(ref)) {
                            UserName = UserName2;
                            UserCurrent user = new UserCurrent(LoginActivity.this);
                            user.setUsername(UserName);
                            user.setPass(Password);
                            validateLogin();
                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(LoginActivity.this, "Wrong User", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        UserCurrent user = new UserCurrent(LoginActivity.this);
                        user.setUsername(UserName);
                        user.setPass(Password);
                        validateLogin();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            loadingDialog.dismissDialog();
            Toast.makeText(this, "You don't have account", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{permission},
                    requestCode);
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(LoginActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void checkSession() {
        loadingDialog.startLoadingDialog();
        if (new UserCurrent(LoginActivity.this).getPass() != "") {
            String p = new UserCurrent(LoginActivity.this).getLoginid();
            loginByDesignation(p);
        } else {
            loadingDialog.dismissDialog();
        }
    }


    private void validateLogin() {

        if (ref == "1") {
            reference.child(UserName).child(ref).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        id1 = dataSnapshot.child("loginID").getValue().toString();
                        pass = dataSnapshot.child("password").getValue().toString();
                        if (pass.equals(Password)) {
                            loginByDesignation(id1);
                        } else {
                            Toast.makeText(LoginActivity.this, "Account not present in vendor", Toast.LENGTH_SHORT).show();
                            new UserCurrent(LoginActivity.this).removeUser();
                            loadingDialog.dismissDialog();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Account not present in vendor", Toast.LENGTH_SHORT).show();
                        new UserCurrent(LoginActivity.this).removeUser();
                        loadingDialog.dismissDialog();
                        Toast.makeText(LoginActivity.this, "Wrong Information", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    new UserCurrent(LoginActivity.this).removeUser();
                    loadingDialog.dismissDialog();
                }
            });
        } else if (ref == "2") {
            reference.child(UserName).child(ref).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        id2 = dataSnapshot.child("loginID").getValue().toString();
                        pass = dataSnapshot.child("password").getValue().toString();
                        if (pass.equals(Password)) {
                            loginByDesignation(id2);
                        } else {
                            Toast.makeText(LoginActivity.this, "Account not present in dealer", Toast.LENGTH_SHORT).show();
                            new UserCurrent(LoginActivity.this).removeUser();
                            loadingDialog.dismissDialog();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Account not present in dealer", Toast.LENGTH_SHORT).show();
                        new UserCurrent(LoginActivity.this).removeUser();
                        loadingDialog.dismissDialog();
                        Toast.makeText(LoginActivity.this, "Wrong Information", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    new UserCurrent(LoginActivity.this).removeUser();
                    loadingDialog.dismissDialog();
                }
            });
        } else if (ref == "3") {
            reference.child(UserName).child(ref).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        id3 = dataSnapshot.child("loginID").getValue().toString();
                        pass = dataSnapshot.child("password").getValue().toString();
                        if (pass.equals(Password)) {
                            loginByDesignation(id3);
                        } else {
                            Toast.makeText(LoginActivity.this, "Account not present in personal", Toast.LENGTH_SHORT).show();
                            new UserCurrent(LoginActivity.this).removeUser();
                            loadingDialog.dismissDialog();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Account not present in personal", Toast.LENGTH_SHORT).show();
                        new UserCurrent(LoginActivity.this).removeUser();
                        loadingDialog.dismissDialog();
                        Toast.makeText(LoginActivity.this, "Wrong Information", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    new UserCurrent(LoginActivity.this).removeUser();
                    loadingDialog.dismissDialog();
                }
            });
        } else {
            Toast.makeText(this, "Your account is not present", Toast.LENGTH_SHORT).show();
        }
    }


    private void loginByDesignation(String id) {
        Intent intent;
        if (id.equals("1")) {
            new UserCurrent(LoginActivity.this).setLoginid(id);
            loadingDialog.dismissDialog();
            intent = new Intent(LoginActivity.this, MainActivityVendor.class);
        } else if (id.equals("2")) {
            new UserCurrent(LoginActivity.this).setLoginid(id);
            loadingDialog.dismissDialog();
            intent = new Intent(LoginActivity.this, MainActivityDealer.class);
        } else if (id.equals("3")) {
            new UserCurrent(LoginActivity.this).setLoginid(id);
            loadingDialog.dismissDialog();
            intent = new Intent(LoginActivity.this, MainActivityPersonal.class);
        } else if (id.equals("4")) {
            new UserCurrent(LoginActivity.this).setLoginid(id);
            loadingDialog.dismissDialog();
            intent = new Intent(LoginActivity.this, MainActivityAdmin.class);
        } else {
            loadingDialog.dismissDialog();
            new UserCurrent(LoginActivity.this).removeUser();
            Toast.makeText(this, "wrong", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //here exit app start..........................................
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
        //here exit app alert close............................................
    }
}
