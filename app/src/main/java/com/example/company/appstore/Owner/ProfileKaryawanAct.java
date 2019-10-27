package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.company.appstore.R;

public class ProfileKaryawanAct extends AppCompatActivity {

    Button btnedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_karyawan);

        btnedit = findViewById(R.id.btnedit);

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(ProfileKaryawanAct.this,EditKaryawanAct.class);
                startActivity(go);

            }
        });
    }
}
