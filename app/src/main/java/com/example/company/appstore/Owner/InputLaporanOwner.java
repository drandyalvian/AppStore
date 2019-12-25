package com.example.company.appstore.Owner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.company.appstore.KepalaCabang.LaporanUangAct;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InputLaporanOwner extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView xtgl;
    private Button addtgl,btnsave, back;
    EditText xnominal;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key ="";
    String username_key_new ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_laporan_owner);

        final String cabang = getIntent().getStringExtra("cabang");

        getUsernameLocal();


        xtgl = findViewById(R.id.xtgl);
        addtgl = findViewById(R.id.addtgl);
        xnominal = findViewById(R.id.xnominal);
        btnsave = findViewById(R.id.btnsave);
        back = findViewById(R.id.back);

        xtgl.addTextChangedListener(loginTextWatcher);
        xnominal.addTextChangedListener(loginTextWatcher);

        reference = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("LaporanUang");


        addtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.child(xtgl.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("tanggal").setValue(xtgl.getText().toString());
                        dataSnapshot.getRef().child("filter").setValue(xtgl.getText().toString().substring(3));
                        dataSnapshot.getRef().child("key").setValue(xtgl.getText().toString());
                        dataSnapshot.getRef().child("nominal").setValue(xnominal.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent go = new Intent(InputLaporanOwner.this, LaporanOwnerAct.class);
                go.putExtra("cabang",cabang);
                startActivity(go);
//                finish();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(InputLaporanOwner.this, LaporanOwnerAct.class);
                go.putExtra("cabang",cabang);
                startActivity(go);
            }
        });


    }

    private void showDateDialog(){


        Calendar newCalendar = Calendar.getInstance();

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                long date = System.currentTimeMillis();
                xtgl.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    //mengambil data local
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new =sharedPreferences.getString(username_key, "");

    }


    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String tglInput = xtgl.getText().toString().trim();
            String nominalInput = xnominal.getText().toString().trim();

            btnsave.setEnabled(!tglInput.isEmpty() && !nominalInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
