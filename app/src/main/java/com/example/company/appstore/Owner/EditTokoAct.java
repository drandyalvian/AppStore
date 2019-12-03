package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditTokoAct extends AppCompatActivity {

    Button back,btnsave;
    EditText xnama, xpass;
    TextView  xusername, texttoko;
    Spinner selectKepala;

    DatabaseReference reference, getSpinner;

    ValueEventListener listener;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_toko);

        back = findViewById(R.id.back);
        xnama = findViewById(R.id.xnama);
        xusername = findViewById(R.id.xusername);
        xpass = findViewById(R.id.xpass);
        texttoko = findViewById(R.id.texttoko);
        btnsave = findViewById(R.id.btnsave);
        selectKepala = findViewById(R.id.selectKepala);

        Bundle bundle = getIntent().getExtras();
        final String cabangx= bundle.getString("nama");

        getSpinner = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangx).child("Karyawan");

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(EditTokoAct.this, android.R.layout.simple_spinner_dropdown_item, arrayList);

        selectKepala.setAdapter(arrayAdapter);
        getDataSpinner();

        selectKepala.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EditTokoAct.this, selectKepala.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//mengambil data dari intent


        reference = FirebaseDatabase.getInstance().getReference().child("KepalaCabang").child(cabangx);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xpass.setText(dataSnapshot.child("password").getValue().toString());
                xusername.setText(dataSnapshot.child("username").getValue().toString());
                //xnama.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                texttoko.setText(dataSnapshot.child("nama_cabang").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //update data
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.child(xusername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("password").setValue(xpass.getText().toString());
                        dataSnapshot.getRef().child("nama_lengkap").setValue(xnama.getText().toString());
                        dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent go = new Intent(EditTokoAct.this,OwnerDashbordAct.class);
                startActivity(go);
            }

        });

// Pindah Activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(EditTokoAct.this,OwnerDashbordAct.class);
                startActivity(go);

            }
        });
    }

    private void getDataSpinner(){
        listener = getSpinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item:dataSnapshot.getChildren()){

                    getSpinner.child(item.getValue().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            arrayList.add(dataSnapshot.getRef().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
