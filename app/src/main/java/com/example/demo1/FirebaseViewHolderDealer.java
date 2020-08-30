package com.example.demo1;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolderDealer extends RecyclerView.ViewHolder {
    TextView id, name, contact, points;
    Button deleteDealer;

    public FirebaseViewHolderDealer(@NonNull View itemView) {
        super(itemView);
        id = itemView.findViewById(R.id.recyclerIDDealer);
        name = itemView.findViewById(R.id.recyclerNameDealer);
        contact = itemView.findViewById(R.id.recyclerContactDealer);
        points = itemView.findViewById(R.id.recyclerPointsDealer);
        deleteDealer = itemView.findViewById(R.id.recyclerDeleteDealer);
    }
}
