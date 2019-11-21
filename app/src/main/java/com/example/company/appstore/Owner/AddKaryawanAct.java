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

public class AddKaryawanAct extends AppCompatActivity {

    Button back, save;
    Spinner xspiner;
    EditText  xnama, xumur, xalamat, xnohp, xgajipokok, xusername, xposisi;
    String cabangx, karyawanx;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_karyawan);


        back = findViewById(R.id.back);
        save = findViewById(R.id.btnsave);
        xnama = findViewById(R.id.xnama);
        xumur = findViewById(R.id.xumur);
        xusername = findViewById(R.id.xusername);
        xalamat = findViewById(R.id.xalamat);
        xnohp = findViewById(R.id.xnohp);
        xgajipokok = findViewById(R.id.xgajipokok);
        xspiner = findViewById(R.id.xspiner);
        xposisi = findViewById(R.id.xposisi);

//        spinner
        final ArrayAdapter pilihGender=ArrayAdapter.createFromResource(this, R.array.pilih_gender, android.R.layout.simple_spinner_dropdown_item);
        xspiner.setAdapter(pilihGender);

//      mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        cabangx= bundle.getString("cabang");

        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangx).child("Karyawan");

        //save data
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(xusername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {

                            dataSnapshot.getRef().child("nama").setValue(xnama.getText().toString());
                            dataSnapshot.getRef().child("posisi").setValue(xposisi.getText().toString());
                            dataSnapshot.getRef().child("key").setValue(xusername.getText().toString());
                            dataSnapshot.getRef().child("umur").setValue(xumur.getText().toString());
                            dataSnapshot.getRef().child("alamat").setValue(xalamat.getText().toString());
                            dataSnapshot.getRef().child("telepon").setValue(xnohp.getText().toString());
                            dataSnapshot.getRef().child("gaji_pokok").setValue(xgajipokok.getText().toString());
                            dataSnapshot.getRef().child("gender").setValue(xspiner.getSelectedItem().toString());
                            dataSnapshot.getRef().child("cabang").setValue(cabangx);
                        }catch (Exception e){
                            Toast.makeText(AddKaryawanAct.this, ""+reference.getRef(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddKaryawanAct.this, ""+reference.getRef(), Toast.LENGTH_SHORT).show();
                    }
                });
                Intent go = new Intent(AddKaryawanAct.this,DataKaryawanAct.class);
                go.putExtra("cabang", cabangx);
                startActivity(go);
                finish();

            }


        });
    }
}
