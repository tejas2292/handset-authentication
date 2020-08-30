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

public class AdminDeductPointsDealer extends AppCompatActivity {
    DatabaseReference reference, reference2, historyDealer, historyAdmin, transaction;
    EditText mSearchDealerEditText, mDeductPoints;
    TextView mDealerName, mDealerAvailablePoints;
    LinearLayout linearLayout;
    String searchingParameter, userNo = null, ownerFirstName, ownerLastName, dealerFullName, dbpoints,
            points, dealerPoints, dealerUserNo, fin, timeStampDate, timeStampTime, date;
    Button mBtnSearchDealer, mBtnDeductPoints;
    LoadingDialog loadingDialog;
    int totalSerialDealer = 0;
    int totalSerialAdmin = 0;
    String stringTransaction;
    long transactionValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_deduct_points_dealer);
        loadingDialog = new LoadingDialog(AdminDeductPointsDealer.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Send Points To Dealer");
        setSupportActionBar(toolbar);

        reference = FirebaseDatabase.getInstance().getReference().child("credentials").child("dealer");
        reference2 = FirebaseDatabase.getInstance().getReference().child("data");
        historyDealer = FirebaseDatabase.getInstance().getReference().child("HistoryDealer");
        historyAdmin = FirebaseDatabase.getInstance().getReference().child("HistoryAdmin");

        transaction = FirebaseDatabase.getInstance().getReference();

        mSearchDealerEditText = findViewById(R.id.et_searchDealer);
        mBtnSearchDealer = findViewById(R.id.btnSearchDealer);
        mDealerName = findViewById(R.id.tv_dealer_name);
        linearLayout = findViewById(R.id.linear2);
        mBtnDeductPoints = findViewById(R.id.btnDeductPoints);
        mDeductPoints = findViewById(R.id.et_enterDeductingPoints);
        mDealerAvailablePoints = findViewById(R.id.tv_dealer_availble_points);

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
                Toast.makeText(AdminDeductPointsDealer.this, "Error: "+databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////

        mBtnSearchDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("checking..");
                searchingParameter = mSearchDealerEditText.getText().toString().trim();
                if (searchingParameter.equals("")) {
                    loadingDialog.dismissDialog();
                    mSearchDealerEditText.setError("Enter Id / Mobile No.");
                } else {
                    checkMobileExist();
                }
            }
        });

        mBtnDeductPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Sending...");
                points = mDeductPoints.getText().toString().trim();
                if (points.equals("")) {
                    loadingDialog.dismissDialog();
                    mDeductPoints.setError("Points are required..");
                } else {
                    int temp1 = Integer.parseInt(dbpoints);
                    int temp2 = Integer.parseInt(points);
                    int resultforvendor = temp1 - temp2;
                    fin = String.valueOf(resultforvendor);

                    if(resultforvendor >= 0) {

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

                                final historyAdminUploadFinal historyAdminUploadDealer = new historyAdminUploadFinal(String.valueOf(totalSerialAdmin + 1),
                                        date, dealerFullName, "D-"+userNo, "-", points, fin, "DeductFromDealer", dealerFullName,"A"+transactionValue);

                                final historyDealerUploadFinal historyDealerUploadVendor = new historyDealerUploadFinal(String.valueOf(totalSerialDealer + 1),
                                        date, "admin", "-", "-", points, "DeductedByAdmin","-", "A"+transactionValue);

                                transaction.child("tadmin").setValue(transactionValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        historyDealer.child(userNo).child(String.valueOf(totalSerialDealer + 1))
                                                .setValue(historyDealerUploadVendor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                historyAdmin.child(String.valueOf(totalSerialAdmin + 1)).setValue(historyAdminUploadDealer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(AdminDeductPointsDealer.this, "Points deducted successfully...", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(AdminDeductPointsDealer.this, MainActivityAdmin.class));
                                                        finish();
                                                        loadingDialog.dismissDialog();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AdminDeductPointsDealer.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loadingDialog.dismissDialog();
                                                Toast.makeText(AdminDeductPointsDealer.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(AdminDeductPointsDealer.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(AdminDeductPointsDealer.this, "You can't deduct more than available points..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void checkMobileExist() {
        reference2.child(searchingParameter).child("2").addValueEventListener(new ValueEventListener() {
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
                                Toast.makeText(AdminDeductPointsDealer.this, "Dealer Not Present..", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(AdminDeductPointsDealer.this, "Error: " + databaseError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismissDialog();
                Toast.makeText(AdminDeductPointsDealer.this, "Error: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nextPointSendingWork() {
        ////////////////////////////////////////////////////////////////////////////////////////////
        historyDealer.child(userNo).addValueEventListener(new ValueEventListener() {
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

        reference.child(userNo).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ownerFirstName = dataSnapshot.child("ownerFirstName").getValue().toString();
                ownerLastName = dataSnapshot.child("ownerLastName").getValue().toString();
                dbpoints = dataSnapshot.child("points").getValue().toString();

                dealerFullName = ownerFirstName + " " + ownerLastName;
                mDealerName.setText(dealerFullName);
                mDealerAvailablePoints.setText(dbpoints);
                linearLayout.setVisibility(View.VISIBLE);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDeductPointsDealer.this, "Error: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminDeductPointsDealer.this, AdminSendPointsOptions.class));
        finish();
    }
}
