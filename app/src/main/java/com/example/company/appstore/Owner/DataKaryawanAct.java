package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.company.appstore.R;

public class DataKaryawanAct extends AppCompatActivity {

    Button back, btnplus2;
    LinearLayout profilk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_karyawan);

        back = findViewById(R.id.back);
        profilk = findViewById(R.id.profilk);
        btnplus2 = findViewById(R.id.btnplus2);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this,OwnerDashbordAct.class);
                startActivity(go);

            }
        });

        profilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this,ProfileKaryawanAct.class);
                startActivity(go);

            }
        });

        btnplus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this,EditKaryawanAct.class);
                startActivity(go);

            }
        });
    }
}
