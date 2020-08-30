package com.example.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminSendPointsOptions extends AppCompatActivity {
    Button mBtnSendPointsToVendor, mBtnSendPointsToDealer, mBtnAdminHistory, mBtnDeductPointsVendor,
            mBtnDeductPointsDealer, mbtnDownloadHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_send_points_options);

        mBtnSendPointsToVendor = findViewById(R.id.btn_sendpointstovendor);
        mBtnSendPointsToDealer = findViewById(R.id.btn_sendpointstodealer);
        mBtnAdminHistory = findViewById(R.id.btn_adminHistory);
        mBtnDeductPointsVendor = findViewById(R.id.btn_deductfromvendor);
        mBtnDeductPointsDealer = findViewById(R.id.btn_deductfromdealer);
        mbtnDownloadHistory = findViewById(R.id.btn_downloadHistory);

        mBtnSendPointsToVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSendPointsOptions.this, AdminSendPointsVendor.class));
                finish();
            }
        });

        mbtnDownloadHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSendPointsOptions.this, AdminDownloadHistory.class));
                finish();
            }
        });

        mBtnSendPointsToDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSendPointsOptions.this, AdminSendPointsDealer.class));
                finish();
            }
        });

        mBtnAdminHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSendPointsOptions.this, AdminHistory1.class));
                finish();
            }
        });

        mBtnDeductPointsVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSendPointsOptions.this, AdminDeductPointsVendor.class));
                finish();
            }
        });

        mBtnDeductPointsDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSendPointsOptions.this, AdminDeductPointsDealer.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminSendPointsOptions.this, MainActivityAdmin.class));
        finish();
    }
}
