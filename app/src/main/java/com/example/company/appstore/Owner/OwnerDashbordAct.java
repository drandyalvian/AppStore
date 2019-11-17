package com.example.company.appstore.Owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OwnerDashbordAct extends AppCompatActivity {

    TextView xnama, jml_C1, jml_C2, jml_C3, tgl_cabang1, tgl_cabang2, tgl_cabang3, nc1, nc2, nc3;
    LinearLayout gajik1,laporank1,datak1, laporank2, laporank3, datak2, datak3;
    Button logout, edit_toko, edit_toko2, edit_toko3;
    ImageView edit_owner;

    DatabaseReference reference, reference2, reference3, reference4, reference5;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new ="";
    private String outputDateStr = "";

    public static final SimpleDateFormat ymdFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);

    public static final SimpleDateFormat EEEddMMMyyyy = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashbord);

        getUsernameLocal();

        gajik1 = findViewById(R.id.gajik1);
        laporank1 = findViewById(R.id.laporank1);
        laporank2 = findViewById(R.id.laporank2);
        laporank3 = findViewById(R.id.laporank3);
        datak1 = findViewById(R.id.datak1);
        datak2 = findViewById(R.id.datak2);
        datak3 = findViewById(R.id.datak3);
        logout = findViewById(R.id.logout);
        edit_toko = findViewById(R.id.edit_toko);
        edit_toko2 = findViewById(R.id.edit_toko2);
        edit_toko3 = findViewById(R.id.edit_toko3);
        edit_owner = findViewById(R.id.edit_owner);
        xnama = findViewById(R.id.xnama);
        jml_C1 = findViewById(R.id.ket_jmlc1);
        jml_C2 = findViewById(R.id.ket_jmlc2);
        jml_C3 = findViewById(R.id.ket_jmlc3);


        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child("cabang1").child("Karyawan");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Admin");

        reference2.child(username_key_new).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xnama.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get count Karyawan cabang 1
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Integer jml = (int) dataSnapshot.getChildrenCount();
                    jml_C1.setText(Integer.toString(jml) + " Karyawan");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get Count Karyawan cabang 2
        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child("cabang2").child("Karyawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Integer jml = (int) dataSnapshot.getChildrenCount();
                    jml_C2.setText(Integer.toString(jml) + " Karyawan");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get count karyawan cabang 3
        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child("cabang3").child("Karyawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Integer jml = (int) dataSnapshot.getChildrenCount();
                    jml_C3.setText(Integer.toString(jml) + " Karyawan");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        reference3 = FirebaseDatabase.getInstance().getReference().child("Cabang").child("cabang1").child("LaporanUang");
        Query last = reference3.orderByKey().limitToLast(1);

        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        last.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    tgl_cabang1 = findViewById(R.id.tgl_cabang1);
                    nc1 = findViewById(R.id.nominal_cabang1);
                    double nominal = Double.parseDouble(ds.child("nominal").getValue().toString());
                    String tanggal = parseDate(ds.child("tanggal").getValue().toString(), ymdFormat, EEEddMMMyyyy);
                    nc1.setText(formatRupiah.format ((double)nominal));
                    tgl_cabang1.setText(tanggal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference4 = FirebaseDatabase.getInstance().getReference().child("Cabang").child("cabang2").child("LaporanUang");
        Query last2 = reference4.orderByKey().limitToLast(1);
        last2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    tgl_cabang2 = findViewById(R.id.tgl_cabang2);
                    nc2 = findViewById(R.id.nominal_cabang2);
                    double nominal = Double.parseDouble(ds.child("nominal").getValue().toString());
                    String tanggal = parseDate(ds.child("tanggal").getValue().toString(), ymdFormat, EEEddMMMyyyy);
                    nc2.setText(formatRupiah.format ((double)nominal));
                    tgl_cabang2.setText(tanggal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference5 = FirebaseDatabase.getInstance().getReference().child("Cabang").child("cabang3").child("LaporanUang");
        Query last3 = reference5.orderByKey().limitToLast(1);
        last3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    tgl_cabang3 = findViewById(R.id.tgl_cabang3);
                    nc3 = findViewById(R.id.nominal_cabang3);
                    double nominal = Double.parseDouble(ds.child("nominal").getValue().toString());
                    String tanggal = parseDate(ds.child("tanggal").getValue().toString(), ymdFormat, EEEddMMMyyyy);
                    nc3.setText(formatRupiah.format ((double)nominal));
                    tgl_cabang3.setText(tanggal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//Pindah activity
        gajik1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,GajiAct.class);
                startActivity(go);

            }
        });

        laporank1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,LaporanOwnerAct.class);
                go.putExtra("cabang","cabang1");
                startActivity(go);

            }
        });
        datak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,DataKaryawanAct.class);
                go.putExtra("cabang", "cabang1");
                startActivity(go);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,Login2Act.class);
                startActivity(go);

            }
        });

        edit_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "cabang1";
                Intent go = new Intent(OwnerDashbordAct.this,EditTokoAct.class);
                go.putExtra("nama", s);
                startActivity(go);

            }
        });

        edit_toko2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "cabang2";
                Intent go = new Intent(OwnerDashbordAct.this,EditTokoAct.class);
                go.putExtra("nama", s);
                startActivity(go);

            }
        });

        edit_toko3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "cabang3";
                Intent go = new Intent(OwnerDashbordAct.this,EditTokoAct.class);
                go.putExtra("nama", s);
                startActivity(go);

            }
        });

        edit_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,EditOwnerAct.class);
                startActivity(go);

            }
        });

        laporank2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,LaporanOwnerAct.class);
                go.putExtra("cabang","cabang2");
                startActivity(go);
            }
        });

        laporank3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,LaporanOwnerAct.class);
                go.putExtra("cabang","cabang3");
                startActivity(go);
            }
        });

        datak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,DataKaryawanAct.class);
                go.putExtra("cabang", "cabang2");
                startActivity(go);
            }
        });

        datak3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,DataKaryawanAct.class);
                go.putExtra("cabang", "cabang3");
                startActivity(go);
            }
        });

    }



    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }


//fungsi mengambil username local sesuai login
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new =sharedPreferences.getString(username_key, "");

    }
}
