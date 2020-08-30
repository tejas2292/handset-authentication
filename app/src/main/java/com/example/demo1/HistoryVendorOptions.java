package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryVendorOptions extends AppCompatActivity {
    Button mBtnPoints, mBtnSerial, mBtnDownloadHistoryPoints, mBtnDownloadHistorySN;
    String vendorUserNo, points, timeStampTime;

    DatabaseReference historySN;
    ArrayList<HistoryVendorUpload> list;
    HistoryVendorUpload history;
    OutputStream output;
    String[] columns = {"Sr.", "Date", "Registered S/N", "Invoice no", "Transaction Id"};
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_vendor_options);

        loadingDialog = new LoadingDialog(HistoryVendorOptions.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);

        mBtnPoints = findViewById(R.id.btn_historyPoints);
        mBtnSerial = findViewById(R.id.btn_historySerialNo);
        mBtnDownloadHistoryPoints = findViewById(R.id.btn_downloadhistoryPoints);
        mBtnDownloadHistorySN = findViewById(R.id.btn_downloadhistorySerialNo);

        vendorUserNo = getIntent().getStringExtra("uno");
        points = getIntent().getStringExtra("points");

        list = new ArrayList<>();
        historySN = FirebaseDatabase.getInstance().getReference().child("HistoryVendor").child(vendorUserNo).child("serial");
        history = new HistoryVendorUpload();


        mBtnPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryVendorOptions.this, HistoryVendorPoints1.class);
                intent.putExtra("vendorUserNo", vendorUserNo);
                startActivity(intent);
                finish();
            }
        });

        mBtnSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryVendorOptions.this, HistoryVendorSerials.class);
                intent.putExtra("vendorUserNo", vendorUserNo);
                startActivity(intent);
                finish();
            }
        });
        mBtnDownloadHistoryPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryVendorOptions.this, VendorDownloadHistoryPoints.class);
                intent.putExtra("vendorUserNo", vendorUserNo);
                intent.putExtra("points", points);
                startActivity(intent);
                finish();
            }
        });

        mBtnDownloadHistorySN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(HistoryVendorOptions.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) HistoryVendorOptions.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                    return;
                }

                loadingDialog.startLoadingDialog();
                loadingDialog.setText("downloading...");

                historySN.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                history = ds.getValue(HistoryVendorUpload.class);
                                list.add(history);
                            }
                            downloadHistory();
                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(HistoryVendorOptions.this, "No S/N Present..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loadingDialog.dismissDialog();
                    }
                });

            }
        });
    }

    private void downloadHistory() {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("History Serial Numbers"); //Creating a sheet

        PrintSetup ps = sheet.getPrintSetup();
        sheet.setAutobreaks(true);

        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);

        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 15);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCellStyle1 = workbook.createCellStyle();
        headerCellStyle1.setFont(titleFont);
        headerCellStyle1.setWrapText(true);
        headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //headerCellStyle.setVerticalAlignment();

        Row rowTitle = sheet.createRow(0);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("History of Registered Serial Numbers");
        cellTitle.setCellStyle(headerCellStyle1);
        rowTitle.setHeightInPoints(3 * sheet.getDefaultRowHeightInPoints());

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(2 * sheet.getDefaultRowHeightInPoints());


        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        CellStyle normal = workbook.createCellStyle();
        normal.setAlignment(HorizontalAlignment.CENTER);
        normal.setVerticalAlignment(VerticalAlignment.CENTER);


        int rowNum = 2;
        int temp = 1;
        for (HistoryVendorUpload his : list) {
            Row row = sheet.createRow(rowNum++);
            row.setHeightInPoints(20);
            Cell cell0 = row.createCell(0);
            Cell cell1 = row.createCell(1);
            Cell cell2 = row.createCell(2);
            Cell cell3 = row.createCell(3);
            Cell cell4 = row.createCell(4);

            cell0.setCellStyle(normal);
            cell1.setCellStyle(normal);
            cell2.setCellStyle(normal);
            cell3.setCellStyle(normal);
            cell4.setCellStyle(normal);

            cell0.setCellValue(temp++);
            cell1.setCellValue(his.date);
            cell2.setCellValue(his.serialNo);
            cell3.setCellValue(his.invoice);
            cell4.setCellValue(his.transactionId);

        }
        sheet.setColumnWidth(0, (8 * 100));
        sheet.setColumnWidth(1, (20 * 250));
        sheet.setColumnWidth(2, (25 * 250));
        sheet.setColumnWidth(3, (25 * 250));
        sheet.setColumnWidth(4, (25 * 250));

        timeStampTime = new SimpleDateFormat("HHmmss").format(new Date());

        String name = "vendorSN" + timeStampTime + "History";
        String fileName = name + ".xlsx"; //Name of the file

        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        File folder = new File(extStorageDirectory, "demo1");// Name of the folder you want to keep your file in the local storage.
        folder.mkdir(); //creating the folder
        File file = new File(folder, fileName);
        try {
            file.createNewFile(); // creating the file inside the folder
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(file); //Opening the file
            workbook.write(fileOut); //Writing all your row column inside the file
            fileOut.close(); //closing the file and done
            // Toast.makeText(HistoryVendorOptions.this, "Excel file saved in your device....", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////


        String pdfName = name + ".pdf";
        File pdfFile = new File(folder, pdfName);
        try {
            output = new FileOutputStream(pdfFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Document document = new Document(PageSize.A4.rotate());
        PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.rotate().getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Sr.");
        table.addCell("Date");
        table.addCell("Registered S/N");
        table.addCell("Invoice no");
        table.addCell("Transaction Id");
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();
        for (int i = 0; i < cells.length; i++) {
            cells[i].setBackgroundColor(BaseColor.GRAY);
        }
        int temp2 = 1;
        for (HistoryVendorUpload his2 : list) {
            table.addCell(String.valueOf(temp2++));
            table.addCell(his2.date);
            table.addCell(his2.serialNo);
            table.addCell(his2.invoice);
            table.addCell(his2.transactionId);
        }

        try {
            PdfWriter.getInstance(document, output);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();
        PdfPTable table2 = new PdfPTable(new float[]{5});
        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.getDefaultCell().setFixedHeight(60);
        table2.setTotalWidth(PageSize.A4.rotate().getWidth());
        table2.setWidthPercentage(100);
        table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
                20.0f);
        PdfPCell cell1 = new PdfPCell(new Phrase("History of Registered Serial Numbers", f));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setFixedHeight(70);
        table2.addCell(cell1);

        try {
            document.add(table2);
            document.add(table);
            Toast.makeText(this, "files are saved in your device.", Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();


        ////////////////////////////////////////////////////////////////////////////////////////////
        loadingDialog.dismissDialog();
        startActivity(new Intent(HistoryVendorOptions.this, MainActivityVendor.class));
        finish();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HistoryVendorOptions.this, MainActivityVendor.class));
        finish();
    }
}
