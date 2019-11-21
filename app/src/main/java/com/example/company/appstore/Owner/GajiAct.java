package com.example.company.appstore.Owner;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.company.appstore.KepalaCabang.AbsensiConst;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GajiAct extends AppCompatActivity {

    Button back, print;
    RelativeLayout inputgaji;

    DatabaseReference reference;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GajiConst> gajiConsts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaji);

        back = findViewById(R.id.back);
        inputgaji = findViewById(R.id.inputgaji);
        print= findViewById(R.id.print);

        rvView = (RecyclerView) findViewById(R.id.gaji_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        final String cabang = getIntent().getStringExtra("cabang");


        reference = FirebaseDatabase.getInstance().getReference()
                .child("Cabang").child(cabang).child("Karyawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gajiConsts = new ArrayList<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    GajiConst aConst = dataSnapshot1.getValue(GajiConst.class);
                    aConst.setKey(dataSnapshot1.getKey());
                    aConst.setCabang(cabang);
                    gajiConsts.add(aConst);

                }
                adapter = new GajiAdapter(gajiConsts, GajiAct.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(GajiAct.this,OwnerDashbordAct.class);
                startActivity(go);

            }
        });

//        inputgaji.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog = new Dialog(GajiAct.this);
//                dialog.setContentView(R.layout.dialogview_input_gaji);
//                dialog.show();
//
//            }
//        });
//        print.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final Dialog dialog = new Dialog(GajiAct.this);
//                dialog.setContentView(R.layout.dialogview_pinjaman);
//                dialog.show();
//
//            }
//        });


    }
}
