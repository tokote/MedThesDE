package com.de_thes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.InputStream;

/**
 * Created by tom on 27.09.16.
 */
public class MyDB_OpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "DIMDI";
    private static final int DATABASE_VERSION =1;
    public  static final String TABLE_ENGLISH_NAME = "EnglishTable";
    public static final String TABLE_GERMAN_NAME = "GermanTable";
    public static final String TEXT_COLUMN_NAME = "entryText";
    public static final String DNUM_COLUMN_NAME = "D_sum";
    public static final String ID_COLUMN_NAME = "_id";

    public SQLiteDatabase getReadableDB() {
        if (readableDB == null) {
            readableDB = getReadableDatabase();
        }
        return readableDB;
    }

    SQLiteDatabase readableDB = null;

    public MyDB_OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertEnglishRow(String text, String D_num) {
        insertRow(text, D_num, TABLE_ENGLISH_NAME);
    }

    public void insertGermanRow(String text, String D_num) {
        insertRow(text, D_num, TABLE_GERMAN_NAME);
    }

    public void insertRow(String text,String D_num, String TableName) {
        ContentValues values = new ContentValues();

        values.put(TEXT_COLUMN_NAME, text);
        values.put(DNUM_COLUMN_NAME, D_num);
        long newRowId = this.getWritableDatabase().insert(TableName, null, values);
    }

    public Cursor EnglishCursor(String prefix) {
        return getListViewCursor(prefix, true);
    }

    public Cursor GermanCursor(String prefix) {
        return getListViewCursor(prefix, false);
    }

    public Cursor getListViewCursor(String prefix, boolean isEn) {
        String table = isEn ? TABLE_ENGLISH_NAME : TABLE_GERMAN_NAME;
        String[] columns = {ID_COLUMN_NAME, TEXT_COLUMN_NAME, DNUM_COLUMN_NAME};

        String selection = null;
        if (prefix == "") {
            prefix = "b";
        }
        selection = TEXT_COLUMN_NAME + " LIKE '" + prefix + "%'";
        String orderBy = null;//TEXT_COLUMN_NAME;
        return getReadableDB().query(table,columns,selection,null,null,null,orderBy,null);
    }


}
