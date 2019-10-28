package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.company.appstore.Owner.LaporanUangAdapter;
import com.example.company.appstore.KepalaCabang.LaporanUangConst;
import com.example.company.appstore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LaporanOwnerAct extends AppCompatActivity implements LaporanUangAdapter.FirebaseDataListener {

    Button back;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<LaporanUangConst> laporanUangConsts;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_owner);

        String cabang = getIntent().getStringExtra("cabang");

        rvView = (RecyclerView) findViewById(R.id.laporan_uang_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        reference = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("LaporanUang");



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                laporanUangConsts = new ArrayList<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    LaporanUangConst uangConst = dataSnapshot1.getValue(LaporanUangConst.class);
                    uangConst.setKey(dataSnapshot1.getKey());

                    laporanUangConsts.add(uangConst);

                }
                adapter = new LaporanUangAdapter(laporanUangConsts, LaporanOwnerAct.this,
                        LaporanOwnerAct.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(LaporanOwnerAct.this,OwnerDashbordAct.class);
                startActivity(go);

            }
        });
    }

    @Override
    public void onDeleteData(LaporanUangConst laporanUangConst, int i) {
        if(reference!=null){
            reference.child(laporanUangConst.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(LaporanOwnerAct.this,"success delete", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
