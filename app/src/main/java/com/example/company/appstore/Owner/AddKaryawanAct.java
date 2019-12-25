package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.KepalaCabang.ListAbsensiConst;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class AddKaryawanAct extends AppCompatActivity {

    Button back, save;
    Spinner xspiner, xcabangtoko;
    EditText  xnama, xumur, xalamat, xnohp, xgajipokok, xusername, xposisi;
    String cabangx, karyawanx, cabangToko, key;

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

        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

//        spinner
        final ArrayAdapter pilihGender = ArrayAdapter.createFromResource(this, R.array.pilih_gender, android.R.layout.simple_spinner_dropdown_item);
        xspiner.setAdapter(pilihGender);

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
                final String[] keyKaryawan = new String[1];

                if (cabangx.equals("cabang1")){
                    cabangToko = "Toko Cabang 1";
                }else if (cabangx.equals("cabang2")){
                    cabangToko = "Toko Cabang 2";
                }else{
                    cabangToko = "Toko Cabang 3";
                }


                GajiConst gajiConst = new GajiConst(
                        xnama.getText().toString(),
                        xnama.getText().toString(),
                        xposisi.getText().toString(),
                        xalamat.getText().toString(),
                        xspiner.getSelectedItem().toString(),
                        cabangToko,
                        cabangx,
                        xumur.getText().toString(),
                        xnohp.getText().toString(),
                        keyKaryawan[0],
                        "0",
                        xgajipokok.getText().toString(),
                        "0",
                        "0",
                        "0",
                        "0");


                DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                Date date = new Date();
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+7"));

                ListAbsensiConst absensiConst = new ListAbsensiConst(
                        "Alpha",
                        dateFormat.format(date),
                        dateFormat.format(date),
                        dateFormat.format(date).substring(3)

                );

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

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if (dataSnapshot.hasChild(arrOfStr[0])) {
                                DateFormat dateFormat2 = new SimpleDateFormat("yyyymmddmmss");
                                Date date2 = new Date();
                                dateFormat2.setTimeZone(TimeZone.getTimeZone("UTC+7"));
                                keyKaryawan[0] = (dateFormat2.format(date2)).toString();
                                reference.child(keyKaryawan[0]).setValue(gajiConst);
                                reference.child(arrOfStr[0]).child("Absensi").child(dateFormat.format(date)).setValue(absensiConst);
                                Toast.makeText(AddKaryawanAct.this, "sudah ada", Toast.LENGTH_SHORT).show();

                            }else {
                                reference.child(arrOfStr[0]).setValue(gajiConst);
                                reference.child(arrOfStr[0]).child("Absensi").child(dateFormat.format(date)).setValue(absensiConst);
                                Toast.makeText(AddKaryawanAct.this, "belum ada", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });

                    Intent go = new Intent(AddKaryawanAct.this,DataKaryawanAct.class);
                    go.putExtra("cabang", cabangx);
                    startActivity(go);


                }

            }

        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(AddKaryawanAct.this, DataKaryawanAct.class);
                go.putExtra("cabang", cabangx);
                startActivity(go);
            }
        });

//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                dataSnapshot.getRef().child("key").setValue(xnama.getText().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }
}
