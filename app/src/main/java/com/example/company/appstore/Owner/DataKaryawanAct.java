package com.example.company.appstore.Owner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataKaryawanAct extends AppCompatActivity implements DataKaryawanAdapter.FirebaseDataListener {


    Button back, btnplus2;
    LinearLayout profilk;
    EditText txtsearch;

    DatabaseReference reference, reference2;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataKaryawanConst> dataKaryawanConsts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_karyawan);
        ButterKnife.bind(this);

        back = findViewById(R.id.back);
        txtsearch = findViewById(R.id.txtsearch);

        btnplus2 = findViewById(R.id.btnplus2);

        rvView = (RecyclerView) findViewById(R.id.karyawan_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        final String cabang = getIntent().getStringExtra("cabang");

        reference2 = FirebaseDatabase.getInstance().getReference()
                .child("KepalaCabang").child(cabang);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Cabang").child(cabang).child("Karyawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataKaryawanConsts = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    DataKaryawanConst kConst = dataSnapshot1.getValue(DataKaryawanConst.class);

                    kConst.setKey(dataSnapshot1.getKey());

                    dataKaryawanConsts.add(kConst);
                }

                adapter = new DataKaryawanAdapter(dataKaryawanConsts, DataKaryawanAct.this, DataKaryawanAct.this);
                rvView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //pencarian data

        //membuat awal kata auto huruf besar (senistive search)


        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    search(s.toString());
                } else {

                    search("");

                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this, OwnerDashbordAct.class);
                startActivity(go);

            }
        });

        btnplus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this, AddKaryawanAct.class);
                go.putExtra("cabang", cabang);
                startActivity(go);

            }
        });
    }


    @Override
    public void onDeleteData(DataKaryawanConst dataKaryawanConst, int i) {

        if (reference != null) {

//            reference.child(DataKaryawanConst.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//
//                    Toast.makeText(DataKaryawanAct.this,"success delete", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }


    //search fungsi
    private void search(String s) {


        Query query = reference.orderByChild("nama")
                .startAt(s)
                .endAt(s + "\uf8ff"); //

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    dataKaryawanConsts.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        final DataKaryawanConst dataK = dss.getValue(DataKaryawanConst.class);
                        dataKaryawanConsts.add(dataK);


                    }

                    DataKaryawanAdapter adapter = new DataKaryawanAdapter(dataKaryawanConsts, DataKaryawanAct.this, DataKaryawanAct.this);
                    rvView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btnRecap)
    public void onViewClicked() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView2 = LayoutInflater.from(this).inflate(R.layout.dialogview_recap, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView2);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText ftahun = (EditText) dialogView2.findViewById(R.id.ftahun);
        TextView dcabang = (TextView) dialogView2.findViewById(R.id.dcabang);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dcabang.setText(dataSnapshot.child("nama_cabang").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Spinner fbulan = (Spinner) dialogView2.findViewById(R.id.fbulan);

        final ArrayAdapter pilihFilter = ArrayAdapter.createFromResource(this, R.array.pilih_filter, android.R.layout.simple_spinner_dropdown_item);
        fbulan.setAdapter(pilihFilter);


        Button btnSave2 =(Button) dialogView2.findViewById(R.id.btnsaves);
        btnSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.hide();

            }
        });
    }
}
