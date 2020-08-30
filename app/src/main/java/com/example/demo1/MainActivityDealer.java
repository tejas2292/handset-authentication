package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivityDealer extends AppCompatActivity {
    Button mBtnSendPointsToVendor, mBtnSearchSerialNo, mBtnDeleteAccount, mBtnHistory, mBtnHistoryDownload;
    TextView mDealerId, mDealerName, mDealerAvailablePoints;
    String UserName, UNo, dealerFirstName, dealerLastName, dealerPoints, timeStampDate, dealerFullName;
    public DatabaseReference referenceDealer, referenceMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dealer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Screen Dealer");
        setSupportActionBar(toolbar);

        mBtnSearchSerialNo = findViewById(R.id.btnSearchSerialNoDealer);
        mBtnSendPointsToVendor = findViewById(R.id.btn_sendpointstovendor);
        mBtnDeleteAccount = findViewById(R.id.btn_deleteMyAccount);
        mBtnHistory = findViewById(R.id.btn_historyDealer);
        mBtnHistoryDownload = findViewById(R.id.btn_historyDownloadDealer);

        mDealerId = findViewById(R.id.tv_dealer_id);
        mDealerName = findViewById(R.id.tv_dealer_name);
        mDealerAvailablePoints = findViewById(R.id.tv_available_points);

        UserName = new UserCurrent(MainActivityDealer.this).getUsername().trim();

        referenceDealer = FirebaseDatabase.getInstance().getReference("data").child(UserName).child("2");

        referenceDealer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UNo = dataSnapshot.child("userNo").getValue().toString();
                    //here we got the vendor id from data.. now we check it into credentials
                    referenceMain = FirebaseDatabase.getInstance().getReference("credentials").child("dealer").child(UNo);
                    referenceMain.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dealerFirstName = dataSnapshot.child("ownerFirstName").getValue().toString();
                            dealerLastName = dataSnapshot.child("ownerLastName").getValue().toString();
                            dealerPoints = dataSnapshot.child("points").getValue().toString();

                            mDealerId.setText(UNo);
                            dealerFullName = dealerFirstName + " " + dealerLastName;
                            mDealerName.setText(dealerFullName);
                            mDealerAvailablePoints.setText("#"+dealerPoints);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivityDealer.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivityDealer.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mBtnSearchSerialNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDealerId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityDealer.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivityDealer.this, SearchSerialNoDealer.class));
                    finish();
                }
            }
        });

        mBtnHistoryDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDealerId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityDealer.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivityDealer.this, DealerDownloadHistory.class);
                    intent.putExtra("dealerUserNo", UNo);
                    intent.putExtra("dealerPoints", dealerPoints);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mBtnSendPointsToVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDealerId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityDealer.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivityDealer.this, SendPointsToVendor.class);
                    intent.putExtra("dealerUserNo", UNo);
                    intent.putExtra("dealerPoints", dealerPoints);
                    intent.putExtra("dealerName", dealerFullName);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mBtnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDealerId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityDealer.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivityDealer.this, HistoryDealerPoints1.class);
                    intent.putExtra("dealerUserNo", UNo);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mBtnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivityDealer.this, "Delete my account dealer....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //here exit app start..........................................
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityDealer.this);
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
        alertDialog.show();
        //here exit app alert close............................................
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //timeStampDate = new SimpleDateFormat("d MMM yyyy").format(new Date());
                timeStampDate = new SimpleDateFormat("HH:mm:ss").format(new Date());

                Toast.makeText(this, "Settings.." + timeStampDate, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                new UserCurrent(MainActivityDealer.this).removeUser();
                Intent intent = new Intent(MainActivityDealer.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
