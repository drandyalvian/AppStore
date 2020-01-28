package com.example.company.appstore.Owner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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

import com.example.company.appstore.KepalaCabang.InputLaporanUangAct;
import com.example.company.appstore.KepalaCabang.LaporanUangAct;
import com.example.company.appstore.KepalaCabang.LaporanUangConst;
import com.example.company.appstore.KepalaCabang.ListAbsensiConst;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class InputLaporanOwner extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView xtgl;
    private Button addtgl,btnsave, back;
    EditText xnominal;

    DatabaseReference reference, reference2, reference3, reference4, reference5,  reference6, reference7, reference10;
    String USERNAME_KEY = "usernamekey";
    String username_key ="";
    String username_key_new ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_laporan_owner);

        final String cabang = getIntent().getStringExtra("cabang");

        getUsernameLocal();


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
                .child(cabang).child("LaporanUang");

        reference2 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("CountAbsen").child(dateString);

        reference3 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("CekLaporan").child(dateString);

        reference4 =  FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("Karyawan");

        reference10 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("CountKomisi");





        addtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });


//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
//
//                List<LaporanUangConst> uangs = new ArrayList<>();
//                while (iterator.hasNext()) {
//                    DataSnapshot dataSnapshotChild = iterator.next();
//                    LaporanUangConst lapUang = dataSnapshotChild.getValue(LaporanUangConst.class);
//                    uangs.add(lapUang);
//                }
//
//                int lengthuang = (int) dataSnapshot.getChildrenCount();
//
//                int hasil = 0;
//                for (int u = 0 ; u < lengthuang; u++){
//
//                    hasil +=  Integer.valueOf(uangs.get(u).getNominal());
////                            Log.d(String.valueOf(u), String.valueOf(uangs.get(u).getNominal()));
//                    Log.d("Hasil", String.valueOf(hasil));
//                }
//                hasilKomisi(hasil);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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

                                    }else if (count == 9 || count == 10){
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
                                    }else if (count > 10){
                                        Toast.makeText(InputLaporanOwner.this,
                                                "tidak masuk dalam rumus, Jumlah karyawan "+count+", maksimal 10 orang", Toast.LENGTH_SHORT).show();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });


                            Intent go = new Intent(InputLaporanOwner.this, LaporanOwnerAct.class);
                            go.putExtra("cabang",cabang);
                            startActivity(go);

                        }else{
                            Toast.makeText(InputLaporanOwner.this,
                                    "Jumlah karyawan yang hadir "+count+", minimal 4 orang", Toast.LENGTH_LONG).show();
                        }


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //komisi perhari
                                reference4.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                        List<DataKaryawanConst> users = new ArrayList<>();
                                        while (iterator.hasNext()) {
                                            DataSnapshot dataSnapshotChild = iterator.next();
                                            DataKaryawanConst name = dataSnapshotChild.getValue(DataKaryawanConst.class);
                                            users.add(name);
                                        }

                                        int lengthUser = (int) dataSnapshot.getChildrenCount();

                                        try{

                                            for (int u = 0 ; u < lengthUser; u++){

                                                reference5 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                        .child(cabang).child("CountKomisi").child(users.get(u).getKey_name());

                                                Query query1 = reference5.child("Absensi").orderByKey().limitToLast(1);
                                                int finalU = u;
                                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                                                            String keyTanggal = child.getKey();

                                                            DatabaseReference reference7 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                                    .child(cabang).child("CountKomisi").child(users.get(finalU).getKey_name());
                                                            reference7.child("Absensi").child(keyTanggal).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                                                    List<ListAbsensiConst> absens = new ArrayList<>();
                                                                    while (iterator.hasNext()) {
                                                                        DataSnapshot dataSnapshotChild = iterator.next();
                                                                        ListAbsensiConst keyku = dataSnapshotChild.getValue(ListAbsensiConst.class);
                                                                        absens.add(keyku);
                                                                    }

                                                                    int lengtAbsen = (int) dataSnapshot.getChildrenCount();

                                                                    for (int i = 0; i < lengtAbsen; i++){

//                                            Log.d("Coba",String.valueOf(absens.get(i).getKeterangan()));
                                                                        if (absens.get(i).getKeterangan().equals("Hadir")){

                                                                            reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                                                                    List<LaporanUangConst> lapUang = new ArrayList<>();
                                                                                    while (iterator.hasNext()) {
                                                                                        DataSnapshot dataSnapshotChild = iterator.next();
                                                                                        LaporanUangConst keyku = dataSnapshotChild.getValue(LaporanUangConst.class);
                                                                                        lapUang.add(keyku);
                                                                                    }

                                                                                    int lengthLapUang = (int) dataSnapshot.getChildrenCount();
                                                                                    for (int l = 0; l < lengthLapUang; l++){

                                                                                        int nominal = Integer.parseInt(lapUang.get(l).getNominal());
                                                                                        int hasilNominal = nominal*1;

                                                                                        DatabaseReference reference8 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                                                                .child(cabang).child("CountKomisi").child(users.get(finalU).getKey_name());
                                                                                        Query query2 = reference8.child("KomisiPerhari").child(keyTanggal);
                                                                                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(hasilNominal));
                                                                                                dataSnapshot.getRef().child("key").setValue(users.get(finalU).getKey_name());

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

                                                                        }else{

                                                                            reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                                                                    List<LaporanUangConst> lapUang = new ArrayList<>();
                                                                                    while (iterator.hasNext()) {
                                                                                        DataSnapshot dataSnapshotChild = iterator.next();
                                                                                        LaporanUangConst key = dataSnapshotChild.getValue(LaporanUangConst.class);
                                                                                        lapUang.add(key);
                                                                                    }

                                                                                    int lengthLapUang = (int) dataSnapshot.getChildrenCount();
                                                                                    for (int l = 0; l < lengthLapUang; l++){

                                                                                        int nominal = Integer.parseInt(lapUang.get(l).getNominal());
                                                                                        int hasilNominal = nominal*0;
                                                                                        DatabaseReference reference8 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                                                                .child(cabang).child("CountKomisi").child(users.get(finalU).getKey_name());
                                                                                        Query query2 = reference8.child("KomisiPerhari").child(keyTanggal);
                                                                                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(hasilNominal));
                                                                                                dataSnapshot.getRef().child("key").setValue(users.get(finalU).getKey_name());

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

                                                                        }

                                                                    }


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

                                            }

                                        }catch (Exception e){


                                        }



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {



                                    }
                                });

                                //total Komisi
                                reference4.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                        List<DataKaryawanConst> users = new ArrayList<>();
                                        while (iterator.hasNext()) {
                                            DataSnapshot dataSnapshotChild = iterator.next();
                                            DataKaryawanConst name = dataSnapshotChild.getValue(DataKaryawanConst.class);
                                            users.add(name);
                                        }

                                        int lengthUser = (int) dataSnapshot.getChildrenCount();

                                        try {

                                            for (int i = 0 ; i < lengthUser ; i++){

                                                DatabaseReference reference9 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                        .child(cabang).child("CountKomisi").child(users.get(i).getKey_name());



                                                reference9.child("KomisiPerhari").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                                        List<LaporanUangConst> komisis = new ArrayList<>();
                                                        while (iterator.hasNext()) {
                                                            DataSnapshot dataSnapshotChild = iterator.next();
                                                            LaporanUangConst nkomisi = dataSnapshotChild.getValue(LaporanUangConst.class);
                                                            komisis.add(nkomisi);
                                                        }

                                                        int lengthuang = (int) dataSnapshot.getChildrenCount();

                                                        int hasil = 0;

                                                        try {

                                                            for (int u = 0 ; u < lengthuang; u++){

                                                                hasil +=  Integer.valueOf(komisis.get(u).getNominal());
                                                                Log.d("Hasil", String.valueOf(hasil));
                                                                reference10 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                                        .child(cabang).child("CountKomisi").child(komisis.get(u).getKey())
                                                                        .child("TotalKomisi");

                                                                reference7 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                                        .child(cabang).child("Karyawan").child(komisis.get(u).getKey());
                                                            }

                                                        }catch (Exception e){

                                                        }

                                                        int finalHasil = hasil;
                                                        try {

                                                            reference10.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    dataSnapshot.getRef().child("total_komisi").setValue(String.valueOf(finalHasil));

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                            reference7.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    dataSnapshot.getRef().child("kompensasi").setValue(String.valueOf(finalHasil));
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }catch (Exception e){

                                                        }



                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                        }catch (Exception e){

                                        }



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }, 1000);

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
                Intent go = new Intent(InputLaporanOwner.this, LaporanOwnerAct.class);
                go.putExtra("cabang",cabang);
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

//    public void hasilKomisi( int hasil){
//        reference4.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
//
//                List<DataKaryawanConst> users = new ArrayList<>();
//                while (iterator.hasNext()) {
//                    DataSnapshot dataSnapshotChild = iterator.next();
//                    DataKaryawanConst name = dataSnapshotChild.getValue(DataKaryawanConst.class);
//                    users.add(name);
//                }
//
//                int lengthUser = (int) dataSnapshot.getChildrenCount();
//
//                for (int u = 0 ; u < lengthUser; u++){
//
//                    reference10.child(String.valueOf(users.get(u).getKey_name())).child("TotalKomisi").
//                            addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                dataSnapshot.getRef().child("total_komisi").setValue(String.valueOf(hasil));
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//
//
//            }
//        });
//    }
}
