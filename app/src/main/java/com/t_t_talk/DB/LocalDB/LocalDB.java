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

import java.lang.reflect.Array;
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

    private Map<String, Integer> fetchUserProgress() {
        Map<String, Integer> progress = new HashMap<>();
        Cursor cursor = database.query(DBConstants.UserProgressTableConstants.TABLE_NAME, null, null, null, null, null, null);

        if (cursor == null) {
            return progress;
        }

        if(cursor.getCount() == 0) {
            cursor.close();
            return progress;
        }

        cursor.moveToFirst();
        do {
            progress.put(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.UserProgressTableConstants.COLNAME_ID)), cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.UserProgressTableConstants.COLNAME_STAR)));
        } while (cursor.moveToNext());

        return progress;
    }

    private ArrayList<Phoneme> fetchPhoneme(String levelCode) {
        Map<String, Integer> userProgress = fetchUserProgress();
        for (Map.Entry<String, Integer> entry : userProgress.entrySet()) {
            Log.d("TEST", entry.getKey() + " " + entry.getValue());
        }
        ArrayList<Phoneme> phonemes = new ArrayList<>();
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
            String code = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.PhonemeTableConstants.COLNAME_CODE));
            int starCount = 0;
            if(userProgress.containsKey(levelCode + "-" + code)) {
                starCount = userProgress.get(levelCode + "-" + code);
            }
            if(!phonemeMap.containsKey(code)) {
                phonemeMap.put(code, new Phoneme(
                    new ArrayList<>(),
                    starCount,
                    code,
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.PhonemeTableConstants.COLNAME_ORDER))
                ));
            }

            phonemeMap.get(code).addSentence(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.PhonemeTableConstants.COLNAME_SENTENCE)));
        } while (cursor.moveToNext());

        phonemes.addAll(phonemeMap.values());
        phonemes.sort((p1, p2) -> Integer.compare(p1.getOrder(), p2.getOrder()));

        cursor.close();
        return phonemes;
    }

    private int fetchPhonemeProgress(String levelCode, String phonemeCode) {
        Cursor cursor = database.query(DBConstants.UserProgressTableConstants.TABLE_NAME, new String[]{DBConstants.UserProgressTableConstants.COLNAME_STAR}, DBConstants.UserProgressTableConstants.COLNAME_ID + "=?", new String[]{ levelCode + "-" + phonemeCode }, null, null, null);
        if (cursor == null) {
            return 0;
        }
        Log.d("LocalDB", "Cursor Count: " + cursor.getCount());
        if(cursor.getCount() == 0) {
            cursor.close();
            return 0;
        }

        cursor.moveToFirst();
        int starCount = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.UserProgressTableConstants.COLNAME_STAR));
        cursor.close();
        return starCount;
    }

    public void open() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
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
            ArrayList<Phoneme> phonemes = fetchPhoneme(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_CODE)));
            levels.add(new Level(
                cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_NUMBER)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_AGE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_COLOR)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.LevelTableConstants.COLNAME_LANGUAGE)),
                phonemes
            ));
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

    public void insert(Phoneme phoneme, String level_code) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBConstants.PhonemeTableConstants.COLNAME_CODE, phoneme.getCode());
        contentValue.put(DBConstants.PhonemeTableConstants.COLNAME_LEVEL_CODE, level_code);
        contentValue.put(DBConstants.PhonemeTableConstants.COLNAME_ORDER, phoneme.getOrder());

        for(String sentence: phoneme.getSentences()) {
            contentValue.put(DBConstants.PhonemeTableConstants.COLNAME_SENTENCE, sentence);
            database.insert(DBConstants.PhonemeTableConstants.TABLE_NAME, null, contentValue);
            contentValue.remove(DBConstants.PhonemeTableConstants.COLNAME_SENTENCE);
        }

        this.insert(level_code + "-" + phoneme.getCode(), phoneme.getStarCount());
    }

    public void insert(String id, int starCount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBConstants.UserProgressTableConstants.COLNAME_ID, id);
        contentValue.put(DBConstants.UserProgressTableConstants.COLNAME_STAR, starCount);
        database.insert(DBConstants.UserProgressTableConstants.TABLE_NAME, null, contentValue);
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

    public void updatePhonemeProgress(String levelCode, String phonemeCode, int starCount) {
        int currentProgress = fetchPhonemeProgress(levelCode, phonemeCode);

        Log.d("LocalDB", "Current Progress: " + currentProgress);
        Log.d("LocalDB", "New Progress: " + starCount);
        if (starCount > currentProgress) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.UserProgressTableConstants.COLNAME_STAR, starCount);
//
            String whereClause = DBConstants.UserProgressTableConstants.COLNAME_ID + " = ?";
            database.update(DBConstants.UserProgressTableConstants.TABLE_NAME, contentValues, whereClause, new String[]{levelCode + "-" + phonemeCode});
        }
    }
}
