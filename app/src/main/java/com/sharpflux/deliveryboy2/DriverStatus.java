package com.sharpflux.deliveryboy2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar Hatikat on 10 September 2019
 */
public class DriverStatus extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Status.db";
    public static final String TABLE_NAME = "DriverStatus";
    public static final String STATUS = "Status";



    public DriverStatus(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (STATUS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean StatusInsert(String Status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS,Status);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean UpdateStatus(String Status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS,Status);
        db.update(TABLE_NAME, contentValues, null,null);
        return true;
    }
    public String GetLastStatus(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME, null);
        String Id="";
        if(cursor.moveToLast()){
            Id = cursor.getString(0);
            //--get other cols values
        }
        return  Id;
    }


  /*  public Cursor FilterGetByFilterName(String FilterBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT CORESPONDENCEID FROM " + TABLE_NAME + " WHERE FILTERBY = ?";
        Cursor cursor =  db.rawQuery(query, new String[] {FilterBy});
        return cursor;
    }*/

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Integer DeleteRecord (String FilterBy, String CorrespondanceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "FILTERBY = ? AND CORESPONDENCEID = ?",new String[] {FilterBy,CorrespondanceId});
    }
    public Integer DeleteRecordAll () {
        SQLiteDatabase db = this.getWritableDatabase();
        return   db.delete(TABLE_NAME, null, null);

    }
    public String GETExist(String itemId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT ITEMID FROM " + TABLE_NAME + " WHERE ITEMID = ?";
        Cursor cursor =  db.rawQuery(query, new String[] {itemId});
        String Id="0";
        if(cursor.moveToFirst()){
            Id = cursor.getString(0);
        }
        return  Id;
    }
}