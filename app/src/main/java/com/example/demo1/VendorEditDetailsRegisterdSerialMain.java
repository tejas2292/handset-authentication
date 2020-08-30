package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VendorEditDetailsRegisterdSerialMain extends AppCompatActivity {
    Button mBtnChangeOwnerName, mBtnClaimFIR;
    String contact, contactWithPlus, serial,contactSecond, contactSecondWithPlus,vendorPhoneNo;
    DatabaseReference referenceFIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit_details_registerd_serial_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Details");
        setSupportActionBar(toolbar);

        mBtnChangeOwnerName = findViewById(R.id.btn_changeOwnerName);
        mBtnClaimFIR = findViewById(R.id.btn_claimFIR);

        contact = getIntent().getStringExtra("contact");
        contactSecond = getIntent().getStringExtra("contactSecond");
        vendorPhoneNo = getIntent().getStringExtra("vendorPhoneNo");
        serial = getIntent().getStringExtra("serial");
        contactWithPlus = ("+91"+contact).trim();
        contactSecondWithPlus = ("+91"+contactSecond).trim();

        referenceFIR = FirebaseDatabase.getInstance().getReference("serials").child(serial).child("FIR");
        referenceFIR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mBtnClaimFIR.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VendorEditDetailsRegisterdSerialMain.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mBtnChangeOwnerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorEditDetailsRegisterdSerialMain.this, VendorEditDetailsRegisterdSerialOwnerName1.class);
                intent.putExtra("contactWithPlus", contactWithPlus);
                intent.putExtra("contactSecondWithPlus", contactSecondWithPlus);
                intent.putExtra("serial", serial);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        mBtnClaimFIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorEditDetailsRegisterdSerialMain.this, VendorEditDetailsRegisterdSerialFIR1.class);
                intent.putExtra("contactWithPlus", contactWithPlus);
                intent.putExtra("contactSecondWithPlus", contactSecondWithPlus);
                intent.putExtra("serial", serial);
                intent.putExtra("vendorPhoneNo", vendorPhoneNo);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VendorEditDetailsRegisterdSerialMain.this, SearchSerialNoVendor2.class);
        intent.putExtra("serialNo", serial);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
