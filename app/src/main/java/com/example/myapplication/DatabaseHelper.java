package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Luokka sisältää tallennuksen ominaisuuden
 * @author Jeremia Kekkonen
 * @version 1.0
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * Alusta tietokannan nimi ja taulun nimi
     * */
    private static final String DATABASE_NAME = "Tiedot";
    private static final String TABLE_NAME = "Suoritukset";
    private static final String ID = "id";
    private static final String AIKA = "aika";
    private static final String ASKELEET = "askeleet";
    private static final String MATKA = "matka";
    private static final String PAIVA = "paiva";
    private Suoritus suoritus;

    DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * Tee taulu
         * */
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AIKA + " TEXT,"
                + ASKELEET + " TEXT,"
                + MATKA + " TEXT,"
                + PAIVA + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * Tuhoa vanha taulu jos olemassa
         * */
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void LisaaSuoritus(String aika, String askeleet, String matka, String paiva ){
        /**
         * Hanki WriteAble tietokanta
         * */
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        /**
         * Luo ContentValues
         * */
        ContentValues contentValues = new ContentValues();
        contentValues.put(AIKA,aika);
        contentValues.put(ASKELEET,askeleet);
        contentValues.put(MATKA,matka);
        contentValues.put(PAIVA,paiva);
        /**
         * Lisää arvot tietokantaan
         * */
        sqLiteDatabase.insert(TABLE_NAME, null,contentValues);
    }

    /**
     *
     * @return palauttaa kaikki suoritukset arraylistana
     */
    public ArrayList<Suoritus> haeSuoritukset(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Suoritus> suoritusLista = new ArrayList<>();
        String query = "SELECT aika, askeleet, matka, paiva FROM "+ "Suoritukset";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            String aika = cursor.getString(cursor.getColumnIndexOrThrow(AIKA));
            String askeleet = cursor.getString(cursor.getColumnIndexOrThrow(ASKELEET));
            String matka = cursor.getString(cursor.getColumnIndexOrThrow(MATKA));
            String paiva = cursor.getString(cursor.getColumnIndexOrThrow(PAIVA));
            suoritus = new Suoritus(aika,askeleet,matka, paiva);
            suoritusLista.add(suoritus);
        }
        return  suoritusLista;
    }
}
