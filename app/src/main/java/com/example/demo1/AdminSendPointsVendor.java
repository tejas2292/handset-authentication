package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AdminSendPointsVendor extends AppCompatActivity {
    DatabaseReference reference, reference2, historyVendor, historyAdmin, transaction;
    EditText mSearchVendorEditText, mSendinPoints;
    TextView mVendorName, mVendorShopName, mVendorAvailablePoints;
    LinearLayout linearLayout;
    String searchingParameter, userNo = null, ownerFirstName, ownerLastName, vendorShopName, vendorFullName, dbpoints,
            points, dealerUserNo, fin, timeStampDate, timeStampTime, dealerFullName, date;
    Button mBtnSearchVendor, mBtnSendPoints;
    LoadingDialog loadingDialog;
    int totalSerialAdmin = 0;
    int totalSerialVendor = 0;
    String stringTransaction;
    long transactionValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_send_points_vendor);
        loadingDialog = new LoadingDialog(AdminSendPointsVendor.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Send Points To Vendor");
        setSupportActionBar(toolbar);

        reference = FirebaseDatabase.getInstance().getReference().child("credentials").child("vendor");
        reference2 = FirebaseDatabase.getInstance().getReference().child("data");
        historyVendor = FirebaseDatabase.getInstance().getReference().child("HistoryVendor");
        historyAdmin = FirebaseDatabase.getInstance().getReference().child("HistoryAdmin");
        transaction = FirebaseDatabase.getInstance().getReference();

        mSearchVendorEditText = findViewById(R.id.et_searchVendor);
        mBtnSearchVendor = findViewById(R.id.btnSearchVendor);
        mVendorName = findViewById(R.id.tv_vendor_name);
        mVendorShopName = findViewById(R.id.tv_vendor_shop_name);
        linearLayout = findViewById(R.id.linear2);
        mBtnSendPoints = findViewById(R.id.btnSendPoints);
        mSendinPoints = findViewById(R.id.et_enterSendingPoints);
        mVendorAvailablePoints = findViewById(R.id.tv_vendor_availble_points);

        mBtnSearchVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("checking..");
                searchingParameter = mSearchVendorEditText.getText().toString().trim();
                if (searchingParameter.equals("")) {
                    loadingDialog.dismissDialog();
                    mSearchVendorEditText.setError("Enter Id / Mobile No.");
                } else {
                    checkMobileExist();
                }
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////

        historyAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    totalSerialAdmin = (int) dataSnapshot.getChildrenCount();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    //////////////////////////////////////////////////////////////////////////////////////////////////

        mBtnSendPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Sending...");
                points = mSendinPoints.getText().toString().trim();
                if (points.equals("")) {
                    loadingDialog.dismissDialog();
                    mSendinPoints.setError("Points are required..");
                } else {
                    int temp1 = Integer.parseInt(dbpoints);
                    int temp2 = Integer.parseInt(points);
                    int resultforvendor = temp1 + temp2;
                    fin = String.valueOf(resultforvendor);

                    if(temp2<=100000) {

                        transaction.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    stringTransaction = dataSnapshot.child("tadmin").getValue().toString();
                                    long temp = Long.parseLong(stringTransaction);
                                    transactionValue = temp + 1;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        loadingDialog.startLoadingDialog();
                        HashMap<String, Object> profileMap = new HashMap<>();
                        profileMap.put("points", fin);

                        reference.child(userNo).updateChildren(profileMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                timeStampDate = new SimpleDateFormat("d MMM yyyy").format(new Date());
                                timeStampTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                                date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

                                final historyAdminUploadFinal historyAdminUploadVendor = new historyAdminUploadFinal(String.valueOf(totalSerialAdmin + 1),
                                        date, vendorFullName, "V-" + userNo, points, "-", fin, "SentToVendor", vendorShopName, "A"+transactionValue);

                                final historyVendorUploadFinal history = new historyVendorUploadFinal(String.valueOf(totalSerialVendor + 1),
                                        date, "admin", "-", "-", points, "-", "ReceivedByAdmin", "A"+transactionValue);


                                transaction.child("tadmin").setValue(transactionValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        historyVendor.child(userNo).child("points").child(String.valueOf(totalSerialVendor + 1))
                                                .setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                historyAdmin.child(String.valueOf(totalSerialAdmin + 1)).setValue(historyAdminUploadVendor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        loadingDialog.dismissDialog();
                                                        Toast.makeText(AdminSendPointsVendor.this, "Points sent successfully...", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(AdminSendPointsVendor.this, MainActivityAdmin.class));
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AdminSendPointsVendor.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loadingDialog.dismissDialog();
                                                Toast.makeText(AdminSendPointsVendor.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(AdminSendPointsVendor.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(AdminSendPointsVendor.this, "You cant send more than 1,00,000 points",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void checkMobileExist() {
        reference2.child(searchingParameter).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userNo = dataSnapshot.child("userNo").getValue().toString();
                    nextPointSendingWork();
                } else {
                    reference.child(searchingParameter).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                userNo = dataSnapshot.child("userNo").getValue().toString();
                                nextPointSendingWork();
                            } else {
                                loadingDialog.dismissDialog();
                                Toast.makeText(AdminSendPointsVendor.this, "Vendor Not Present..", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(AdminSendPointsVendor.this, "Error: " + databaseError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismissDialog();
                Toast.makeText(AdminSendPointsVendor.this, "Error: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nextPointSendingWork() {
        ////////////////////////////////////////////////////////////////////////////////////////////
        historyVendor.child(userNo).child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    totalSerialVendor = (int) dataSnapshot.getChildrenCount();
                } else {
                    Toast.makeText(AdminSendPointsVendor.this, "not present", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        reference.child(userNo).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ownerFirstName = dataSnapshot.child("ownerFirstName").getValue().toString();
                ownerLastName = dataSnapshot.child("ownerLastName").getValue().toString();
                vendorShopName = dataSnapshot.child("shopeName").getValue().toString();
                dbpoints = dataSnapshot.child("points").getValue().toString();

                vendorFullName = ownerFirstName + " " + ownerLastName;
                mVendorName.setText(vendorFullName);
                mVendorShopName.setText(vendorShopName);
                mVendorAvailablePoints.setText(dbpoints);
                linearLayout.setVisibility(View.VISIBLE);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminSendPointsVendor.this, "Error: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminSendPointsVendor.this, AdminSendPointsOptions.class));
        finish();
    }
}
