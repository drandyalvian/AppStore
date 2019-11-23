package com.example.company.appstore.Owner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTokoAct extends AppCompatActivity {

    Button back,btnsave;
    EditText xnama, xpass;
    TextView  xusername, texttoko;

    DatabaseReference reference;

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

//mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String cabangx= bundle.getString("nama");

        reference = FirebaseDatabase.getInstance().getReference().child("KepalaCabang").child(cabangx);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xpass.setText(dataSnapshot.child("password").getValue().toString());
                xusername.setText(dataSnapshot.child("username").getValue().toString());
                xnama.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
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
}
