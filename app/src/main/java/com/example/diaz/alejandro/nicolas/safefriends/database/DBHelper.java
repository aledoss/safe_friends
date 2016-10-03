package com.example.diaz.alejandro.nicolas.safefriends.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.diaz.alejandro.nicolas.safefriends.util.Constants;

import java.util.ArrayList;

/**
 * Created by Lenwe on 01/10/2016.
 */
public class DBHelper extends SQLiteOpenHelper implements Constants{

    public DBHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + PARADATABLE + " (id integer primary key, " + NAMECOLUMNPARADA + " text, " + NAMECOLUMNUSER + " text, " +
                LATITUDCOLUMNPARADA + " text, " + LONGITUDCOLUMNPARADA + " text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertParadaUser(ParadaUser paradaUser){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try{
            contentValues.put(NAMECOLUMNPARADA, paradaUser.getNameParada());
            contentValues.put(NAMECOLUMNUSER, paradaUser.getNameUser());
            contentValues.put(LATITUDCOLUMNPARADA, paradaUser.getLatitud());
            contentValues.put(LONGITUDCOLUMNPARADA, paradaUser.getLongitud());
            db.insert(PARADATABLE, null, contentValues);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ParadaUser> getAllParadaUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ParadaUser> listaParadaUser = new ArrayList<>();
        try{
            Cursor cur = db.rawQuery("select * from " + PARADATABLE, null);
            cur.moveToFirst();
            while(!cur.isAfterLast()){
                ParadaUser paradaUser = new ParadaUser();
                paradaUser.setId(cur.getInt(cur.getColumnIndex("id")));
                paradaUser.setNameParada(cur.getString(cur.getColumnIndex(NAMECOLUMNPARADA)));
                paradaUser.setNameUser(cur.getString(cur.getColumnIndex(NAMECOLUMNUSER)));
                paradaUser.setLatitud(cur.getString(cur.getColumnIndex(LATITUDCOLUMNPARADA)));
                paradaUser.setLongitud(cur.getString(cur.getColumnIndex(LONGITUDCOLUMNPARADA)));
                listaParadaUser.add(paradaUser);
                cur.moveToNext();
            }
            return listaParadaUser;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteParadaUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            return db.delete(PARADATABLE, "id = " + id, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
