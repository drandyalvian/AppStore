package com.example.company.appstore;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.company.appstore.sqllite.SqlLiteHelper;

public class Splash_Act extends AppCompatActivity {
    private SqlLiteHelper sqlLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        sqlLiteHelper = new SqlLiteHelper(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
                String cek = sqlLiteHelper.getSplash()+"";
                if ( cek.equals("open")){
                    Intent gotosign = new Intent(Splash_Act.this,StartAct.class);
                    startActivity(gotosign);
                    finish();
                }else{
                    Intent gotosign = new Intent(Splash_Act.this,LoginAct.class);
                    startActivity(gotosign);
                    finish();
                }
            }
        }, 1000); // 1000 ms = 1s
    }
}
