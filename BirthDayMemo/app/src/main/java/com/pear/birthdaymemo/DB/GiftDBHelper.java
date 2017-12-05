package com.pear.birthdaymemo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bowenwu on 2017/12/5.
 */

public class GiftDBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "gift.db";
    private static final String TABLE_NAME  = "gift";
    private static final int DB_VERSION = 4;


    public GiftDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public GiftDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME
                                + "(_id integer primary key ,"
                                + "name text,"
                                + "birthday text,"
                                + "gift text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
