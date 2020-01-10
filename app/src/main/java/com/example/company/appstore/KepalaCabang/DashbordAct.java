package com.example.company.appstore.KepalaCabang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.Owner.Login2Act;
import com.example.company.appstore.Owner.OwnerDashbordAct;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DashbordAct extends AppCompatActivity{

    TextView keuangan, absensi,namakepala,namatoko,tglKeuangan, jmlkaryawan, jmlhadir,tgl;
    Button logout;
    LinearLayout linearlayout, linearlayout2;
    ImageView absen_belum,absen_sudah,laporan_belum,laporan_sudah;

    DatabaseReference reference, reference2, reference3;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        getUsernameLocal();

        namakepala = findViewById(R.id.namakepala);
        namatoko = findViewById(R.id.namatoko);
        tglKeuangan = findViewById(R.id.tglKeuangan);
        tgl = findViewById(R.id.tgl);
        jmlkaryawan = findViewById(R.id.jmlkaryawan);
        jmlhadir = findViewById(R.id.jmlhadir);
        keuangan = findViewById(R.id.keuangan);
        absensi = findViewById(R.id.absensi);
        linearlayout = findViewById(R.id.linearLayout);
        linearlayout2 = findViewById(R.id.linearLayout2);
        absen_belum = findViewById(R.id.absen_belum);
        absen_sudah = findViewById(R.id.absen_sudah);
        laporan_belum = findViewById(R.id.laporan_belum);
        laporan_sudah = findViewById(R.id.laporan_sudah);
        logout = findViewById(R.id.logout);

        //set tgl
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String dateString = sdf.format(date);

        SimpleDateFormat sdflocal = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        String dateLocal = sdflocal.format(date);
//        tglKeuangan.setText(dateString);
        tgl.setText(dateLocal);

// notif

        reference2 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(username_key_new).child("CountAbsen").child(dateString);
        reference3 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(username_key_new).child("CekLaporan").child(dateString);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                jmlhadir.setText(String.valueOf(count)+ " Hadir dari ");
                if (count > 3 ){
                    absen_sudah.animate().alpha(1).setDuration(300).start();
                    absen_belum.animate().alpha(0).setDuration(300).start();
//                    absen_sudah.setBackgroundDrawable(ContextCompat.getDrawable(DashbordAct.this, R.drawable.icon_sudah));

                }else {
                    absen_belum.animate().alpha(1).setDuration(300).start();
                    absen_sudah.animate().alpha(0).setDuration(300).start();
//                    absen_belum.setBackgroundDrawable(ContextCompat.getDrawable(DashbordAct.this, R.drawable.icon_belum));

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                if (count != 0){
                    laporan_sudah.animate().alpha(1).setDuration(300).start();
                    laporan_belum.animate().alpha(0).setDuration(300).start();
                    reference3.child(dateString).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            tglKeuangan.setText("Rp. "+NumberFormat.getNumberInstance(Locale.getDefault()).
                                    format(Double.parseDouble(dataSnapshot.child("nominal").getValue().toString())));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }else {
                    laporan_sudah.animate().alpha(0).setDuration(300).start();
                    laporan_belum.animate().alpha(1).setDuration(300).start();
                    tglKeuangan.setText("Rp. "+"0");
                }

//

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




//Get Jumlah Data Karyawan
        reference = FirebaseDatabase.getInstance().getReference().
                child("Cabang").child(username_key_new).child("Karyawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer jml = (int) dataSnapshot.getChildrenCount();
                jmlkaryawan.setText(jml+" Karyawan" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//database
        reference= FirebaseDatabase.getInstance().getReference().
                child("KepalaCabang").child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namakepala.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                namatoko.setText(dataSnapshot.child("nama_cabang").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//Pindah activity
        keuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DashbordAct.this,LaporanUangAct.class);
                startActivity(go);

            }
        });
        absensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DashbordAct.this,AbsensiAct.class);
                startActivity(go);

            }
        });
        linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DashbordAct.this,AbsensiAct.class);
                startActivity(go);

            }
        });
        linearlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DashbordAct.this,LaporanUangAct.class);
                startActivity(go);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(DashbordAct.this)
                                .setTitle("Logout Account")
                                .setMessage("Apakah anda yakin?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent go = new Intent(DashbordAct.this,LoginAct.class);
                                        startActivity(go);
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();


                    }
                });


            }
        });

    }

//fungsi mengambil username local sesuai login
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new =sharedPreferences.getString(username_key, "");

    }


    boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik lagi keluar aplikasi", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}
