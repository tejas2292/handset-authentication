package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SearchSerialNoVendor2 extends AppCompatActivity {
    RelativeLayout stolen;
    LinearLayout allData;
    Button mBtnStolen;

    Button mBtnEditDetails;
    TextView mVendorId, mShopName, mAddress, mSerialNo, mBuyerName, mBrandName, mModelNo,
            mWarrantyDate, mSNregNo;
    ImageView mBtnMob, mBtnInvoice;
    String UNo, shopName, address, serialNo, serialNo2, buyerFirstName, buyerLastName, brandName,
            modelNo, warrantyDate, imgMob, imgInvoice, vendorPhoneNo, contactFirst, contactSecond;
    DatabaseReference referenceVendor, referenceFIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_serial_no_vendor2);

        stolen = findViewById(R.id.stolen);
        allData = findViewById(R.id.allData);
        mBtnStolen = findViewById(R.id.btn_stolen);

        mBtnEditDetails = findViewById(R.id.btn_editDetails);
        mVendorId = findViewById(R.id.tv_vendor_id);
        mShopName = findViewById(R.id.tv_vendor_shop_name);
        mSerialNo = findViewById(R.id.tv_serial_no);
        mBuyerName = findViewById(R.id.tv_buyer_name);
        mBrandName = findViewById(R.id.tv_brand_name);
        mAddress = findViewById(R.id.tv_address);
        mModelNo = findViewById(R.id.tv_model_no);
        mBtnMob = findViewById(R.id.btn_mobBox);
        mWarrantyDate = findViewById(R.id.tv_warranty_period);
        mBtnInvoice = findViewById(R.id.btn_invoice);
        mSNregNo = findViewById(R.id.tv_sn_reg_no);

        serialNo = getIntent().getStringExtra("serialNo");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(serialNo);
        setSupportActionBar(toolbar);

        referenceFIR = FirebaseDatabase.getInstance().getReference("serials").child(serialNo).child("FIR");
        referenceFIR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    stolen.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) allData.getLayoutParams();
                    lp.addRule(RelativeLayout.BELOW, stolen.getId());
                    allData.setLayoutParams(lp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchSerialNoVendor2.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        if (serialNo != null) {
            referenceVendor = FirebaseDatabase.getInstance().getReference("serials");

            mBtnStolen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchSerialNoVendor2.this, SearchSerialNoVendor3.class);
                    intent.putExtra("buyerFirstName", buyerFirstName);
                    intent.putExtra("buyerLastName", buyerLastName);
                    intent.putExtra("contact", contactFirst);
                    intent.putExtra("serialNo", serialNo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

            mBtnEditDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchSerialNoVendor2.this, VendorEditDetailsRegisterdSerialMain.class);
                    intent.putExtra("contact", contactFirst);
                    intent.putExtra("contactSecond", contactSecond);
                    intent.putExtra("serial", serialNo);
                    intent.putExtra("vendorPhoneNo", vendorPhoneNo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

            referenceVendor.child(serialNo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        serialNo2 = dataSnapshot.child("modelSerial").getValue().toString();
                        buyerFirstName = dataSnapshot.child("buyerFirstName").getValue().toString();
                        buyerLastName = dataSnapshot.child("buyerLastName").getValue().toString();
                        modelNo = dataSnapshot.child("modelNo").getValue().toString();
                        warrantyDate = dataSnapshot.child("warrantyDate").getValue().toString();
                        imgMob = dataSnapshot.child("url1").getValue().toString();
                        imgInvoice = dataSnapshot.child("url2").getValue().toString();
                        UNo = dataSnapshot.child("vendorLoginID").getValue().toString();
                        shopName = dataSnapshot.child("vendorShopName").getValue().toString();
                        vendorPhoneNo = dataSnapshot.child("vendorContact").getValue().toString();
                        address = dataSnapshot.child("vendorAddress").getValue().toString();
                        brandName = dataSnapshot.child("brandName").getValue().toString();
                        contactFirst = dataSnapshot.child("contactFirst").getValue().toString();
                        contactSecond = dataSnapshot.child("contactSecond").getValue().toString();

                        mVendorId.setText(UNo);
                        mShopName.setText(shopName);
                        mAddress.setText(address);
                        mSNregNo.setText(vendorPhoneNo);
                        mSerialNo.setText(serialNo2);
                        mBuyerName.setText(buyerFirstName + " " + buyerLastName);
                        mBrandName.setText(brandName);
                        mModelNo.setText(modelNo);
                        mWarrantyDate.setText("till " + warrantyDate);

                        Picasso.get().load(imgMob).into(mBtnMob);
                        Picasso.get().load(imgInvoice).into(mBtnInvoice);


                        mBtnMob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder altdial = new AlertDialog.Builder(SearchSerialNoVendor2.this);
                                altdial.setCancelable(false)
                                        .setPositiveButton("Open Image", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(imgMob));
                                                startActivity(intent);
                                            }
                                        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = altdial.create();
                                alert.show();
                            }
                        });

                        mBtnInvoice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder altdial = new AlertDialog.Builder(SearchSerialNoVendor2.this);
                                altdial.setCancelable(false)
                                        .setPositiveButton("Open Image", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(imgInvoice));
                                                startActivity(intent);
                                            }
                                        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = altdial.create();
                                alert.show();
                            }
                        });

                    } else {
                        Toast.makeText(SearchSerialNoVendor2.this, "Record not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchSerialNoVendor2.this, SearchSerialNoVendor.class));
        finish();
    }
}
