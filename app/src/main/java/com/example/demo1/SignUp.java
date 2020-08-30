package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
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

public class SignUp extends AppCompatActivity {
    EditText mShopName, mOwnerFirstName, mOwnerLastName, mOwnerAddress, mGstNo, mEmail, mRecoveryEmail,
            mPassword, mConfirmPassword, mPincode, mCreateUserName;
    TextView mContactNo;
    Button mBtnSubmit, mBtnSignIN;
    String loginId = "1";
    int maxIDVendor = 0;
    FirebaseAuth fAuth;
    String ShopName, OwnerFirstName, OwnerLastName, OwnerAddress, GstNo, Email, RecoveryEmail, Password,
            ContactNo, ConfirmPassword, UserNo, Points = "10000", PinCode, UserName;
    DatabaseReference referenceMain, referenceVendor, reference2;
    LoadingDialog loadingDialog;
    public static final String GSTINFORMAT_REGEX = "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}";
    public static final String GSTN_CODEPOINT_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loadingDialog = new LoadingDialog(SignUp.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Register Vendor Account");
        setSupportActionBar(toolbar);

        mCreateUserName = findViewById(R.id.et_create_username);
        mShopName = findViewById(R.id.et_retailer_shop_name);
        mOwnerFirstName = findViewById(R.id.et_owner_first_name);
        mOwnerLastName = findViewById(R.id.et_owner_last_name);
        mOwnerAddress = findViewById(R.id.et_owner_address);
        mGstNo = findViewById(R.id.et_gst_no);
        mEmail = findViewById(R.id.et_email);
        mPincode = findViewById(R.id.et_pincode);
        mRecoveryEmail = findViewById(R.id.et_recovery_email);
        mPassword = findViewById(R.id.et_create_password);
        mConfirmPassword = findViewById(R.id.et_re_enter_password);
        mContactNo = findViewById(R.id.et_contact_no);
        mBtnSubmit = findViewById(R.id.buttonSubmit);
        fAuth = FirebaseAuth.getInstance();
        mBtnSignIN = findViewById(R.id.alreadyAccount);

        ContactNo = getIntent().getStringExtra("phone");
        mContactNo.setText(ContactNo);
        referenceMain = FirebaseDatabase.getInstance().getReference("data");
        reference2 = FirebaseDatabase.getInstance().getReference("username");
        referenceVendor = FirebaseDatabase.getInstance().getReference("credentials").child("vendor");

//////////////////////////////////////////////////////////////////////////////////////////////

        referenceVendor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxIDVendor = (int) dataSnapshot.getChildrenCount();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////

        mBtnSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

/////////////////////////////////////////////////////////////////////////////////////////////////

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Creating Account...");

                ShopName = mShopName.getText().toString();
                OwnerFirstName = mOwnerFirstName.getText().toString();
                OwnerLastName = mOwnerLastName.getText().toString();
                OwnerAddress = mOwnerAddress.getText().toString();
                GstNo = mGstNo.getText().toString();
                Email = mEmail.getText().toString();
                RecoveryEmail = mRecoveryEmail.getText().toString();
                Password = mPassword.getText().toString();
                ConfirmPassword = mConfirmPassword.getText().toString();
                PinCode = mPincode.getText().toString();
                UserName = mCreateUserName.getText().toString();

                reference2.child(UserName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            loadingDialog.dismissDialog();
                            mCreateUserName.setError("This username is already taken, try diffrent.");
                        } else {
                            if (ShopName.equals("") || OwnerFirstName.equals("") || OwnerLastName.equals("") ||
                                    OwnerAddress.equals("") || PinCode.equals("") || GstNo.equals("") || GstNo.length() < 15 || Email.equals("")
                                    || RecoveryEmail.equals("") || UserName.equals("") || Password.equals("") || ConfirmPassword.equals("") || Password.length() < 6) {
                                loadingDialog.dismissDialog();
                                if (ShopName.equals("")) {
                                    mShopName.setError("Please provide shop name");
                                } else if (OwnerFirstName.equals("")) {
                                    mOwnerFirstName.setError("Please provide owner first name");
                                } else if (OwnerLastName.equals("")) {
                                    mOwnerLastName.setError("Please provide owner last name");
                                } else if (OwnerAddress.equals("")) {
                                    mOwnerAddress.setError("Please provide owner address");
                                } else if (PinCode.equals("")) {
                                    mOwnerAddress.setError("Please provide Pin Code");
                                } else if (GstNo.equals("") || GstNo.length() < 15) {
                                    mGstNo.setError("Please provide GST No");
                                } else if (Email.equals("")) {
                                    mEmail.setError("Please E-mail address");
                                } else if (RecoveryEmail.equals("")) {
                                    mRecoveryEmail.setError("Please provide Recovery E-mail");
                                } else if (Password.equals("")) {
                                    mPassword.setError("Please provide password");
                                } else if (UserName.equals("")) {
                                    mCreateUserName.setError("Please provide username");
                                } else if (Password.length() < 6) {
                                    mPassword.setError("Password must be greater than 6");
                                } else if (ConfirmPassword.equals("")) {
                                    mConfirmPassword.setError("Please confirm the password");
                                }
                            } else {
                                try {
                                    if(validGSTIN(GstNo)){
                                        if (!ConfirmPassword.equals(Password)) {
                                            loadingDialog.dismissDialog();
                                            mConfirmPassword.setError("Password Dosent Match");
                                        }
                                        else {
                                            furtherUpload();
                                        }
                                    }
                                    else {
                                        loadingDialog.dismissDialog();
                                        mGstNo.setError("Invalid GST no");
                                    }
                                }
                                catch (Exception e){}
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignUp.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private static boolean validGSTIN(String gstNo) throws Exception {
        boolean isValidFormat = false;
        if (checkPattern(gstNo, GSTINFORMAT_REGEX)) {
            isValidFormat = verifyCheckDigit(gstNo);
        }
        return isValidFormat;

    }
    private static boolean verifyCheckDigit(String gstinWCheckDigit) throws Exception {
        Boolean isCDValid = false;
        String newGstninWCheckDigit = getGSTINWithCheckDigit(
                gstinWCheckDigit.substring(0, gstinWCheckDigit.length() - 1));

        if (gstinWCheckDigit.trim().equals(newGstninWCheckDigit)) {
            isCDValid = true;
        }
        return isCDValid;
    }
    public static boolean checkPattern(String inputval, String regxpatrn) {
        boolean result = false;
        if ((inputval.trim()).matches(regxpatrn)) {
            result = true;
        }
        return result;
    }
    public static String getGSTINWithCheckDigit(String gstinWOCheckDigit) throws Exception {
        int factor = 2;
        int sum = 0;
        int checkCodePoint = 0;
        char[] cpChars;
        char[] inputChars;

        try {
            if (gstinWOCheckDigit == null) {
                throw new Exception("GSTIN supplied for checkdigit calculation is null");
            }
            cpChars = GSTN_CODEPOINT_CHARS.toCharArray();
            inputChars = gstinWOCheckDigit.trim().toUpperCase().toCharArray();

            int mod = cpChars.length;
            for (int i = inputChars.length - 1; i >= 0; i--) {
                int codePoint = -1;
                for (int j = 0; j < cpChars.length; j++) {
                    if (cpChars[j] == inputChars[i]) {
                        codePoint = j;
                    }
                }
                int digit = factor * codePoint;
                factor = (factor == 2) ? 1 : 2;
                digit = (digit / mod) + (digit % mod);
                sum += digit;
            }
            checkCodePoint = (mod - (sum % mod)) % mod;
            return gstinWOCheckDigit + cpChars[checkCodePoint];
        } finally {
            inputChars = null;
            cpChars = null;
        }
    }

    private void furtherUpload() {
        UserNo = String.valueOf(maxIDVendor + 1);

        final VendorUpload v1 = new VendorUpload(ShopName, OwnerFirstName, OwnerLastName, OwnerAddress, PinCode, GstNo, Email, RecoveryEmail, ConfirmPassword,
                ContactNo, UserNo, loginId, Points, UserName, "active");

        final Upload u1 = new Upload(ConfirmPassword, ContactNo, loginId, UserNo, UserName);

        referenceVendor.child(String.valueOf(maxIDVendor + 1)).setValue(v1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                referenceMain.child(ContactNo).child(loginId).setValue(u1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        reference2.child(UserName).setValue(u1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                fAuth.signOut();
                                Toast.makeText(SignUp.this, "Successfully registered..", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, LoginActivity.class));
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
                Toast.makeText(SignUp.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
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
                fAuth.signOut();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}