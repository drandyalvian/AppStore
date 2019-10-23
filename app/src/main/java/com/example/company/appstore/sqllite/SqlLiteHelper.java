package com.example.company.appstore.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL(SQL_TABLE_USERS);
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
}
