package com.example.company.appstore.KepalaCabang;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.Owner.InputLaporanOwner;
import com.example.company.appstore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LaporanUangAct extends AppCompatActivity implements LaporanUangAdapter.FirebaseDataListener {

    Button btnplus, btnsave, back;
    EditText txtsearch;
    TextView xToday;

    DatabaseReference reference, reference2;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<LaporanUangConst> laporanUangConsts;
    private SimpleDateFormat dateFormatter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_uang);
        ButterKnife.bind(this);

        btnplus = findViewById(R.id.btnplus);
        back = findViewById(R.id.back);
        xToday = findViewById(R.id.xToday);

//        txtsearch = findViewById(R.id.txtsearch);

        rvView = (RecyclerView) findViewById(R.id.laporan_uang_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);


        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());//formating according to my need
        String date = formatter.format(today);

        long dateku = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        String dateString = sdf.format(dateku);

        xToday.setText(date);


        getUsernameLocal();

//database

        reference = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(username_key_new).child("LaporanUang");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(username_key_new).child("CekLaporan").child(dateString);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                laporanUangConsts = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    LaporanUangConst uangConst = dataSnapshot1.getValue(LaporanUangConst.class);
                    uangConst.setKey(dataSnapshot1.getKey());

                    laporanUangConsts.add(uangConst);

                }
                adapter = new LaporanUangAdapter(laporanUangConsts, LaporanUangAct.this,
                        LaporanUangAct.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//pindah activity
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go = new Intent(LaporanUangAct.this, InputLaporanUangAct.class);
                startActivity(go);
                finish();


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(LaporanUangAct.this, DashbordAct.class);
                startActivity(go);

            }
        });
    }

    //mengambil data local
    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");

    }

    //delete fungsi
    public void onDeleteData(LaporanUangConst laporanUangConst, final int i) {
        if (reference != null) {
            reference.child(laporanUangConst.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(LaporanUangAct.this, "success delete", Toast.LENGTH_SHORT).show();
                }
            });
            reference2.child(laporanUangConst.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });

        }
    }

    public void aFilter(String fbulan, String ftahun){


        Query query = reference.orderByChild("filter").equalTo(fbulan+" "+ftahun);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    laporanUangConsts.clear();
                    for (DataSnapshot dss: dataSnapshot.getChildren()){
                        final LaporanUangConst laporan = dss.getValue(LaporanUangConst.class);
                        laporanUangConsts.add(laporan);
                    }

                    LaporanUangAdapter adapter = new LaporanUangAdapter(laporanUangConsts,LaporanUangAct.this,
                            LaporanUangAct.this );
                    rvView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(LaporanUangAct.this, fbulan+" "+ftahun, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void allData(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                laporanUangConsts = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    LaporanUangConst uangConst = dataSnapshot1.getValue(LaporanUangConst.class);
                    uangConst.setKey(dataSnapshot1.getKey());

                    laporanUangConsts.add(uangConst);

                }
                adapter = new LaporanUangAdapter(laporanUangConsts, LaporanUangAct.this,
                        LaporanUangAct.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_filter)
    public void onViewClicked() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView2 = LayoutInflater.from(this).inflate(R.layout.dialogview_filter, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView2);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText ftahun = (EditText) dialogView2.findViewById(R.id.ftahun);
//        EditText fbulan = (EditText) dialogView2.findViewById(R.id.fbulan);
        Button btnAll = (Button) dialogView2.findViewById(R.id.btnAll);

        Spinner fbulan = (Spinner) dialogView2.findViewById(R.id.fbulan);

        final ArrayAdapter pilihFilter = ArrayAdapter.createFromResource(this, R.array.pilih_filter, android.R.layout.simple_spinner_dropdown_item);
        fbulan.setAdapter(pilihFilter);


        Button btnSave2 =(Button) dialogView2.findViewById(R.id.btnsaves);
        btnSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ftahun.getText().toString().equals("")) {

                    Toast.makeText(LaporanUangAct.this, "Tidak boleh kosong !", Toast.LENGTH_SHORT).show();
                } else {
                    aFilter(fbulan.getSelectedItem().toString(), ftahun.getText().toString());
                    alertDialog.hide();
                }

            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.hide();
                allData();


            }
        });
    }

//search fungsi
//    private void search(String s) {
//
//        Query query = reference.orderByChild("tanggal")
//                .startAt(s)
//                .endAt(s+"\uf8ff");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChildren()){
//                    laporanUangConsts.clear();
//                    for (DataSnapshot dss: dataSnapshot.getChildren()){
//                        final LaporanUangConst laporan = dss.getValue(LaporanUangConst.class);
//                        laporanUangConsts.add(laporan);
//                    }
//
//                    LaporanUangAdapter adapter = new LaporanUangAdapter(laporanUangConsts,LaporanUangAct.this,
//                            LaporanUangAct.this );
//                    rvView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
