package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import java.util.Calendar;
import java.util.Date;

public class AdminDownloadHistory extends AppCompatActivity {
    Button mDownload;
    DatabaseReference historyAdmin;
    ArrayList<historyAdminUploadFinal> list;
    historyAdminUploadFinal history;
    String timeStampTime, startDate, endDate;
    OutputStream output;
    TextView mStartDate, mEndDate;
    LoadingDialog loadingDialog;

    String[] columns = {"Sr.", "Date", "Dealer/Vendor Name", "Shop Name", "V/D Login Id","Transaction Id",
            "Sent Point", "Deduct Point", "Available points of ven-deal \n after sending-deducting"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_download_history);
        mDownload = findViewById(R.id.download);

        loadingDialog = new LoadingDialog(AdminDownloadHistory.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Download History Admin");
        setSupportActionBar(toolbar);

        mStartDate = findViewById(R.id.tv_startDate);
        mEndDate = findViewById(R.id.tv_endDate);

        final DialogFragment dialogFragment = new DatePickerDialogTheme4();

        list = new ArrayList<>();
        historyAdmin = FirebaseDatabase.getInstance().getReference().child("HistoryAdmin");
        history = new historyAdminUploadFinal();

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogTheme4.id = "start";
                dialogFragment.show(getSupportFragmentManager(), "theme 4");
            }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogTheme4.id = "end";
                dialogFragment.show(getSupportFragmentManager(), "theme 4");
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AdminDownloadHistory.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) AdminDownloadHistory.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                    return;
                }

                loadingDialog.startLoadingDialog();
                loadingDialog.setText("downloading...");

                startDate = mStartDate.getText().toString().trim();
                endDate = mEndDate.getText().toString().trim();

                if(startDate.equals("")||endDate.equals(""))
                {
                    loadingDialog.dismissDialog();
                    Toast.makeText(AdminDownloadHistory.this, "please select dates please", Toast.LENGTH_SHORT).show();
                }
                else {

                    Query query = historyAdmin.orderByChild("date").startAt(startDate).endAt(endDate);

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    history = ds.getValue(historyAdminUploadFinal.class);
                                    list.add(history);
                                }
                                downloadHistory();

                            }
                            else {
                                loadingDialog.dismissDialog();
                                Toast.makeText(AdminDownloadHistory.this, "No data saved on these days.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            loadingDialog.dismissDialog();
                        }
                    });

                    //downloadHistory();
                }

            }
        });
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    private void downloadHistory() {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("History Admin"); //Creating a sheet


        PrintSetup ps = sheet.getPrintSetup();
        sheet.setAutobreaks(true);

        ps.setFitHeight((short)1);
        ps.setFitWidth((short)1);

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
        cellTitle.setCellValue("Admin Points History Screen");
        cellTitle.setCellStyle(headerCellStyle1);
        rowTitle.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());

        sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));

        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());


        for(int  i=0; i<columns.length; i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        CellStyle normal = workbook.createCellStyle();
        normal.setAlignment(HorizontalAlignment.CENTER);
        normal.setVerticalAlignment(VerticalAlignment.CENTER);


        int rowNum = 2;
        int temp = 1;
        for(historyAdminUploadFinal his: list){
            Row row = sheet.createRow(rowNum++);
            row.setHeightInPoints(20);
            Cell cell0 = row.createCell(0);
            Cell cell1 = row.createCell(1);
            Cell cell2 = row.createCell(2);
            Cell cell3 = row.createCell(3);
            Cell cell4 = row.createCell(4);
            Cell cell5 = row.createCell(5);
            Cell cell6 = row.createCell(6);
            Cell cell7 = row.createCell(7);
            Cell cell8 = row.createCell(8);

            cell0.setCellStyle(normal);
            cell1.setCellStyle(normal);
            cell2.setCellStyle(normal);
            cell3.setCellStyle(normal);
            cell4.setCellStyle(normal);
            cell5.setCellStyle(normal);
            cell6.setCellStyle(normal);
            cell7.setCellStyle(normal);
            cell8.setCellStyle(normal);

            cell0.setCellValue(temp++);
            cell1.setCellValue(his.date);
            cell2.setCellValue(his.name);
            cell3.setCellValue(his.shopName);
            cell4.setCellValue(his.loginId);
            cell5.setCellValue(his.transactionId);
            cell6.setCellValue(his.sentPoints);
            cell7.setCellValue(his.diductPoints);
            cell8.setCellValue(his.remainPointsToUser);
        }
        sheet.setColumnWidth(0,(8*100));
        sheet.setColumnWidth(1,(15 * 250));
        sheet.setColumnWidth(2,(20 * 250));
        sheet.setColumnWidth(3,(40 * 250));
        sheet.setColumnWidth(4,(20 * 250));
        sheet.setColumnWidth(5,(20 * 250));
        sheet.setColumnWidth(6,(20 * 250));
        sheet.setColumnWidth(7,(20 * 250));
        sheet.setColumnWidth(8,(55 * 250));

        timeStampTime = new SimpleDateFormat("HHmmss").format(new Date());

        String name = "admin"+timeStampTime+"History";
        String fileName = name+".xlsx"; //Name of the file

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
            //Toast.makeText(AdminDownloadHistory.this, "Excel file saved in your device....", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        String pdfName = name+".pdf";
        File pdfFile = new File(folder,pdfName);
        try {
            output = new FileOutputStream(pdfFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Document document = new Document(PageSize.A4.rotate());
        PdfPTable table = new PdfPTable(new float[]{1,3,3,4,3,3,3,3,3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.rotate().getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Sr.");
        table.addCell("Date");
        table.addCell("Dealer/Vendor Name");
        table.addCell( "Shop Name");
        table.addCell( "V/D Login Id");
        table.addCell("Transaction Id");
        table.addCell("Sent Point");
        table.addCell("Deduct Point");
        table.addCell("Available points of ven-deal after sending-deducting");
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();
        for(int i = 0; i<cells.length; i++)
        {
            cells[i].setBackgroundColor(BaseColor.GRAY);
        }
        int temp2 = 1;
        for(historyAdminUploadFinal his2: list) {
            table.addCell(String.valueOf(temp2++));
            table.addCell(his2.date);
            table.addCell(his2.name);
            table.addCell(his2.shopName);
            table.addCell(his2.loginId);
            table.addCell(his2.transactionId);
            table.addCell(his2.sentPoints);
            table.addCell(his2.diductPoints);
            table.addCell(his2.remainPointsToUser);
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
        PdfPCell cell1 = new PdfPCell(new Phrase("Admin Points History Screen",f));
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


        try {
            loadingDialog.dismissDialog();
            startActivity(new Intent(AdminDownloadHistory.this, MainActivityAdmin.class));
            finish();
        }
        catch (Exception e)
        {
            loadingDialog.dismissDialog();
        }

    }
//////////////////////////////////////////////////////////////////////////////////////////////////
    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public static String id = null;
        String date;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //these three lines are used to for cancle set future dates
            calendar.add(Calendar.DATE, 0);
            Date newDate = calendar.getTime();
            datePickerDialog.getDatePicker().setMaxDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));
            //here it ends

            return datePickerDialog;
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int month2 = month + 1;
            String formattedMonth = "" + month2;
            String formattedDayOfMonth = "" + day;

            if(month2 < 10){
                formattedMonth = "0" + month2;
            }
            if(day < 10){
                formattedDayOfMonth = "0" + day;
            }
            if(id.equals("start")){
                TextView textView = getActivity().findViewById(R.id.tv_startDate);
                textView.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
                date = textView.getText().toString().trim();
            }
            else if(id.equals("end")) {
                TextView textView2 = getActivity().findViewById(R.id.tv_endDate);
                textView2.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
                date = textView2.getText().toString().trim();
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminDownloadHistory.this, AdminSendPointsOptions.class));
        finish();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
}
