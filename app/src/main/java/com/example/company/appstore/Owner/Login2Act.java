package com.example.company.appstore.Owner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
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

    boolean doubleBackToExitPressedOnce = false;

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

        xusername.setText("admin");
        xpass.setText("1111");

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

                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    android.net.NetworkInfo wifi = cm
                            .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    android.net.NetworkInfo datac = cm
                            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if((wifi != null & datac != null) && (wifi.isConnected()| datac.isConnected())){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

//                                Toast.makeText(LoginAct.this, "Koneksi lambat", Toast.LENGTH_SHORT).show();

                                btn_login.setText("LOGIN");
                                btn_login.setEnabled(true);

//                                new AlertDialog.Builder(Login2Act.this)
//                                        .setTitle("Koneksi Lambat ")
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .setMessage("Tunggu...?")
//                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                                btn_login.setText("LOGIN");
//                                                btn_login.setEnabled(true);
//                                            }})
//                                        .setPositiveButton(android.R.string.ok, null).show();

                            }
                        }, 1*15*1000);


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

                    }else {

                        Toast.makeText(Login2Act.this, "Koneksi tidak tersedia", Toast.LENGTH_SHORT).show();
                        btn_login.setText("LOGIN");
                        btn_login.setEnabled(true);

                    }


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

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik lagi keluar aplikasi", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
