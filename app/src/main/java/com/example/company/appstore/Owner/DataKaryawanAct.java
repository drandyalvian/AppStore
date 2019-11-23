package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.company.appstore.KepalaCabang.AbsensiAct;
import com.example.company.appstore.KepalaCabang.AbsensiAdapter;
import com.example.company.appstore.KepalaCabang.AbsensiConst;
import com.example.company.appstore.KepalaCabang.LaporanUangConst;
import com.example.company.appstore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataKaryawanAct extends AppCompatActivity implements DataKaryawanAdapter.FirebaseDataListener {


    Button back, btnplus2;
    LinearLayout profilk;

    DatabaseReference reference;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<AbsensiConst> absensiConsts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_karyawan);

        back = findViewById(R.id.back);

        btnplus2 = findViewById(R.id.btnplus2);

        rvView = (RecyclerView) findViewById(R.id.karyawan_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        final String cabang = getIntent().getStringExtra("cabang");

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Cabang").child(cabang).child("Karyawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                absensiConsts = new ArrayList<>();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    AbsensiConst aConst = dataSnapshot1.getValue(AbsensiConst.class);

                     aConst.setKey(dataSnapshot1.child("key").getValue().toString());
//
                    absensiConsts.add(aConst);

                }
                adapter = new DataKaryawanAdapter(absensiConsts, DataKaryawanAct.this, DataKaryawanAct.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this,OwnerDashbordAct.class);
                startActivity(go);

            }
        });

        btnplus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this,AddKaryawanAct.class);
                go.putExtra("cabang",cabang);
                startActivity(go);

            }
        });
    }

    @Override
    public void onDeleteData(AbsensiConst absensiConsts, int i) {
        if(reference!=null){
            reference.child(AbsensiConst.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(DataKaryawanAct.this,"success delete", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
