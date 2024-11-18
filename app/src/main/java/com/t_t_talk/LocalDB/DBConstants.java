package com.t_t_talk.LocalDB;

public class DBConstants {
    public static class PhonemeTableConstants {
        public static final String TABLE_NAME = "Phonemes";
        public static final String COLNAME_CODE = "phoneme_code";
        public static final String COLNAME_SENTENCE = "sentence";
        public static final String COLNAME_STAR = "star_count";
        public static final String COLNAME_LEVEL_CODE = "level_code";
    }

    public static class LevelTableConstants {
        public static final String TABLE_NAME = "Levels";
        public static final String COLNAME_CODE = "level_code";
        public static final String COLNAME_NUMBER = "level_number";
        public static final String COLNAME_LANGUAGE = "language";
        public static final String COLNAME_AGE = "age";
    }

    public static final String CREATE_PHONEME_TABLE_STATEMENT =
        "CREATE TABLE IF NOT EXISTS " + PhonemeTableConstants.TABLE_NAME + "(" +
        PhonemeTableConstants.COLNAME_CODE + " TEXT," +
        PhonemeTableConstants.COLNAME_SENTENCE + " TEXT," +
        PhonemeTableConstants.COLNAME_LEVEL_CODE + " TEXT," +
        PhonemeTableConstants.COLNAME_STAR + " INTEGER)";

    public static final String CREATE_LEVEL_TABLE_STATEMENT =
        "CREATE TABLE IF NOT EXISTS " + LevelTableConstants.TABLE_NAME + "(" +
        LevelTableConstants.COLNAME_CODE + " TEXT PRIMARY KEY," +
        LevelTableConstants.COLNAME_NUMBER + " INTEGER," +
        LevelTableConstants.COLNAME_LANGUAGE + " TEXT," +
        LevelTableConstants.COLNAME_AGE + " INTEGER)";

    public static final String DROP_PHONEME_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + PhonemeTableConstants.TABLE_NAME;
    public static final String DROP_LEVEL_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + LevelTableConstants.TABLE_NAME;
}
