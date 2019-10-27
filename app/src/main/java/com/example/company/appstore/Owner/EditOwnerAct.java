package com.example.company.appstore.Owner;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class EditOwnerAct extends AppCompatActivity {

    Button back, btnsave;
    EditText xpass, xnama;
    TextView xusername;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_owner);

        back = findViewById(R.id.back);
        btnsave = findViewById(R.id.btnsave);
        xpass = findViewById(R.id.xpass);
        xnama= findViewById(R.id.xnama);
        xusername= findViewById(R.id.xusername);

        getUsernameLocal();

//database

        //mengambil data
        reference = FirebaseDatabase.getInstance().getReference().child("Admin").child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xpass.setText(dataSnapshot.child("password").getValue().toString());
                xusername.setText(dataSnapshot.child("username").getValue().toString());
                xnama.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
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

                Intent go = new Intent(EditOwnerAct.this,OwnerDashbordAct.class);
                startActivity(go);
            }

        });



// Pindah activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(EditOwnerAct.this,OwnerDashbordAct.class);
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
