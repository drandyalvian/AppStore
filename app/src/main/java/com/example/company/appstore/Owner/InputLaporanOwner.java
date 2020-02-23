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

    DatabaseReference reference, reference2, reference3, reference4, reference5,  reference6,reference7, reference9, reference10, reference11;
    String USERNAME_KEY = "usernamekey";
    String username_key ="";
    String username_key_new ="";


    String cabangku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_laporan_owner);

        final String cabang = getIntent().getStringExtra("cabang");
        cabangku = cabang;


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
//        xtgl.setText(dateString);

        reference = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("LaporanUang");

        reference2 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("CountAbsen");

        reference3 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("CekLaporan");
        reference4 =  FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("Karyawan");
        reference9 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("Recap");
        reference10 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("CountKomisi");
        reference11 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("CountKaryawan");


        addtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
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

    public void saveAction(String tanggal){

        Query query = reference2.child(tanggal).orderByChild("keterangan").equalTo("Hadir");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();

                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
////                        Log.d("count", s);
                        btnsave.setText("PROSES...");

                        if (count > 3){
                            int jml = Integer.parseInt(xnominal.getText().toString());

                            reference.child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child("tanggal").setValue(xtgl.getText().toString());
                                    dataSnapshot.getRef().child("filter").setValue(xtgl.getText().toString().substring(3));
                                    dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
//                            dataSnapshot.getRef().child("nominal").setValue(xnominal.getText().toString());
                                    if (count == 4){
                                        double rounded = jml*30 * 0.3;
                                        double jumlah = (double) Math.round(rounded/100d)*100;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));

                                        reference3.child(tanggal).child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        double rounded = jml*29 * 0.24;
                                        double jumlah = (double) Math.round(rounded/100d)*100;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(tanggal).child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        double rounded = jml*28 * 0.22;
                                        double jumlah = (double) Math.round(rounded/100d)*100;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(tanggal).child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        double rounded = jml*27 * 0.2;
                                        double jumlah = (double) Math.round(rounded/100d)*100;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(tanggal).child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        double rounded = jml*26 * 0.18;
                                        double jumlah = (double) Math.round(rounded/100d)*100;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(tanggal).child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
                                                dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }else if (count == 9 || count ==10){
                                        double rounded = jml*25 * 0.17;
                                        double jumlah = (double) Math.round(rounded/100d)*100;
                                        DecimalFormat df = new DecimalFormat("###.#");
                                        dataSnapshot.getRef().child("nominal").setValue(String.valueOf(df.format(jumlah)));
                                        reference3.child(tanggal).child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
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

                            //komisi perhari
                            reference4.addListenerForSingleValueEvent(new ValueEventListener() {
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

                                            for (DataSnapshot child: dataSnapshot.getChildren()) {

                                                DatabaseReference reference7 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                        .child(cabangku).child("CountKomisi").child(users.get(u).getKey_name());
                                                int finalU = u;
                                                reference7.child("Absensi").child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
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

                                                                reference3.child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                                                    .child(cabangku).child("CountKomisi").child(users.get(finalU).getKey_name());
                                                                            Query query2 = reference8.child("KomisiPerhari").child(tanggal).child(tanggal);
                                                                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(hasilNominal));
                                                                                    dataSnapshot.getRef().child("key").setValue(users.get(finalU).getKey_name());

                                                                                    Log.d("nominal", String.valueOf(hasilNominal));

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                            //suportTotalKomisi
                                                                            reference8.child("TotalKomisi").child(tanggal).
                                                                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                            dataSnapshot.getRef().child("nominal").setValue(String.valueOf(hasilNominal));
                                                                                            dataSnapshot.getRef().child("key").setValue(users.get(finalU).getKey_name());

                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                        }
                                                                                    });

                                                                            //recap
                                                                            reference9.child(users.get(finalU).getKey_name()).child(tanggal.substring(3)).child(tanggal).
                                                                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                            dataSnapshot.getRef().child("nominal").setValue(String.valueOf(hasilNominal));

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

                                                                reference3.child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                                                    .child(cabangku).child("CountKomisi").child(users.get(finalU).getKey_name());
                                                                            Query query2 = reference8.child("KomisiPerhari").child(tanggal).child(tanggal);
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

                                                                            //suportTotalKomisi
                                                                            reference8.child("TotalKomisi").child(tanggal).
                                                                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                            dataSnapshot.getRef().child("nominal").setValue(String.valueOf(hasilNominal));
                                                                                            dataSnapshot.getRef().child("key").setValue(users.get(finalU).getKey_name());

                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                        }
                                                                                    });

                                                                            //recap
                                                                            reference9.child(users.get(finalU).getKey_name()).child(tanggal.substring(3)).child(tanggal).
                                                                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                            dataSnapshot.getRef().child("nominal").setValue(String.valueOf(hasilNominal));

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

                                    }catch (Exception e){


                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {



                                }
                            });

                            //komisiKepalaCabang dan komisiKaryawan
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //komisiKepalaCabang
                                    Query queryKep = reference4.orderByChild("posisi").equalTo("Kepala Cabang");
                                    queryKep.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                            List<DataKaryawanConst> users = new ArrayList<>();
                                            while (iterator.hasNext()) {
                                                DataSnapshot dataSnapshotChild = iterator.next();
                                                DataKaryawanConst name = dataSnapshotChild.getValue(DataKaryawanConst.class);
                                                users.add(name);
                                            }

                                            int lengthK = (int) dataSnapshot.getChildrenCount();

                                            for (int u = 0 ; u < lengthK ; u++){

                                                int finalU = u;
                                                Query queryKepala = reference10.child(users.get(u).getKey_name()).child("Absensi").child(tanggal).
                                                        orderByChild("keterangan").equalTo("Hadir");
                                                queryKepala.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        if (dataSnapshot.exists()){
                                                            int lengthKepala = (int) dataSnapshot.getChildrenCount();

                                                            Log.d("cek", String.valueOf(dataSnapshot.exists()));

                                                            reference10.child(users.get(finalU).getKey_name()).child("KomisiPerhari").child(tanggal)
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                                                            List<LaporanUangConst> lapUang = new ArrayList<>();
                                                                            while (iterator.hasNext()) {
                                                                                DataSnapshot dataSnapshotChild = iterator.next();
                                                                                LaporanUangConst keyku = dataSnapshotChild.getValue(LaporanUangConst.class);
                                                                                lapUang.add(keyku);
                                                                            }

                                                                            int lengthKomisi = (int) dataSnapshot.getChildrenCount();

                                                                            Log.d("lengthkey", String.valueOf(lengthKomisi));

                                                                            for (int i = 0 ; i < lengthKomisi ; i++){

                                                                                Log.d("nominalkuApa", lapUang.get(i).getNominal());
                                                                                int komisiTukang = Integer.parseInt(lapUang.get(i).getNominal());

                                                                                Query query = reference2.child(tanggal).orderByChild("keterangan").equalTo("Hadir");
                                                                                query.addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        long count = dataSnapshot.getChildrenCount();

                                                                                        Query queryKaryawan =  reference11.child(tanggal)
                                                                                                .orderByChild("keterangan").equalTo("Hadir");
                                                                                        queryKaryawan.addValueEventListener(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                int lengthKaryawan = (int) dataSnapshot.getChildrenCount();
                                                                                                Log.d("lengthKaryawan", String.valueOf(lengthKaryawan));

                                                                                                int lengthSisa = lengthKepala + lengthKaryawan;
                                                                                                int jmlLaporan = Integer.parseInt(xnominal.getText().toString());

                                                                                                if (count == 4) {
                                                                                                    int operasi = (komisiTukang - (jmlLaporan * 30)) / lengthSisa;
                                                                                                    int hasil = operasi * (-1);
                                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100
                                                                                                    Log.d("hasil", String.valueOf(komisiTukang) + " - "
                                                                                                            + String.valueOf(jmlLaporan) + " x 29" + " / " + lengthSisa + " " + hasil);

                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("KomisiPerhari").child(tanggal)
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                    //suportTotalKomisi
                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("TotalKomisi")
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                    //recap
                                                                                                    reference9.child(users.get(finalU).getKey_name())
                                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                } else if (count == 5) {
//
                                                                                                    int operasi = (komisiTukang - (jmlLaporan * 29)) / lengthSisa;
                                                                                                    int hasil = operasi * (-1);
                                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100
                                                                                                    Log.d("hasil", String.valueOf(komisiTukang) + " - "
                                                                                                            + String.valueOf(jmlLaporan) + " x 29" + " / " + lengthSisa + " " + hasil);

                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("KomisiPerhari").child(tanggal)
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                    //suportTotalKomisi
                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("TotalKomisi")
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });
                                                                                                    //recap
                                                                                                    reference9.child(users.get(finalU).getKey_name())
                                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });


                                                                                                } else if (count == 6) {
                                                                                                    int operasi = (komisiTukang - (jmlLaporan * 28)) / lengthSisa;
                                                                                                    int hasil = operasi * (-1);
                                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100
                                                                                                    Log.d("hasil", String.valueOf(komisiTukang) + " - "
                                                                                                            + String.valueOf(jmlLaporan) + " x 29" + " / " + lengthSisa + " " + hasil);

                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("KomisiPerhari").child(tanggal)
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                    //suportTotalKomisi
                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("TotalKomisi")
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });
                                                                                                    //recap
                                                                                                    reference9.child(users.get(finalU).getKey_name())
                                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                } else if (count == 7) {
                                                                                                    int operasi = (komisiTukang - (jmlLaporan * 27)) / lengthSisa;
                                                                                                    int hasil = operasi * (-1);
                                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100
                                                                                                    Log.d("hasil", String.valueOf(komisiTukang) + " - "
                                                                                                            + String.valueOf(jmlLaporan) + " x 29" + " / " + lengthSisa + " " + hasil);

                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("KomisiPerhari").child(tanggal)
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                    //suportTotalKomisi
                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("TotalKomisi")
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });
                                                                                                    //recap
                                                                                                    reference9.child(users.get(finalU).getKey_name())
                                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                } else if (count == 8) {
                                                                                                    int operasi = (komisiTukang - (jmlLaporan * 26)) / lengthSisa;
                                                                                                    int hasil = operasi * (-1);
                                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100
                                                                                                    Log.d("hasil", String.valueOf(komisiTukang) + " - "
                                                                                                            + String.valueOf(jmlLaporan) + " x 29" + " / " + lengthSisa + " " + hasil);

                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("KomisiPerhari").child(tanggal)
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                    //suportTotalKomisi
                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("TotalKomisi")
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                    //recap
                                                                                                    reference9.child(users.get(finalU).getKey_name())
                                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                } else if (count == 9 || count == 10) {
                                                                                                    int operasi = (komisiTukang - (jmlLaporan * 25)) / lengthSisa;
                                                                                                    int hasil = operasi * (-1);
                                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100
                                                                                                    Log.d("hasil", String.valueOf(komisiTukang) + " - "
                                                                                                            + String.valueOf(jmlLaporan) + " x 29" + " / " + lengthSisa + " " + hasil);

                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("KomisiPerhari").child(tanggal)
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });
                                                                                                    //suportTotalKomisi
                                                                                                    reference10.child(users.get(finalU).getKey_name()).child("TotalKomisi")
                                                                                                            .child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }
                                                                                                            });

                                                                                                    //recap
                                                                                                    reference9.child(users.get(finalU).getKey_name())
                                                                                                            .child(tanggal.substring(3)).child(tanggal)
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

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

                                    //komisiKaryawan
                                    Query queryKaryawan =  reference11.child(tanggal)
                                            .orderByChild("keterangan").equalTo("Hadir");
                                    queryKaryawan.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                            List<DataKaryawanConst> usersK = new ArrayList<>();
                                            while (iterator.hasNext()) {
                                                DataSnapshot dataSnapshotChild = iterator.next();
                                                DataKaryawanConst name = dataSnapshotChild.getValue(DataKaryawanConst.class);
                                                usersK.add(name);
                                            }
                                            int lengthKaryawan = (int) dataSnapshot.getChildrenCount();
