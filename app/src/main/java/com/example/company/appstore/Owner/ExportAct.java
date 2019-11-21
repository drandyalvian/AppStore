package com.example.company.appstore.Owner;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.company.appstore.FileUtils;
import com.example.company.appstore.R;
import com.example.company.appstore.permission.PermissionsActivity;
import com.example.company.appstore.permission.PermissionsChecker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.company.appstore.LogUtils.LOGE;
import static com.example.company.appstore.permission.PermissionsActivity.PERMISSION_REQUEST_CODE;
import static com.example.company.appstore.permission.PermissionsChecker.REQUIRED_PERMISSION;

public class ExportAct extends AppCompatActivity {

    Context mContext;

    PermissionsChecker checker;

    DatabaseReference reference;

    String nama = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        mContext = getApplicationContext();

        checker = new PermissionsChecker(this);

        Bundle bundle = getIntent().getExtras();
        String cabangx= bundle.getString("cabang");
        String key = bundle.getString("key");


        Intent go = new Intent(this, GajiAct.class);
        go.putExtra("cabang", cabangx);
        startActivity(go);
        finish();

        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangx).child("Karyawan").child(key);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama = dataSnapshot.child("nama").getValue().toString();
                //createPdf(FileUtils.getAppPath(mContext) + dataSnapshot.child("nama").getValue().toString() +".pdf");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
            PermissionsActivity.startActivityForResult(ExportAct.this, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
        } else {
            //createPdf(FileUtils.getAppPath(mContext) + "123.pdf");
        }


    }

    public void createPdf(String dest, String nama, String komisi, String gajiPokok, String pinjaman, String uangMakan, Integer gajiTotal, Integer gajiDiterima, String namaCabang) {

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {
            /**
             * Creating Document
             */
            Document document = new Document();

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A8);
            document.addCreationDate();
            document.addAuthor("Wonogiri Developer");
            document.addCreator("Wonogiri.Dev");

            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;

            /**
             * How to USE FONT....
             */
            BaseFont urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            // Title Order Details...
            // Adding Title....
            Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk("Toko Andhika ", mOrderDetailsTitleFont);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);

            Font mOrderDetailsTitleFont2 = new Font(urName, 29.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk2 = new Chunk(namaCabang+"", mOrderDetailsTitleFont2);
            Paragraph mOrderDetailsTitleParagraph2 = new Paragraph(mOrderDetailsTitleChunk2);
            mOrderDetailsTitleParagraph2.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph2);

            document.add(new Paragraph(""));

            // Fields of Order Details...
            // Adding Chunks for Title and value
            Font mOrderIdFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderIdChunk = new Chunk("Nama : " +nama, mOrderIdFont);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(new Chunk(lineSeparator));
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            Font mOrderIdValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderIdValueChunk = new Chunk("Gaji Pokok", mOrderIdValueFont);
            Paragraph mOrderIdValueParagraph = new Paragraph(mOrderIdValueChunk);
            document.add(mOrderIdValueParagraph);

            Chunk glue = new Chunk(new VerticalPositionMark());
            Chunk mOrderIdValueChunk2 = new Chunk("25x20.000", mOrderIdValueFont);
            Paragraph mOrderIdValueParagraph2 = new Paragraph(mOrderIdValueChunk2);
            mOrderIdValueParagraph2.add(new Chunk(glue));
            Chunk mOrderIdValueChunk1 = new Chunk(gajiPokok+"", mOrderIdValueFont);
            mOrderIdValueParagraph2.add(mOrderIdValueChunk1);
            document.add(mOrderIdValueParagraph2);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(new Chunk(lineSeparator));
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Font mOrderDateValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateChunk = new Chunk("Komisi", mOrderDateValueFont);
            Paragraph mOrderDateParagraph = new Paragraph(mOrderDateChunk);
            document.add(mOrderDateParagraph);


            Chunk mOrderDateValueChunk = new Chunk(komisi+"", mOrderDateValueFont);
            Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
            mOrderDateValueParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderDateValueParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Font mOrderAcNameValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderAcNameChunk = new Chunk("Uang Makan:", mOrderAcNameValueFont);
            Paragraph mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            document.add(mOrderAcNameParagraph);


            Chunk mUangMakan = new Chunk("25x5.000", mOrderIdValueFont);
            Paragraph mUangMakanParagrap = new Paragraph(mUangMakan);
            mUangMakanParagrap.add(new Chunk(glue));
            Chunk mUangMakanTotal = new Chunk(uangMakan+"", mOrderIdValueFont);
            mUangMakanParagrap.add(mUangMakanTotal);
            document.add(mUangMakanParagrap);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Chunk mGajiTotal = new Chunk("Gaji Total :", mOrderIdValueFont);
            Paragraph mGajiTotalParagrap = new Paragraph(mGajiTotal);
            mGajiTotalParagrap.add(new Chunk(glue));
            Chunk mGajiTotalNominal = new Chunk(gajiTotal+"", mOrderIdValueFont);
            mGajiTotalParagrap.add(mGajiTotalNominal);
            document.add(mGajiTotalParagrap);

            Chunk mPeminjaman= new Chunk("Peminjaman :", mOrderIdValueFont);
            Paragraph mPeminjamanParagrap = new Paragraph(mPeminjaman);
            mPeminjamanParagrap.add(new Chunk(glue));
            Chunk mPeminjamanNominal = new Chunk(pinjaman+"", mOrderIdValueFont);
            mPeminjamanParagrap.add(mPeminjamanNominal);
            document.add(mPeminjamanParagrap);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Chunk mGajiDiterimaLabel= new Chunk("Gaji Diterima:", mOrderIdValueFont);
            Paragraph mGajiDiterimaParagrap = new Paragraph(mGajiDiterimaLabel);
            mGajiDiterimaParagrap .add(new Chunk(glue));
            Chunk mGajiDiterimaNominal = new Chunk(gajiDiterima+"", mOrderIdValueFont);
            mGajiDiterimaParagrap.add(mGajiDiterimaNominal);
            document.add(mGajiDiterimaParagrap);

            document.close();

            //Toast.makeText(, "Created... :)", Toast.LENGTH_SHORT).show();
//            if (new File(dest).exists()) {
//                FileUtils.openFile(mContext, new File(dest));
//            }




        } catch (IOException | DocumentException ie) {
            LOGE("createPdf: Error " + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            Toast.makeText(mContext, "Permission Granted to Save", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Permission not granted, Try again!", Toast.LENGTH_SHORT).show();
        }
    }

}
