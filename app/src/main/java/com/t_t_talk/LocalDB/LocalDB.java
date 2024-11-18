package com.t_t_talk.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.t_t_talk.DataTypes.Level;
import com.t_t_talk.DataTypes.Phoneme;

public class LocalDB {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public LocalDB(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();//REMOVE ME WHEN DONE
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor fetchLevel() {
        String[] columns = new String[] { DBConstants.LevelTableConstants.COLNAME_CODE,
                DBConstants.LevelTableConstants.COLNAME_NUMBER,
                DBConstants.LevelTableConstants.COLNAME_LANGUAGE,
                DBConstants.LevelTableConstants.COLNAME_AGE
         };
        Cursor cursor = database.query(DBConstants.LevelTableConstants.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchPhoneme() {
        String[] columns = new String[] { DBConstants.PhonemeTableConstants.COLNAME_CODE,
                DBConstants.PhonemeTableConstants.COLNAME_SENTENCE,
                DBConstants.PhonemeTableConstants.COLNAME_STAR,
                DBConstants.PhonemeTableConstants.COLNAME_LEVEL_CODE
        };
        Cursor cursor = database.query(DBConstants.PhonemeTableConstants.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void insert(Level level) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_CODE, level.getCode());
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_NUMBER, level.getLevelNumber());
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_LANGUAGE, level.getLanguage());
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_AGE, level.getAge());
        database.insert(DBConstants.LevelTableConstants.TABLE_NAME, null, contentValue);

        for(Phoneme p: level.getPhonemeList()) {
            this.insert(p, level.getCode());
        }
    }

    public void insert(Phoneme phoneme, String levelCode) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBConstants.PhonemeTableConstants.COLNAME_CODE, phoneme.getCode());
        contentValue.put(DBConstants.PhonemeTableConstants.COLNAME_STAR, phoneme.getStarCount());
        contentValue.put(DBConstants.PhonemeTableConstants.COLNAME_LEVEL_CODE, levelCode);

        for(String sentence: phoneme.getSentences()) {
            contentValue.put(DBConstants.PhonemeTableConstants.COLNAME_SENTENCE, sentence);
            database.insert(DBConstants.PhonemeTableConstants.TABLE_NAME, null, contentValue);
            contentValue.remove(DBConstants.PhonemeTableConstants.COLNAME_SENTENCE);
        }
    }

    public void delete(Level level) {
        for(Phoneme p: level.getPhonemeList()) {
            this.delete(p, level.getCode());
        }

        database.delete(DBConstants.LevelTableConstants.TABLE_NAME, DBConstants.LevelTableConstants.COLNAME_CODE + "=?", new String[]{level.getCode()});
    }

    public void delete(Phoneme phoneme, String levelCode) {
        String whereClause = DBConstants.PhonemeTableConstants.COLNAME_CODE + " = ? AND " + DBConstants.PhonemeTableConstants.COLNAME_LEVEL_CODE + " = ?";
        database.delete(DBConstants.PhonemeTableConstants.TABLE_NAME, whereClause, new String[]{ phoneme.getCode(), levelCode });
    }

    public void reset() {
        dbHelper.reset(database);
    }
}
