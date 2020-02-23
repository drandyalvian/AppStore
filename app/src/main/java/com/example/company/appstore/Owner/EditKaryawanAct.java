package com.example.company.appstore.Owner;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class EditKaryawanAct extends AppCompatActivity {

    Button back, save;
    Spinner xspiner, xposisi;
    TextView xusername, xcabangtoko;
    EditText xnama, xumur, xalamat, xnohp, xgajipokok;
    String cabangx, karyawanx;

    DatabaseReference reference;

//    @Override
//    public void onBackPressed() {
//        Intent go = new Intent(EditKaryawanAct.this,DataKaryawanAct.class);
//        go.putExtra("cabang", cabangx);
//        startActivity(go);
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_karyawan);

        back = findViewById(R.id.back);
        save = findViewById(R.id.btnsave);
        xnama = findViewById(R.id.xnama);
        xusername = findViewById(R.id.xusername);
        xcabangtoko = findViewById(R.id.xcabangtoko);
        xumur = findViewById(R.id.xumur);
        xalamat = findViewById(R.id.xalamat);
        xnohp = findViewById(R.id.xnohp);
        xgajipokok = findViewById(R.id.xgajipokok);
        xspiner = findViewById(R.id.xspiner);
        xposisi = findViewById(R.id.xposisi);

        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

//        spinner
        final ArrayAdapter pilihGender=ArrayAdapter.createFromResource(this, R.array.pilih_gender, android.R.layout.simple_spinner_dropdown_item);
        xspiner.setAdapter(pilihGender);

//        final ArrayAdapter cabangToko=ArrayAdapter.createFromResource(this, R.array.cabangToko, android.R.layout.simple_spinner_dropdown_item);
//        xcabangtoko.setAdapter(cabangToko);
        final ArrayAdapter pilihPosisi = ArrayAdapter.createFromResource(EditKaryawanAct.this, R.array.posisiSpinner, android.R.layout.simple_spinner_dropdown_item);
        xposisi.setAdapter(pilihPosisi);



//mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        cabangx= bundle.getString("keyCabang");
        karyawanx= bundle.getString("key");


        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangx).child("Karyawan").child(karyawanx);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayAdapter pilihPosisi = ArrayAdapter.createFromResource(EditKaryawanAct.this, R.array.posisiSpinner, android.R.layout.simple_spinner_dropdown_item);
                xnama.setText(dataSnapshot.child("nama").getValue().toString());
                xposisi.setSelection(pilihPosisi.getPosition(dataSnapshot.child("posisi").getValue().toString()));
                xcabangtoko.setText(dataSnapshot.child("nama_cabang").getValue().toString());
                xumur.setText(dataSnapshot.child("umur").getValue().toString());
                xalamat.setText(dataSnapshot.child("alamat").getValue().toString());
                xnohp.setText(dataSnapshot.child("telepon").getValue().toString());
                xgajipokok.setText(dataSnapshot.child("gaji_pokok").getValue().toString());
                xspiner.setSelection(pilihGender.getPosition(dataSnapshot.child("gender").getValue().toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //save data
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final ArrayAdapter pilihPosisi = ArrayAdapter.createFromResource(EditKaryawanAct.this, R.array.posisiSpinner, android.R.layout.simple_spinner_dropdown_item);
                        dataSnapshot.getRef().child("nama").setValue(xnama.getText().toString());
                        dataSnapshot.getRef().child("key").setValue(xnama.getText().toString());
                        dataSnapshot.getRef().child("key_name").setValue(xnama.getText().toString());
                        dataSnapshot.getRef().child("posisi").setValue(xposisi.getSelectedItem().toString());
                        dataSnapshot.getRef().child("nama_cabang").setValue(xcabangtoko.getText().toString());
                        dataSnapshot.getRef().child("umur").setValue(xumur.getText().toString());
                        dataSnapshot.getRef().child("alamat").setValue(xalamat.getText().toString());
                        dataSnapshot.getRef().child("telepon").setValue(xnohp.getText().toString());
                        dataSnapshot.getRef().child("gaji_pokok").setValue(xgajipokok.getText().toString());
                        dataSnapshot.getRef().child("gender").setValue(xspiner.getSelectedItem().toString());
//                        try {
//
//                        }catch (Exception e){
//                            Toast.makeText(EditKaryawanAct.this, ""+reference.getRef(), Toast.LENGTH_SHORT).show();
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(EditKaryawanAct.this, ""+reference.getRef(), Toast.LENGTH_SHORT).show();
                    }


                });

                Intent go = new Intent(EditKaryawanAct.this,DataKaryawanAct.class);
                go.putExtra("cabang", cabangx);
                startActivity(go);
//                finish();
            }




        });

//        pindah activity

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(EditKaryawanAct.this,DataKaryawanAct.class);
                go.putExtra("cabang", cabangx);
                startActivity(go);
//                finish();

            }
        });




    }

}
