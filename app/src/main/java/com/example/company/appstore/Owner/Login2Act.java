package com.example.company.appstore.Owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.company.appstore.KepalaCabang.DashbordAct;
import com.example.company.appstore.KepalaCabang.LoginAct;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login2Act extends AppCompatActivity {

    Button btn_kcabang,btn_login;
    EditText xusername, xpass;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        btn_kcabang = findViewById(R.id.btn_kcabang);
        btn_login = findViewById(R.id.btn_login);
        xusername = findViewById(R.id.xusername);
        xpass = findViewById(R.id.xpass);

        // Login

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(xusername.length() == 0){
                    xusername.requestFocus();
                    xusername.setError("Masukan Username!");
                }else if (xpass.length() == 0){
                    xpass.requestFocus();
                    xpass.setError("Masukan Password!");
                }else {

                    btn_login.setEnabled(false);
                    btn_login.setText("Loading...");

                    //database
                    String username = xusername.getText().toString();
                    final String password = xpass.getText().toString();

                    reference= FirebaseDatabase.getInstance().getReference().
                            child("Admin").child(username);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                // ambil data password dari firbase
                                String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                //Validasi password dengan firebase
                                if(password.equals(passwordFromFirebase)){


                                    //Simpan username key pada local
                                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(username_key, xusername.getText().toString());
                                    editor.apply();

                                    //pindah activity
                                    Intent gotoprofil = new Intent(Login2Act.this,OwnerDashbordAct.class);
                                    startActivity(gotoprofil);


                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Password salah!", Toast.LENGTH_SHORT).show();
                                    btn_login.setEnabled(true);
                                    btn_login.setText("Login");
                                }

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Username tidak ada!", Toast.LENGTH_SHORT).show();
                                btn_login.setEnabled(true);
                                btn_login.setText("Login");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                //loading





            }
        });

//Pindah Activity
        btn_kcabang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(Login2Act.this,LoginAct.class);
                startActivity(go);

            }
        });

    }

    public void validation(){

    }
}
