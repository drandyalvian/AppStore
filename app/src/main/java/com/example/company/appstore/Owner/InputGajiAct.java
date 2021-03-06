package com.example.company.appstore.Owner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class InputGajiAct extends AppCompatActivity {

    DatabaseReference reference;
    String cabangx, karyawanx;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;


    TextView xnama, xposisi, xtgl;
    EditText xgajipokok,xgajilembur, xAngsuranPasti, xuangmakan, xpinjaman, xKomisi;
    Button btnsave, back, addtgl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_gaji);

        xnama = findViewById(R.id.xnama);
        xtgl = findViewById(R.id.xtgl);
        xposisi = findViewById(R.id.xposisi);
        xgajipokok = findViewById(R.id.xgajipokok);
        xgajilembur = findViewById(R.id.xgajilembur);
        xAngsuranPasti = findViewById(R.id.x_angsuran_pasti);
        xuangmakan = findViewById(R.id.xuangmakan);
        xpinjaman = findViewById(R.id.xpinjaman);
        xKomisi = findViewById(R.id.xKomisi);
        btnsave = findViewById(R.id.btnsave);
        back = findViewById(R.id.back);

        dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);


        Bundle bundle = getIntent().getExtras();
        cabangx= bundle.getString("cabang");
        karyawanx= bundle.getString("key");

//        Get
        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangx).child("Karyawan").child(karyawanx);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xnama.setText(dataSnapshot.child("nama").getValue().toString());
                xposisi.setText(dataSnapshot.child("posisi").getValue().toString());
                xgajipokok.setText(dataSnapshot.child("gaji_pokok").getValue().toString());
                xgajilembur.setText(dataSnapshot.child("gaji_lembur").getValue().toString());
                xAngsuranPasti.setText(dataSnapshot.child("angsuran_pasti").getValue().toString());
                xuangmakan.setText(dataSnapshot.child("uang_makan").getValue().toString());
                xpinjaman.setText(dataSnapshot.child("pinjaman").getValue().toString());
                xKomisi.setText(dataSnapshot.child("kompensasi").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //update data
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                Date date = new Date();
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+7"));

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("nama").setValue(xnama.getText().toString());
                        dataSnapshot.getRef().child("posisi").setValue(xposisi.getText().toString());
                        dataSnapshot.getRef().child("gaji_pokok").setValue(xgajipokok.getText().toString());
                        dataSnapshot.getRef().child("gaji_lembur").setValue(xgajilembur.getText().toString());
                        dataSnapshot.getRef().child("angsuran_pasti").setValue(xAngsuranPasti.getText().toString());
                        dataSnapshot.getRef().child("uang_makan").setValue(xuangmakan.getText().toString());
                        dataSnapshot.getRef().child("pinjaman").setValue(xpinjaman.getText().toString());
                        dataSnapshot.getRef().child("kompensasi").setValue(xKomisi.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent go = new Intent(InputGajiAct.this,GajiAct.class);
                go.putExtra("cabang",cabangx);
                startActivity(go);
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(InputGajiAct.this,GajiAct.class);
                go.putExtra("cabang","cabang1");
                startActivity(go);

            }
        });

    }

    private void showDateDialog(){


        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                long date = System.currentTimeMillis();
                xtgl.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
