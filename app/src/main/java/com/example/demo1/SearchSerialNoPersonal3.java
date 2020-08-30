package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SearchSerialNoPersonal3 extends AppCompatActivity {
    String buyerFirstName, buyerLastName, contact, serialNo, firNo, policeAddress, five, four, three,
            two, one;
    TextView mBuyerName, mContact, mFirNo, mAddressPolice;
    ImageView img5, img4, img3, img2, img1;
    DatabaseReference referenceFIR, referenceFIRImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_serial_no_personal3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("FIR Information");
        setSupportActionBar(toolbar);

        buyerFirstName = getIntent().getStringExtra("buyerFirstName");
        buyerLastName = getIntent().getStringExtra("buyerLastName");
        contact = getIntent().getStringExtra("contact");
        serialNo = getIntent().getStringExtra("serialNo");

        mBuyerName = findViewById(R.id.tv_buyer_name);
        mContact = findViewById(R.id.tv_contact);
        mFirNo = findViewById(R.id.tv_firno);
        mAddressPolice = findViewById(R.id.tv_policestationaddress);
        img5 = findViewById(R.id.img5);
        img4 = findViewById(R.id.img4);
        img3 = findViewById(R.id.img3);
        img2 = findViewById(R.id.img2);
        img1 = findViewById(R.id.img1);

        referenceFIR = FirebaseDatabase.getInstance().getReference("serials").child(serialNo).child("FIR");
        referenceFIRImages = FirebaseDatabase.getInstance().getReference("serials").child(serialNo).child("FIRIMAGE");

        referenceFIR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    firNo = dataSnapshot.child("firno").getValue().toString();
                    policeAddress = dataSnapshot.child("policeStationAddress").getValue().toString();

                    mBuyerName.setText(buyerFirstName + " " + buyerLastName);
                    mContact.setText(contact);
                    mFirNo.setText(firNo);
                    mAddressPolice.setText(policeAddress);

                } else {
                    Toast.makeText(SearchSerialNoPersonal3.this, "Error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchSerialNoPersonal3.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        referenceFIRImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("5").exists()) {
                        five = dataSnapshot.child("5").getValue().toString();
                    }
                    if (dataSnapshot.child("4").exists()) {
                        four = dataSnapshot.child("4").getValue().toString();
                    }
                    if (dataSnapshot.child("3").exists()) {
                        three = dataSnapshot.child("3").getValue().toString();
                    }
                    if (dataSnapshot.child("2").exists()) {
                        two = dataSnapshot.child("2").getValue().toString();
                    }
                    if (dataSnapshot.child("1").exists()) {
                        one = dataSnapshot.child("1").getValue().toString();
                    }

                    Picasso.get().load(five).into(img5);
                    Picasso.get().load(four).into(img4);
                    Picasso.get().load(three).into(img3);
                    Picasso.get().load(two).into(img2);
                    Picasso.get().load(one).into(img1);
                }

            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
                Toast.makeText(SearchSerialNoPersonal3.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchSerialNoPersonal3.this, SearchSerialNoPersonal2.class);
        intent.putExtra("serialNo", serialNo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}