package com.example.demo1;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog {
    public Activity activity;
    public AlertDialog dialog;
    TextView textView;

    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View content = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(content);
        builder.setCancelable(false);
        textView = content.findViewById(R.id.textViewCustomDialog);
        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }

    void setText(String val){
        textView.setText(val);
    }
}
