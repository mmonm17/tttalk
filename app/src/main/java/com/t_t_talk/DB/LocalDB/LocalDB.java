package com.t_t_talk.DB.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.t_t_talk.DB.Models.Level;
import com.t_t_talk.DB.Models.Phoneme;
import com.t_t_talk.DB.RemoteDB.FirestoreDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalDB {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public LocalDB(Context context) {
        this.context = context;
    }

    private void clearCachedLevels(List<Level> levels) {
        dbHelper.reset(database);
    }

    private ArrayList<Phoneme> fetchPhoneme(String levelCode) {
        ArrayList<Phoneme> phonemes = new ArrayList<>();
        String code;
        Map<String, Phoneme> phonemeMap = new HashMap<>();
        Cursor cursor = database.query(DBConstants.PhonemeTableConstants.TABLE_NAME, null, DBConstants.PhonemeTableConstants.COLNAME_LEVEL_CODE + "=?", new String[]{ levelCode }, null, null, null);

        if (cursor == null) {
            return phonemes;
        }

        if(cursor.getCount() == 0) {
            cursor.close();
            return phonemes;
        }

        cursor.moveToFirst();
        do {
            code = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.PhonemeTableConstants.COLNAME_CODE));
            if(!phonemeMap.containsKey(code)) {
                phonemeMap.put(code, new Phoneme(
                        new ArrayList<>(),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.PhonemeTableConstants.COLNAME_STAR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.PhonemeTableConstants.COLNAME_CODE))
                ));
            }

            phonemeMap.get(code).addSentence(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.PhonemeTableConstants.COLNAME_SENTENCE)));
        } while (cursor.moveToNext());

        phonemes.addAll(phonemeMap.values());

        cursor.close();
        return phonemes;
    }

    public void open() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();//REMOVE ME WHEN DONE
    }

    public void close() {
        dbHelper.close();
    }

    public List<Level> fetchLevels() {
        List<Level> levels = new ArrayList<Level>();
        Cursor cursor = database.query(DBConstants.LevelTableConstants.TABLE_NAME, null, null, null, null, null, null);

        if (cursor == null) {
            return levels;
        }

        if(cursor.getCount() == 0) {
            cursor.close();
            return levels;
        }

        cursor.moveToFirst();
        do {
            levels.add(new Level(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_NUMBER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_AGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_COLOR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_LANGUAGE)),
                    fetchPhoneme(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_CODE)))
            ));
            Log.d("TAG", String.valueOf(levels.get(levels.size() - 1).getPhonemeList().size()));
        } while (cursor.moveToNext());

        cursor.close();
        return levels;
    }

    public void insert(Level level) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_CODE, level.getCode());
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_NUMBER, level.getLevelNumber());
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_LANGUAGE, level.getLanguage());
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_AGE, level.getAge());
        contentValue.put(DBConstants.LevelTableConstants.COLNAME_COLOR, level.getColor());
        database.insert(DBConstants.LevelTableConstants.TABLE_NAME, null, contentValue);

        for(Phoneme p: level.getPhonemeList()) {
            this.insert(p, level.getCode());
        }
    }

    public void insert(long userId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.UserTableConstants.COLNAME_UID, userId);
        database.insert(DBConstants.UserTableConstants.TABLE_NAME, null, contentValues);
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

    public void delete(long userId) {
        database.delete(DBConstants.UserTableConstants.TABLE_NAME, DBConstants.UserTableConstants.COLNAME_UID + "=?", new String[]{String.valueOf(userId)});
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

    //REMOVE THIS IN PROD
    public void reset() {
        dbHelper.reset(database);
    }
}
