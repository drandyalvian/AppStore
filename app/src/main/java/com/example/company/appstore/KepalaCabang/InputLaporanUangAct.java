package com.example.company.appstore.KepalaCabang;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InputLaporanUangAct extends AppCompatActivity  {


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView xtgl;
    private Button addtgl,btnsave, back;
    EditText xnominal;

    DatabaseReference reference, reference2, reference3;
    String USERNAME_KEY = "usernamekey";
    String username_key ="";
    String username_key_new ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_laporan_uang);

        getUsernameLocal();

//        dateFormatter = new SimpleDateFormat("dd MMMM yyyy");

        xtgl = findViewById(R.id.xtgl);
        addtgl = findViewById(R.id.addtgl);
        xnominal = findViewById(R.id.xnominal);
        btnsave = findViewById(R.id.btnsave);
        back = findViewById(R.id.back);

        xtgl.addTextChangedListener(loginTextWatcher);
        xnominal.addTextChangedListener(loginTextWatcher);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        String dateString = sdf.format(date);
        xtgl.setText(dateString);


        reference = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(username_key_new).child("LaporanUang");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(username_key_new).child("CountAbsen").child(dateString);
        reference3 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(username_key_new).child("CekLaporan").child(dateString);


        addtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        Query query = reference2.orderByChild("keterangan").equalTo("Hadir");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();

                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
////                        Log.d("count", s);

                        if (count > 3){
                            int jml = Integer.parseInt(xnominal.getText().toString());


                            reference.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child("tanggal").setValue(xtgl.getText().toString());
                                    dataSnapshot.getRef().child("filter").setValue(xtgl.getText().toString().substring(3));
                                    dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
//                            dataSnapshot.getRef().child("nominal").setValue(xnominal.getText().toString());
                                    if (count == 4){
                                        double jumlah = jml*30 * 0.3;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));

                                        reference3.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }else if (count == 5){
                                        double jumlah = jml*29 * 0.24;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }else if (count == 6){
                                        double jumlah = jml*28 * 0.22;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }else if (count == 7){
                                        double jumlah = jml*27 * 0.2;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }else if (count == 8){
                                        double jumlah = jml*26 * 0.18;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }else if (count == 9){
                                        double jumlah = jml*25 * 0.17;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });

                            Intent go = new Intent(InputLaporanUangAct.this,LaporanUangAct.class);
                            startActivity(go);
                            finish();

                        }else{
                            Toast.makeText(InputLaporanUangAct.this,
                                    "Jumlah karyawan yang hadir "+count+", minimal 4 orang", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(InputLaporanUangAct.this, LaporanUangAct.class);
                startActivity(go);
            }
        });


    }

    private void showDateDialog(){


        Calendar newCalendar = Calendar.getInstance();

//        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

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

//mengambil data local
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new =sharedPreferences.getString(username_key, "");

    }


    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String tglInput = xtgl.getText().toString().trim();
            String nominalInput = xnominal.getText().toString().trim();

            btnsave.setEnabled(!tglInput.isEmpty() && !nominalInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
