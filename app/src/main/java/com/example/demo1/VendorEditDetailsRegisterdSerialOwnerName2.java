package com.example.demo1;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class VendorEditDetailsRegisterdSerialOwnerName2 extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText mNewBuyerFirstName, mNewBuyerLastName, mPurchaserCont1, mPurchaserCont2, mEmail, mRecovery;
    Button mBtnChangeName;
    String newBuyerFirstName, newBuyerLastName, contact1, contact2, serialNo, email, recovery;
    DatabaseReference referenceSerial;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit_details_registerd_serial_owner_name2);

        serialNo = getIntent().getStringExtra("serial");

        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Change Buyer Details");
        setSupportActionBar(toolbar);

        loadingDialog = new LoadingDialog(VendorEditDetailsRegisterdSerialOwnerName2.this);

        referenceSerial = FirebaseDatabase.getInstance().getReference("serials");

        mNewBuyerFirstName = findViewById(R.id.et_newBuyerFirstName);
        mNewBuyerLastName = findViewById(R.id.et_newBuyerLastName);
        mBtnChangeName = findViewById(R.id.btn_changeOwner);
        mPurchaserCont1 = findViewById(R.id.et_purchaserCont);
        mPurchaserCont2 = findViewById(R.id.et_purchaserContAlterenative);
        mEmail = findViewById(R.id.et_email);
        mRecovery = findViewById(R.id.et_recovery_email);
        mBtnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBuyerFirstName = mNewBuyerFirstName.getText().toString().trim();
                newBuyerLastName = mNewBuyerLastName.getText().toString().trim();
                contact1 = mPurchaserCont1.getText().toString().trim();
                contact2 = mPurchaserCont2.getText().toString().trim();
                email = mEmail.getText().toString().trim();
                recovery = mRecovery.getText().toString().trim();

                if (newBuyerFirstName.equals("") || newBuyerLastName.equals("") || contact1.equals("")
            || contact2.equals("") || email.equals("") || recovery.equals("")) {
                    if (newBuyerFirstName.equals("")) {
                        mNewBuyerFirstName.setError("Owner First Name is Required..");
                    } else if (newBuyerLastName.equals("")) {
                        mNewBuyerLastName.setError("Owner Last Name is Required..");
                    } else if (contact1.equals("")) {
                        mPurchaserCont1.setError("Contact no is Required..");
                    } else if (contact2.equals("")) {
                        mPurchaserCont2.setError("Alternate Contact no is Required..");
                    }
                    else if (email.equals("")) {
                        mEmail.setError("Email-id is Required..");
                    }
                    else if (recovery.equals("")) {
                        mRecovery.setError("Recovery Email-id is Required..");
                    }
                } else {
                    loadingDialog.startLoadingDialog();
                    loadingDialog.setText("Changing...");
                    referenceSerial.child(serialNo).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                HashMap<String, Object> profileMap = new HashMap<>();
                                profileMap.put("buyerFirstName", newBuyerFirstName);
                                profileMap.put("buyerLastName", newBuyerLastName);
                                profileMap.put("contactFirst", contact1);
                                profileMap.put("contactSecond", contact2);
                                profileMap.put("email", email);
                                profileMap.put("recoveryEmail", recovery);


                                referenceSerial.child(serialNo).updateChildren(profileMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(VendorEditDetailsRegisterdSerialOwnerName2.this,
                                                        "Buyer Name Changed Successfully.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(VendorEditDetailsRegisterdSerialOwnerName2.this,
                                                        MainActivityVendor.class));
                                                finish();
                                                loadingDialog.dismissDialog();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(VendorEditDetailsRegisterdSerialOwnerName2.this,
                                                "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismissDialog();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(VendorEditDetailsRegisterdSerialOwnerName2.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    });

                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(VendorEditDetailsRegisterdSerialOwnerName2.this);
        builder.setMessage("Are you sure want to go back on main screen?");
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
                startActivity(new Intent(VendorEditDetailsRegisterdSerialOwnerName2.this,
                        MainActivityVendor.class));
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //here exit app alert close............................................
    }

}