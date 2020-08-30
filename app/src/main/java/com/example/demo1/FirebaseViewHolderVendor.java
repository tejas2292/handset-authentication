package com.example.demo1;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolderVendor extends RecyclerView.ViewHolder {
    TextView id, name, shopName, contact, points, gst;
    Button deleteVendor;

    public FirebaseViewHolderVendor(@NonNull View itemView) {
        super(itemView);

        id = itemView.findViewById(R.id.recyclerIDVendor);
        name = itemView.findViewById(R.id.recyclerNameVendor);
        shopName = itemView.findViewById(R.id.recyclerShopNameVendor);
        contact = itemView.findViewById(R.id.recyclerContactVendor);
        points = itemView.findViewById(R.id.recyclerPointsVendor);
        gst = itemView.findViewById(R.id.recyclerGSTNo);
        deleteVendor = itemView.findViewById(R.id.recyclerDeleteVendor);
    }
}
