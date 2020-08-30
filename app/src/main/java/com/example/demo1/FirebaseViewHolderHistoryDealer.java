package com.example.demo1;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolderHistoryDealer extends RecyclerView.ViewHolder {
    TextView date, points, status, nameandid;

    public FirebaseViewHolderHistoryDealer(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.recycler_dealer_date);
        points = itemView.findViewById(R.id.recycler_dealer_points);
        status = itemView.findViewById(R.id.recycler_dealer_status);
        nameandid = itemView.findViewById(R.id.recycler_dealer_nameandid);
    }
}
