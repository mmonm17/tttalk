package com.t_t_talk.DB.LocalDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper  {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TTTalkLocal.db";

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBConstants.CREATE_USER_TABLE_STATEMENT);
        sqLiteDatabase.execSQL(DBConstants.CREATE_PHONEME_TABLE_STATEMENT);
        sqLiteDatabase.execSQL(DBConstants.CREATE_LEVEL_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DBConstants.DROP_USER_TABLE_STATEMENT);
        sqLiteDatabase.execSQL(DBConstants.DROP_PHONEME_TABLE_STATEMENT);
        sqLiteDatabase.execSQL(DBConstants.DROP_LEVEL_TABLE_STATEMENT);
        onCreate(sqLiteDatabase);
    }

    public void reset(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBConstants.DROP_USER_TABLE_STATEMENT);
        sqLiteDatabase.execSQL(DBConstants.DROP_PHONEME_TABLE_STATEMENT);
        sqLiteDatabase.execSQL(DBConstants.DROP_LEVEL_TABLE_STATEMENT);
        onCreate(sqLiteDatabase);
    }
}
