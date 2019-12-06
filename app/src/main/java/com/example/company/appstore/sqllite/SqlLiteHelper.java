package com.example.company.appstore.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqlLiteHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "TokoAndhika.db";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //TABLE NAME
    public static final String TABLE_Name= "setting";

    //TABLE USERS COLUMNS
    //ID COLUMN @primaryKey
    public static final String KEY_ID = "id";
    public static final String STAT = "stat_splash";

    SQLiteDatabase db;

    public SqlLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_Name
                + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + STAT + " Text"
                + " ) ";
        String SQL_TABLE_GAJI = " CREATE TABLE gaji"
                + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nama" + " Text, "
                + "gaji_total" + " Integer, "
                + "total_masuk" + " Integer, "
                + "pinjaman" + " Integer, "
                + "cicilan" + " Integer, "
                + "cicilan_ke" + " Integer, "
                + "komisi" + " Integer, "
                + "gaji_pokok" + " Integer, "
                + "uang_makan" + " Integer, "
                + "gaji_diterima" + " Integer, "
                + "nama_cabang" + " Text, "
                + "total_makan" + " Integer "
                + " ) ";
        db.execSQL(SQL_TABLE_USERS);
        db.execSQL(SQL_TABLE_GAJI);
        String sql = "INSERT INTO "+TABLE_Name+" ("+KEY_ID+", "+STAT+") VALUES (1, 'open');";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_Name);
        onCreate(db);
    }

    public String getSplash(){
        SQLiteDatabase db = this.getReadableDatabase();
        String stat = "";
        Cursor myCursor = db.rawQuery("SELECT * FROM setting WHERE id = '" + 1 + "'",null);
        myCursor.moveToFirst();
        if (myCursor.getCount()>0) {
            myCursor.moveToPosition(0);
            stat = myCursor.getString(1).toString();
        }
        return stat;
    }
    public int updateSplash(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STAT, "close");

        // updating row
        return db.update(TABLE_Name, values, KEY_ID + "="+1,null);
    }

    public void insertToGaji(String nama, Double gajiTotal, int totalMasuk, Double pinjman, Double cicilan, int cicilanKe, Double komisi, Double gajiPokok, Double uangMakan, Double gajiDiterima, String namaCabang, Double totalUangMakan) {
        SQLiteDatabase db = this.getWritableDatabase();
        String cek = getWhere(nama);
        String sql ="";
        if (cek.equals(nama)) {
            updateGaji(nama, gajiTotal, totalMasuk, pinjman, 0.0,0, komisi, gajiPokok, uangMakan, gajiDiterima, namaCabang, totalUangMakan);
        }else{
            sql = "INSERT INTO " + "gaji" + " (nama, gaji_total, total_masuk, pinjaman, cicilan, cicilan_ke, komisi, gaji_pokok, uang_makan, gaji_diterima, nama_cabang, total_makan) VALUES ('" + nama + "', " + gajiTotal + ", " + totalMasuk + ", " + pinjman + ", " + cicilan + ", " + cicilanKe + ", " + komisi + ", " + gajiPokok + ", " + uangMakan + ", " + gajiDiterima + ", '" + namaCabang + "', " + totalUangMakan +");";
            db.execSQL(sql);
        }

    }

    public String getWhere(String nama){
        SQLiteDatabase db = this.getReadableDatabase();
        String stat = "";
        Cursor myCursor = db.rawQuery("SELECT * FROM gaji WHERE nama = '" + nama + "'",null);
        myCursor.moveToFirst();
        if (myCursor.getCount()>0) {
            myCursor.moveToPosition(0);
            stat = myCursor.getString(1).toString();
        }
        return stat;
    }

    public int updateGaji(String nama, Double gajiTotal, int totalMasuk, Double pinjman, Double cicilan, int cicilanKe, Double komisi, Double gajiPokok, Double uangMakan, Double gajiDiterima, String namaCabang, Double totalUangMakan){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("gaji_total", gajiTotal);
        values.put("total_masuk", totalMasuk);
        values.put("pinjaman", pinjman);
        values.put("cicilan", cicilan);
        values.put("cicilan_ke", cicilanKe);
        values.put("komisi", komisi);
        values.put("gaji_pokok", gajiPokok);
        values.put("uang_makan", uangMakan);
        values.put("gaji_diterima", gajiDiterima);
        values.put("nama_cabang", namaCabang);
        values.put("total_makan", totalUangMakan);


        // updating row
        return db.update("gaji", values, "nama" + "= '"+nama+"'",null);
    }
}
