package com.example.company.appstore.Owner;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.company.appstore.KepalaCabang.InputLaporanUangAct;
import com.example.company.appstore.KepalaCabang.LaporanUangAct;
import com.example.company.appstore.Owner.LaporanUangAdapter;
import com.example.company.appstore.KepalaCabang.LaporanUangConst;
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

public class LaporanOwnerAct extends AppCompatActivity implements LaporanUangAdapter.FirebaseDataListener {

    Button btnplus, btnsave, back;
    TextView xToday;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<LaporanUangConst> laporanUangConsts;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_owner);
        ButterKnife.bind(this);

        xToday = findViewById(R.id.xToday);
        btnplus = findViewById(R.id.btnplus);
        back = findViewById(R.id.back);

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());//formating according to my need
        String date = formatter.format(today);
        xToday.setText(date);


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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(LaporanOwnerAct.this,OwnerDashbordAct.class);
                startActivity(go);

            }
        });

        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go = new Intent(LaporanOwnerAct.this, InputLaporanOwner.class);
                go.putExtra("cabang",cabang);
                startActivity(go);
                finish();


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

    public void aFilter(String fbulan, String ftahun){

        Toast.makeText(LaporanOwnerAct.this, fbulan+" "+ftahun, Toast.LENGTH_SHORT).show();

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

                    adapter = new LaporanUangAdapter(laporanUangConsts,LaporanOwnerAct.this,
                            LaporanOwnerAct.this );
                    rvView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
                adapter = new LaporanUangAdapter(laporanUangConsts, LaporanOwnerAct.this,
                        LaporanOwnerAct.this);
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

                    Toast.makeText(LaporanOwnerAct.this, "Tidak boleh kosong !", Toast.LENGTH_SHORT).show();
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
}
