package com.t_t_talk.DB.LocalDB;

public class DBConstants {
    public static class PhonemeTableConstants {
        public static final String TABLE_NAME = "Phonemes";
        public static final String COLNAME_CODE = "phoneme_code";
        public static final String COLNAME_SENTENCE = "sentence";
        public static final String COLNAME_LEVEL_CODE = "level_code";
        public static final String COLNAME_ORDER = "phoneme_order";
    }

    public static class LevelTableConstants {
        public static final String TABLE_NAME = "Levels";
        public static final String COLNAME_CODE = "level_code";
        public static final String COLNAME_NUMBER = "level_number";
        public static final String COLNAME_LANGUAGE = "language";
        public static final String COLNAME_AGE = "age";
        public static final String COLNAME_COLOR = "color";
    }

    public static class UserProgressTableConstants {
        public static final String TABLE_NAME = "UserProgress";
        public static final String COLNAME_ID = "id";
        public static final String COLNAME_STAR = "star_count";
    }

    public static final String CREATE_PHONEME_TABLE_STATEMENT =
        "CREATE TABLE IF NOT EXISTS " + PhonemeTableConstants.TABLE_NAME + "(" +
        PhonemeTableConstants.COLNAME_CODE + " TEXT," +
        PhonemeTableConstants.COLNAME_SENTENCE + " TEXT," +
        PhonemeTableConstants.COLNAME_LEVEL_CODE + " INTEGER," +
        PhonemeTableConstants.COLNAME_ORDER + " INTEGER)";

    public static final String CREATE_LEVEL_TABLE_STATEMENT =
        "CREATE TABLE IF NOT EXISTS " + LevelTableConstants.TABLE_NAME + "(" +
        LevelTableConstants.COLNAME_CODE + " TEXT," +
        LevelTableConstants.COLNAME_NUMBER + " INTEGER," +
        LevelTableConstants.COLNAME_COLOR + " INTEGER," +
        LevelTableConstants.COLNAME_LANGUAGE + " TEXT," +
        LevelTableConstants.COLNAME_AGE + " INTEGER)";

    public static final String CREATE_USER_PROGRESS_TABLE_STATEMENT =
        "CREATE TABLE IF NOT EXISTS " + UserProgressTableConstants.TABLE_NAME + "(" +
        UserProgressTableConstants.COLNAME_ID + " TEXT," +
        UserProgressTableConstants.COLNAME_STAR + " INTEGER)";

    public static final String DROP_PHONEME_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + PhonemeTableConstants.TABLE_NAME;
    public static final String DROP_LEVEL_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + LevelTableConstants.TABLE_NAME;
    public static final String DROP_USER_PROGRESS_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + UserProgressTableConstants.TABLE_NAME;
}
