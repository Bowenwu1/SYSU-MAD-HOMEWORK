package com.pear.birthdaymemo.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pear.birthdaymemo.entity.Gift;

import java.util.ArrayList;

/**
 * Created by bowenwu on 2017/12/5.
 */

public class GiftDBManager {
    private static final String TABLE_NAME = "gift";
    private SQLiteDatabase db;

    public static final String KEY_NAME = "name";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_GIFT = "gift";
    public static final String KEY_ID = "_id";
    public GiftDBManager(Context context) {
        GiftDBHelper giftDBHelper = new GiftDBHelper(context);
        db = giftDBHelper.getWritableDatabase();
        // for test
        // this.addOneGift(new Gift("123", "456", "789"));
    }

    public void closeDB() {
        db.close();
    }

    public int addOneGift(Gift gift) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(GiftDBManager.KEY_NAME, gift.name);
        contentValues.put(GiftDBManager.KEY_BIRTHDAY, gift.birthday);
        contentValues.put(GiftDBManager.KEY_GIFT, gift.gift);

        return (int)db.insert(TABLE_NAME, null, contentValues);
    }

    public ArrayList<Gift> queryAllGifts() {
        ArrayList<Gift> gifts = new ArrayList<>();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " order by _id DESC", null);

        while (c.moveToNext()) {
            Gift gift = new Gift();
            gift.id = c.getInt(c.getColumnIndex(GiftDBManager.KEY_ID));
            gift.name = c.getString(c.getColumnIndex(GiftDBManager.KEY_NAME));
            gift.birthday = c.getString(c.getColumnIndex(GiftDBManager.KEY_BIRTHDAY));
            gift.gift = c.getString(c.getColumnIndex(GiftDBManager.KEY_GIFT));

            gifts.add(gift);
        }

        return gifts;
    }

    public ArrayList<Gift> queryGifts(String name) {
        ArrayList<Gift> gifts = new ArrayList<>();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where "
                                + GiftDBManager.KEY_NAME
                                + " = ?", new String[]{name});
        while (c.moveToNext()) {
            Gift gift = new Gift();
            gift.id = c.getInt(c.getColumnIndex(GiftDBManager.KEY_ID));
            gift.name = c.getString(c.getColumnIndex(GiftDBManager.KEY_NAME));
            gift.birthday = c.getString(c.getColumnIndex(GiftDBManager.KEY_BIRTHDAY));
            gift.gift = c.getString(c.getColumnIndex(GiftDBManager.KEY_GIFT));

            gifts.add(gift);
        }

        return gifts;
    }
    public void updateOneGift(int id, Gift gift) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(GiftDBManager.KEY_NAME, gift.name);
        contentValues.put(GiftDBManager.KEY_BIRTHDAY, gift.birthday);
        contentValues.put(GiftDBManager.KEY_GIFT, gift.gift);

        db.update(TABLE_NAME, contentValues, GiftDBManager.KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteGift(int id) {
        db.delete(TABLE_NAME, GiftDBManager.KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
