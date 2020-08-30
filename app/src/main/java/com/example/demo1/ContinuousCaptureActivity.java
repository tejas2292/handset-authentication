package com.example.demo1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity {
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    public String lastText, path;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();


            //Added preview of scanned barcode
            getImageUri(ContinuousCaptureActivity.this, result.getBitmapWithResultPoints(Color.YELLOW));
            VendorRegisterSerialNo.pickedImageUri = Uri.parse(path);
            Picasso.get().load(path).into(VendorRegisterSerialNo.img1);
            //VendorRegisterSerialNo.img1.setImageURI(Uri.parse(path));

            VendorRegisterSerialNo.mModelSerialNo.setText(lastText);
            VendorRegisterSerialNo.mModelSerialNo.setFocusable(false);
            VendorRegisterSerialNo.mModelSerialNo.setFocusableInTouchMode(false);
            VendorRegisterSerialNo.mModelSerialNo.setClickable(false);
            VendorRegisterSerialNo.mModelSerialNoConfirm.setText(lastText);
            VendorRegisterSerialNo.mModelSerialNoConfirm.setFocusable(false);
            VendorRegisterSerialNo.mModelSerialNoConfirm.setFocusableInTouchMode(false);
            VendorRegisterSerialNo.mModelSerialNoConfirm.setClickable(false);

            //MainActivity.mImg.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
            onBackPressed();

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.continuous_scan);

        barcodeView = findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