//                                        int jmlLaporan = Integer.parseInt(xnominal.getText().toString());

                                            for (int u = 0 ; u < lengthKaryawan ; u++){
                                                int finalU = u;
                                                Log.d("karyawanku", usersK.get(u).getKey());
                                                reference10.child(usersK.get(u).getKey()).child("KomisiPerhari")
                                                        .child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                                        List<LaporanUangConst> lapUang = new ArrayList<>();
                                                        while (iterator.hasNext()) {
                                                            DataSnapshot dataSnapshotChild = iterator.next();
                                                            LaporanUangConst keyku = dataSnapshotChild.getValue(LaporanUangConst.class);
                                                            lapUang.add(keyku);
                                                        }

                                                        int lengthKomisi = (int) dataSnapshot.getChildrenCount();
                                                        for (int i = 0 ; i < lengthKomisi ; i++){

                                                            int komisiTukang = Integer.parseInt(lapUang.get(i).getNominal());

                                                            Query queryKep = reference4.orderByChild("posisi").equalTo("Kepala Cabang");
                                                            queryKep.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                                                    List<DataKaryawanConst> users = new ArrayList<>();
                                                                    while (iterator.hasNext()) {
                                                                        DataSnapshot dataSnapshotChild = iterator.next();
                                                                        DataKaryawanConst name = dataSnapshotChild.getValue(DataKaryawanConst.class);
                                                                        users.add(name);
                                                                    }

                                                                    int lengthK = (int) dataSnapshot.getChildrenCount();
                                                                    for (int i = 0 ; i < lengthK ; i++){

                                                                        Query queryKepala = reference10.child(users.get(i).getKey_name()).child("Absensi").child(tanggal).
                                                                                orderByChild("keterangan").equalTo("Hadir");
                                                                        queryKepala.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                int lengthKepala = (int) dataSnapshot.getChildrenCount();


                                                                                int lengthSisa = lengthKepala + lengthKaryawan;
                                                                                int jmlLaporan = Integer.parseInt(xnominal.getText().toString());

                                                                                if (count == 4) {
                                                                                    int operasi = (komisiTukang - (jmlLaporan * 30)) / lengthSisa;
                                                                                    int hasil = operasi * (-1);
                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100
                                                                                    Log.d("hasil", String.valueOf(komisiTukang) + " - "
                                                                                            + String.valueOf(jmlLaporan) + " x 29" + " / " + lengthSisa + " " + hasil);

                                                                                    reference10.child(usersK.get(finalU).getKey()).child("KomisiPerhari").child(tanggal)
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });

                                                                                    //suportTotalKomisi
                                                                                    reference10.child(usersK.get(finalU).getKey()).child("TotalKomisi")
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });

                                                                                    //recap
                                                                                    reference9.child(usersK.get(finalU).getKey())
                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });



                                                                                } else if (count == 5) {
//
                                                                                    int operasi = (komisiTukang - (jmlLaporan * 29)) / lengthSisa;
                                                                                    int hasil = operasi * (-1);
                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100

                                                                                    reference10.child(usersK.get(finalU).getKey()).child("KomisiPerhari").child(tanggal)
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                    //suportTotalKomisi
                                                                                    reference10.child(usersK.get(finalU).getKey()).child("TotalKomisi")
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });

                                                                                    //recap
                                                                                    reference9.child(usersK.get(finalU).getKey())
                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });


                                                                                }else if (count == 6) {
//
                                                                                    int operasi = (komisiTukang - (jmlLaporan * 28)) / lengthSisa;
                                                                                    int hasil = operasi * (-1);
                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100

                                                                                    reference10.child(usersK.get(finalU).getKey()).child("KomisiPerhari").child(tanggal)
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                    //suportTotalKomisi
                                                                                    reference10.child(usersK.get(finalU).getKey()).child("TotalKomisi")
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });

                                                                                    //recap
                                                                                    reference9.child(usersK.get(finalU).getKey())
                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });


                                                                                }else if (count == 7) {
//
                                                                                    int operasi = (komisiTukang - (jmlLaporan * 27)) / lengthSisa;
                                                                                    int hasil = operasi * (-1);
                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100

                                                                                    reference10.child(usersK.get(finalU).getKey()).child("KomisiPerhari").child(tanggal)
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                    //suportTotalKomisi
                                                                                    reference10.child(usersK.get(finalU).getKey()).child("TotalKomisi")
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });

                                                                                    //recap
                                                                                    reference9.child(usersK.get(finalU).getKey())
                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });


                                                                                }else if (count == 8) {
//
                                                                                    int operasi = (komisiTukang - (jmlLaporan * 26)) / lengthSisa;
                                                                                    int hasil = operasi * (-1);
                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100

                                                                                    reference10.child(usersK.get(finalU).getKey()).child("KomisiPerhari").child(tanggal)
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                    //suportTotalKomisi
                                                                                    reference10.child(usersK.get(finalU).getKey()).child("TotalKomisi")
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });

                                                                                    //recap
                                                                                    reference9.child(usersK.get(finalU).getKey())
                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });


                                                                                }else if (count == 9 || count == 10) {
//
                                                                                    int operasi = (komisiTukang - (jmlLaporan * 29)) / lengthSisa;
                                                                                    int hasil = operasi * (-1);
                                                                                    int rounded = (int) Math.round(hasil / 100d) * 100; // bulat 100

                                                                                    reference10.child(usersK.get(finalU).getKey()).child("KomisiPerhari").child(tanggal)
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                    //suportTotalKomisi
                                                                                    reference10.child(usersK.get(finalU).getKey()).child("TotalKomisi")
                                                                                            .child(tanggal)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });

                                                                                    //recap
                                                                                    reference9.child(usersK.get(finalU).getKey())
                                                                                            .child(tanggal.substring(3)).child(tanggal).
                                                                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    dataSnapshot.getRef().child("nominal").setValue(String.valueOf(rounded));

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

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                }
                            }, 1000);

                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent go = new Intent(InputLaporanOwner.this,LaporanOwnerAct.class);
                                    go.putExtra("key","TotalKomisi");
                                    go.putExtra("cabang",cabangku);
                                    startActivity(go);
                                    finish();
                                }
                            }, 10000);

