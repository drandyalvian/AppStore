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
import com.google.firebase.database.ValueEventListener;

public class OwnerDashbordAct extends AppCompatActivity {

    TextView xnama, jml_C1, jml_C2, jml_C3;
    LinearLayout gajik1,laporank1,datak1;
    Button logout, edit_toko;
    ImageView edit_owner;

    DatabaseReference reference, reference2;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashbord);

        getUsernameLocal();

        gajik1 = findViewById(R.id.gajik1);
        laporank1 = findViewById(R.id.laporank1);
        datak1 = findViewById(R.id.datak1);
        logout = findViewById(R.id.logout);
        edit_toko = findViewById(R.id.edit_toko);
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
                startActivity(go);

            }
        });
        datak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,DataKaryawanAct.class);
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

        edit_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(OwnerDashbordAct.this,EditOwnerAct.class);
                startActivity(go);

            }
        });
    }

//fungsi mengambil username local sesuai login
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new =sharedPreferences.getString(username_key, "");

    }
}
