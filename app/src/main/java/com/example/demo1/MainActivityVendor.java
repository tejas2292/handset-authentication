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

public class MainActivityVendor extends AppCompatActivity {
    Button mBtnRegisterNewSerial, mBtnSearchSerialNo, mBtnDeleteAccount, mBtnHistoryVendor;
    TextView mVendorId, mShopName, mAvailablePoints;
    String UserName, UNo, shopName, vendorPhoneNo, Points, vendorAddress, userName, password, timeStamp;
    int currentPoints = 0;
    public DatabaseReference referenceVendor, referenceMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vendor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Screen Vendor");
        setSupportActionBar(toolbar);

        mVendorId = findViewById(R.id.tv_vendor_id);
        mShopName = findViewById(R.id.tv_vendor_shop_name);
        mAvailablePoints = findViewById(R.id.tv_available_points);
        mBtnSearchSerialNo = findViewById(R.id.btnSearchSerialNo);
        mBtnRegisterNewSerial = findViewById(R.id.btn_registerNewSerialNo);
        mBtnDeleteAccount = findViewById(R.id.btn_deleteMyAccount);
        mBtnHistoryVendor = findViewById(R.id.btn_historyVendor);
        UserName = new UserCurrent(MainActivityVendor.this).getUsername().trim();

        referenceVendor = FirebaseDatabase.getInstance().getReference("data").child(UserName).child("1");
        referenceVendor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UNo = dataSnapshot.child("userNo").getValue().toString();
                //here we got the vendor id from data.. now we check it into credentials
                referenceMain = FirebaseDatabase.getInstance().getReference("credentials").child("vendor").child(UNo);
                referenceMain.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        vendorPhoneNo = dataSnapshot.child("contact").getValue().toString();
                        shopName = dataSnapshot.child("shopeName").getValue().toString();
                        Points = dataSnapshot.child("points").getValue().toString();
                        vendorAddress = dataSnapshot.child("ownerAddress").getValue().toString();
                        userName = dataSnapshot.child("userName").getValue().toString();
                        password = dataSnapshot.child("password").getValue().toString();

                        mAvailablePoints.setText("#" + Points);
                        currentPoints = Integer.parseInt(Points);
                        mVendorId.setText(UNo);
                        mShopName.setText(shopName);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivityVendor.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivityVendor.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mBtnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVendorId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityVendor.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivityVendor.this, DeleteMyAccount.class);
                    intent.putExtra("contact", vendorPhoneNo);
                    intent.putExtra("userName", userName);
                    intent.putExtra("pass", password);
                    intent.putExtra("vendorID", UNo);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mBtnSearchSerialNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVendorId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityVendor.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivityVendor.this, SearchSerialNoVendor.class);
                    intent.putExtra("uno", UNo);
                    intent.putExtra("shop", shopName);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mBtnHistoryVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVendorId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityVendor.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(MainActivityVendor.this, HistoryVendorOptions.class);
                    intent.putExtra("uno", UNo);
                    intent.putExtra("points",Points);
                    startActivity(intent);
                    finish();
                }
            }
        });


        mBtnRegisterNewSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVendorId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityVendor.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    if (currentPoints > 0) {
                        Intent intent = new Intent(MainActivityVendor.this, VendorRegisterSerialNo.class);
                        intent.putExtra("uno", UNo);
                        intent.putExtra("shop", shopName);
                        intent.putExtra("vendorPhone", vendorPhoneNo);
                        intent.putExtra("points", Points);
                        intent.putExtra("address", vendorAddress);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivityVendor.this, "You can't register.. You don't have enough points..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    //here exit app start..........................................
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityVendor.this);
        builder.setMessage("Are you sure want to exit from app?");
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

                Toast.makeText(this, "Settings.."+ timeStamp, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                new UserCurrent(MainActivityVendor.this).removeUser();
                Intent intent = new Intent(MainActivityVendor.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
