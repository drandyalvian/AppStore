package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.company.appstore.R;

public class EditKaryawanAct extends AppCompatActivity {

    Button back;
    Spinner xspiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_karyawan);

        back = findViewById(R.id.back);
        xspiner = findViewById(R.id.xspiner);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(EditKaryawanAct.this,DataKaryawanAct.class);
                startActivity(go);

            }
        });

        final ArrayAdapter pilihGender=ArrayAdapter.createFromResource(this, R.array.pilih_gender, android.R.layout.simple_spinner_dropdown_item);
        xspiner.setAdapter(pilihGender);
    }
}
