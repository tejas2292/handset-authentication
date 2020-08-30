package com.example.demo1;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolderVendorRegisteredSerial extends RecyclerView.ViewHolder {
    TextView serialNo, date;
    public FirebaseViewHolderVendorRegisteredSerial(@NonNull View itemView) {
        super(itemView);
        serialNo = itemView.findViewById(R.id.recyclerSerialNo);
    }
}
