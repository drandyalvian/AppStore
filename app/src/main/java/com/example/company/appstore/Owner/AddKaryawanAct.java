package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddKaryawanAct extends AppCompatActivity {

    Button back, save;
    Spinner xspiner, xcabangtoko;
    EditText  xnama, xumur, xalamat, xnohp, xgajipokok, xusername, xposisi;
    String cabangx, karyawanx, cabangToko;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_karyawan);


        back = findViewById(R.id.back);
        save = findViewById(R.id.btnsave);
//        xcabangtoko = findViewById(R.id.xcabangtoko);
        xnama = findViewById(R.id.xnama);
        xumur = findViewById(R.id.xumur);
        xusername = findViewById(R.id.xusername);
        xalamat = findViewById(R.id.xalamat);
        xnohp = findViewById(R.id.xnohp);
        xgajipokok = findViewById(R.id.xgajipokok);
        xspiner = findViewById(R.id.xspiner);
        xposisi = findViewById(R.id.xposisi);

//        spinner
        final ArrayAdapter pilihGender = ArrayAdapter.createFromResource(this, R.array.pilih_gender, android.R.layout.simple_spinner_dropdown_item);
        xspiner.setAdapter(pilihGender);

//        final ArrayAdapter pilihCabang = ArrayAdapter.createFromResource(this, R.array.cabangToko, android.R.layout.simple_spinner_dropdown_item);
//        xcabangtoko.setAdapter(pilihCabang);

//      mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        cabangx = bundle.getString("cabang");

        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangx).child("Karyawan");

        //add karyawan
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = xnama.getText().toString();
                String[] arrOfStr = str.split(" ");

                if (cabangx.equals("cabang1")){
                    cabangToko = "Toko Cabang 1";
                }else if (cabangx.equals("cabang2")){
                    cabangToko = "Toko Cabang 2";
                }else{
                    cabangToko = "Toko Cabang 3";
                }


                GajiConst gajiConst = new GajiConst(
                        xnama.getText().toString(),
                        xposisi.getText().toString(),
                        xalamat.getText().toString(),
                        xspiner.getSelectedItem().toString(),
                        cabangToko,
                        cabangx,
                        xumur.getText().toString(),
                        xnohp.getText().toString(),
                        arrOfStr[0],
                        "0",
                        xgajipokok.getText().toString(),
                        "0",
                        "0",
                        "0",
                        "0");

                if (xnama.length() == 0) {
                    xnama.setError("Masukan nama karyawan");
                    xnama.requestFocus();
                } else if (xposisi.length() == 0) {
                    xposisi.setError("Masukan posisi karyawan");
                    xposisi.requestFocus();
                } else if (xumur.length() == 0) {
                    xumur.setError("Masukan umur karyawan");
                    xumur.requestFocus();
                } else if (xalamat.length() == 0) {
                    xalamat.setError("Masukan alamat");
                    xalamat.requestFocus();
                } else if (xnohp.length() == 0) {
                    xnohp.requestFocus();
                    xnohp.setError("Masukan No.Hp");
                } else if (xgajipokok.length() == 0) {
                    xgajipokok.setError("Masukan gaji pokok");
                    xgajipokok.requestFocus();
                } else {

                    reference.child(arrOfStr[0]).setValue(gajiConst);

                    Intent go = new Intent(AddKaryawanAct.this, DataKaryawanAct.class);
                    go.putExtra("cabang", cabangx);
                    startActivity(go);
                    finish();
                }

            }

        });
    }
}
