package com.example.company.appstore;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LaporanUangAct extends AppCompatActivity implements LaporanUangAdapter.FirebaseDataListener {

    Button btnplus,btnsave,back;
    EditText txtsearch;

    //testtta dicoba

    DatabaseReference reference;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<LaporanUangConst> laporanUangConsts;

    String USERNAME_KEY = "usernamekey";
    String username_key ="";
    String username_key_new ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_uang);

        btnplus = findViewById(R.id.btnplus);
        back = findViewById(R.id.back);
        txtsearch = findViewById(R.id.txtsearch);

        rvView = (RecyclerView) findViewById(R.id.laporan_uang_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        getUsernameLocal();

//database

        reference = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(username_key_new).child("LaporanUang");

        reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        laporanUangConsts = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                            LaporanUangConst uangConst = dataSnapshot1.getValue(LaporanUangConst.class);
                            uangConst.setKey(dataSnapshot1.getKey());

                            laporanUangConsts.add(uangConst);

                        }
                        adapter = new LaporanUangAdapter(laporanUangConsts,LaporanUangAct.this,
                                LaporanUangAct.this);
                        rvView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//pencarian data
        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    search(s.toString());
                }else {
                    search("");
                }


            }
        });


//pop up
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go = new Intent(LaporanUangAct.this,InputLaporanUangAct.class);
                startActivity(go);

//                final Dialog dialog = new Dialog(LaporanUangAct.this);
//                dialog.setContentView(R.layout.dialogview_keuangan);
//                dialog.show();
//
//                btnsave = dialog.findViewById(R.id.btnsave);
//                final TextView xtgl = dialog.findViewById(R.id.xtgl);
//                final EditText xnominal = dialog.findViewById(R.id.xnominal);
//
//
//                btnsave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        reference.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                dataSnapshot.getRef().child("tanggal").setValue(xtgl.getText().toString());
//                                dataSnapshot.getRef().child("nominal").setValue(xnominal.getText().toString());
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//
//                        });
//
//                        dialog.dismiss();
//                    }
//                });

            }
        });

//pindah activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(LaporanUangAct.this,DashbordAct.class);
                startActivity(go);

            }
        });
    }

//mengambil data local
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new =sharedPreferences.getString(username_key, "");

    }

//delete fungsi
    public void onDeleteData(LaporanUangConst laporanUangConst, final int i){
        if(reference!=null){
            reference.child(laporanUangConst.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(LaporanUangAct.this,"success delete", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

//search fungsi
    private void search(String s) {

        Query query = reference.orderByChild("tanggal")
                .startAt(s)
                .endAt(s+"\uf8ff");
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
