package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Alusta tietokannan nimi ja taulun nimi
    private static final String DATABASE_NAME = "database_name";
    private static final String TABLE_NAME = "table_name";

    DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tee taulu
        String createTable = "create table table_name(id INTEGER PRIMARY KEY,txt TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Tuhoa vanha taulu jos olemassa
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean addText(String text){
        // Hanki WriteAble tietokanta
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // Luo ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put("txt",text);
        // Lisää arvot tietokantaan
        sqLiteDatabase.insert(TABLE_NAME, null,contentValues);
        return true;
    }

    public ArrayList getAllText(){
        // Hanki luettava tietokanta
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        // Rakenna Cursor valitakseen kaikki arvot
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME
                ,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            arrayList.add(cursor.getString(cursor.getColumnIndex("txt")));
            cursor.moveToNext();
        }
        return arrayList;
    }
}