//                            //total Komisi
//                            reference4.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
//
//                                    List<DataKaryawanConst> users = new ArrayList<>();
//                                    while (iterator.hasNext()) {
//                                        DataSnapshot dataSnapshotChild = iterator.next();
//                                        DataKaryawanConst name = dataSnapshotChild.getValue(DataKaryawanConst.class);
//                                        users.add(name);
//                                    }
//
//                                    int lengthUser = (int) dataSnapshot.getChildrenCount();
//
//                                    try {
//
//                                        for (int i = 0 ; i < lengthUser ; i++){
//
//                                            DatabaseReference reference9 = FirebaseDatabase.getInstance().getReference().child("Cabang")
//                                                    .child(cabang).child("CountKomisi").child(users.get(i).getKey_name());
//
//                                            reference9.child("KomisiPerhari").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
//
//                                                    List<LaporanUangConst> komisis = new ArrayList<>();
//                                                    while (iterator.hasNext()) {
//                                                        DataSnapshot dataSnapshotChild = iterator.next();
//                                                        LaporanUangConst nkomisi = dataSnapshotChild.getValue(LaporanUangConst.class);
//                                                        komisis.add(nkomisi);
//                                                    }
//
//                                                    int lengthuang = (int) dataSnapshot.getChildrenCount();
//
//                                                    int hasil = 0;
//
//                                                    try {
//
//                                                        for (int u = 0 ; u < lengthuang; u++){
//
//                                                            hasil +=  Integer.valueOf(komisis.get(u).getNominal());
//                                                            Log.d("Hasil", String.valueOf(hasil));
//                                                            reference10 = FirebaseDatabase.getInstance().getReference().child("Cabang")
//
//                                                                    .child(username_key_new).child("CountKomisi").child(komisis.get(u).getKey())
//                                                                    .child("TotalKomisi");
//                                                            reference7 = FirebaseDatabase.getInstance().getReference().child("Cabang")
//                                                                    .child(username_key_new).child("Karyawan").child(komisis.get(u).getKey());
//                                                        }
//
//                                                    }catch (Exception e){
//
//                                                    }
//
//                                                    int finalHasil = hasil;
//
//                                                    Log.d("hasilnya", String.valueOf(finalHasil));
//
//                                                    try {
//
//                                                        reference10.addValueEventListener(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                                dataSnapshot.getRef().child("total_komisi").setValue(String.valueOf(finalHasil));
//
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                            }
//
//                                                        });
//
//                                                        reference7.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                dataSnapshot.getRef().child("kompensasi").setValue(String.valueOf(finalHasil));
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                            }
//                                                        });
//
//                                                    }catch (Exception e){
//
//                                                    }
//
//
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                }
//                                            });
//
//                                        }
//
//                                    }catch (Exception e){
//
//                                    }
//
//
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });

                        }else{
                            Toast.makeText(InputLaporanOwner.this,
                                    tanggal+"Jumlah karyawan yang hadir "+count+", minimal 4 orang", Toast.LENGTH_LONG).show();
                            btnsave.setText("SAVE");
                        }





                    }



                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                saveAction(dateFormatter.format(newDate.getTime()));

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
