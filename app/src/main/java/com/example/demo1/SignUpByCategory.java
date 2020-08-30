package com.example.demo1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpByCategory extends AppCompatActivity {
    Button mBtnVendor, mBtnDealer, mBtnPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_by_category);

        mBtnVendor = (Button) findViewById(R.id.btn_vendor);
        mBtnDealer = (Button) findViewById(R.id.btn_dealer);
        mBtnPersonal = (Button) findViewById(R.id.btn_personal);

        mBtnVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpByCategory.this, SignUpVendor1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        mBtnDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpByCategory.this, SignUpDealer1.class));
                finish();
            }
        });

        mBtnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpByCategory.this, SignUpPersonal1.class));
                finish();
            }
        });

    }

    //here exit app start..........................................
    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpByCategory.this, LoginActivity.class));
        finish();
        //here exit app alert close............................................
    }
}