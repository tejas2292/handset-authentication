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

public class SendPointsToVendor extends AppCompatActivity {
    DatabaseReference reference, reference2, referenceDealer, historyDealer, historyVendor, transaction;
    EditText mSearchVendorEditText, mSendinPoints;
    TextView mVendorName, mVendorShopName, mVendorAvailablePoints;
    LinearLayout linearLayout;
    String searchingParameter, userNo = null, ownerFirstName, ownerLastName, vendorShopName, vendorFullName, dbpoints,
            points, dealerPoints, dealerUserNo, fin, fin2, timeStampDate, timeStampTime, dealerFullName, date;
    Button mBtnSearchVendor, mBtnSendPoints;
    LoadingDialog loadingDialog;
    int totalSerialDealer = 0, totalSerialVendor = 0;
    String stringTransaction;
    long transactionValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_points_to_vendor);
        loadingDialog = new LoadingDialog(SendPointsToVendor.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Send Points To Vendor");
        setSupportActionBar(toolbar);

        dealerPoints = getIntent().getStringExtra("dealerPoints");
        dealerUserNo = getIntent().getStringExtra("dealerUserNo");
        dealerFullName = getIntent().getStringExtra("dealerName");
        transaction = FirebaseDatabase.getInstance().getReference();



        reference = FirebaseDatabase.getInstance().getReference().child("credentials").child("vendor");
        referenceDealer = FirebaseDatabase.getInstance().getReference().child("credentials").child("dealer");
        reference2 = FirebaseDatabase.getInstance().getReference().child("data");
        historyDealer = FirebaseDatabase.getInstance().getReference().child("HistoryDealer");
        historyVendor = FirebaseDatabase.getInstance().getReference().child("HistoryVendor");

        mSearchVendorEditText = findViewById(R.id.et_searchVendor);
        mBtnSearchVendor = findViewById(R.id.btnSearchVendor);
        mVendorName = findViewById(R.id.tv_vendor_name);
        mVendorShopName = findViewById(R.id.tv_vendor_shop_name);
        linearLayout = findViewById(R.id.linear2);
        mBtnSendPoints = findViewById(R.id.btnSendPoints);
        mSendinPoints = findViewById(R.id.et_enterSendingPoints);
        mVendorAvailablePoints = findViewById(R.id.tv_vendor_availble_points);

        ////////////////////////////////////////////////////////////////////////////////////////////
        historyDealer.child(dealerUserNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    totalSerialDealer = (int) dataSnapshot.getChildrenCount();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

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

        mBtnSendPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Sending...");

                points = mSendinPoints.getText().toString().trim();
                if(points.equals("")){
                    loadingDialog.dismissDialog();
                    mSendinPoints.setError("Points are required..");
                }
                else {
                    int temp1 = Integer.parseInt(dbpoints);
                    int temp2 = Integer.parseInt(points);
                    int temp3 = Integer.parseInt(dealerPoints);
                    int resultforvendor = temp1 + temp2;
                    int resultfordealer = temp3 - temp2;
                    fin = String.valueOf(resultforvendor);
                    fin2 = String.valueOf(resultfordealer);

                    if(resultfordealer >= 0) {

                        transaction.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    stringTransaction = dataSnapshot.child("tdealer").getValue().toString();
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
                                HashMap<String, Object> profileMap2 = new HashMap<>();
                                profileMap2.put("points", fin2);

                                referenceDealer.child(dealerUserNo).updateChildren(profileMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        timeStampDate = new SimpleDateFormat("d MMM yyyy").format(new Date());
                                        timeStampTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                                        date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());


                                        final historyDealerUploadFinal history = new historyDealerUploadFinal(String.valueOf(totalSerialDealer + 1),
                                                date, vendorFullName, userNo, "-", points, "DeductedByOwn", vendorShopName,"D"+transactionValue);


                                        final historyVendorUploadFinal his = new historyVendorUploadFinal(String.valueOf(totalSerialVendor + 1),
                                                date, dealerFullName, dealerUserNo, "-", points, "-", "ReceivedByDealer","D"+transactionValue);

                                        transaction.child("tdealer").setValue(transactionValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                historyDealer.child(dealerUserNo).child(String.valueOf(totalSerialDealer + 1))
                                                        .setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        historyVendor.child(userNo).child("points").child(String.valueOf(totalSerialVendor + 1)).setValue(his).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(SendPointsToVendor.this, "Points sent successfully...", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(SendPointsToVendor.this, MainActivityDealer.class));
                                                                finish();
                                                                loadingDialog.dismissDialog();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                loadingDialog.dismissDialog();
                                                                Toast.makeText(SendPointsToVendor.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        loadingDialog.dismissDialog();
                                                        Toast.makeText(SendPointsToVendor.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadingDialog.dismissDialog();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(SendPointsToVendor.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(SendPointsToVendor.this, "You don't have enough points.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(SendPointsToVendor.this, "Vendor Not Present..", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(SendPointsToVendor.this, "Error: " + databaseError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismissDialog();
                Toast.makeText(SendPointsToVendor.this, "Error: " + databaseError.getMessage(),
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

                vendorFullName = ownerFirstName+" "+ownerLastName;
                mVendorName.setText(vendorFullName);
                mVendorShopName.setText(vendorShopName);
                mVendorAvailablePoints.setText(dbpoints);
                linearLayout.setVisibility(View.VISIBLE);
                loadingDialog.dismissDialog();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SendPointsToVendor.this, "Error: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(SendPointsToVendor.this, MainActivityDealer.class));
        finish();
    }
}
