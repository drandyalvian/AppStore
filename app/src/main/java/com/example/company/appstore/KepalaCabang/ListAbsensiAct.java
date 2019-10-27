package com.example.company.appstore.KepalaCabang;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListAbsensiAct extends AppCompatActivity {
    private static ListAbsensiAct instance;
    DatabaseReference reference;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ListAbsensiConst> labsensiConsts;

    String USERNAME_KEY = "usernamekey";
    String username_key ="";
    String username_key_new ="";
    String nKaryawan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_absensi);
        instance = this;

        rvView = (RecyclerView) findViewById(R.id.labsensi_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        getUsernameLocal();

//mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String nama_karyawan= bundle.getString("key");
        nKaryawan = nama_karyawan;


//database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Cabang").child(username_key_new).child("Karyawan")
                .child(nama_karyawan).child("Absensi")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        labsensiConsts = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                            ListAbsensiConst laConst = dataSnapshot1.getValue(ListAbsensiConst.class);
                            laConst.setKey(dataSnapshot1.getKey());

                            labsensiConsts.add(laConst);

                        }
                        adapter = new ListAbsensiAdapter(labsensiConsts,ListAbsensiAct.this);
                        rvView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public static ListAbsensiAct getInstance() {
        return instance;
    }

    //update absen
    public void updateAbsen(String key, String keterangan){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cabang").child(username_key_new).child("Karyawan").child(nKaryawan).child("Absensi").child(key);
        ListAbsensiConst listAbsensiConst = new ListAbsensiConst(keterangan, key, key);
        db.setValue(listAbsensiConst);
    }

//mengambil data local
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new =sharedPreferences.getString(username_key, "");

    }
}
