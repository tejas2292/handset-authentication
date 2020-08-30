package com.example.demo1;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolderPersonal extends RecyclerView.ViewHolder {
    TextView id, name, contact;
    Button deletePersonal;
    public FirebaseViewHolderPersonal(@NonNull View itemView) {
        super(itemView);
        id = itemView.findViewById(R.id.recyclerIDPersonal);
        name = itemView.findViewById(R.id.recyclerNamePersonal);
        contact = itemView.findViewById(R.id.recyclerContactPersonal);
        deletePersonal = itemView.findViewById(R.id.recyclerDeletePersonal);
    }
}
