package com.t_t_talk;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.t_t_talk.DataTypes.Level;
import com.t_t_talk.DataTypes.Phoneme;
import com.t_t_talk.LocalDB.DBConstants;
import com.t_t_talk.LocalDB.LocalDB;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class LocalDBTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    LocalDB db = new LocalDB(appContext);

    @Test
    public void level_insertion() {
        String[] sentences = new String[]{
                "Sam the cat saw a snake in the grass",
                "The snake slid fast",
                "Sam sat and watched the snake go by",
                "The snake said, \"sss,\" and Sam said, \"Hi!\""
        };

        ArrayList<Phoneme> phonemes = new ArrayList<>();
        phonemes.add(new Phoneme(sentences, 2, "S"));
        phonemes.add(new Phoneme(sentences, 1, "A"));
        phonemes.add(new Phoneme(sentences, 3, "T"));
        phonemes.add(new Phoneme(sentences, 0, "P"));

        Level l = new Level(1, 3, Color.rgb(249, 222, 104), "English", phonemes);

        db.open();

        db.insert(l);

        Cursor cursor = db.fetchLevel();

        assertEquals(1, cursor.getCount());

        cursor.close();

        cursor = db.fetchPhoneme();

        assertEquals(16, cursor.getCount());

        cursor.close();

        db.reset();
        db.close();
    }

    @Test
    public void phoneme_insertion() {
        String[] sentences = new String[]{
                "Sam the cat saw a snake in the grass",
                "The snake slid fast",
                "Sam sat and watched the snake go by",
                "The snake said, \"sss,\" and Sam said, \"Hi!\""
        };

        Phoneme p = new Phoneme(sentences, 1, "S");

        db.open();

        db.insert(p, "TEST");

        Cursor cursor = db.fetchPhoneme();

        assertEquals(4, cursor.getCount());

        cursor.close();

        db.reset();
        db.close();
    }

    @Test
    public void level_deletion() {
        String[] sentences = new String[]{
                "Sam the cat saw a snake in the grass",
                "The snake slid fast",
                "Sam sat and watched the snake go by",
                "The snake said, \"sss,\" and Sam said, \"Hi!\""
        };

        ArrayList<Phoneme> phonemes = new ArrayList<>();
        phonemes.add(new Phoneme(sentences, 2, "S"));
        phonemes.add(new Phoneme(sentences, 1, "A"));
        phonemes.add(new Phoneme(sentences, 3, "T"));
        phonemes.add(new Phoneme(sentences, 0, "P"));

        Level l1 = new Level(1, 3, Color.rgb(249, 222, 104), "English", phonemes);
        Level l2 = new Level(2, 3, Color.rgb(249, 222, 104), "English", phonemes);

        db.open();

        db.insert(l1);
        db.insert(l2);
        db.delete(l1);

        Cursor cursor = db.fetchLevel();

        assertEquals(1, cursor.getCount());

        cursor.close();

        cursor = db.fetchPhoneme();

        assertEquals(16, cursor.getCount());

        cursor.close();

        db.reset();
        db.close();
    }

    @Test
    public void phoneme_deletion() {
        String[] sentences = new String[]{
                "Sam the cat saw a snake in the grass",
                "The snake slid fast",
                "Sam sat and watched the snake go by",
                "The snake said, \"sss,\" and Sam said, \"Hi!\""
        };

        Phoneme p = new Phoneme(sentences, 1, "S");

        db.open();

        db.insert(p, "TEST");
        db.delete(p, "TEST");

        Cursor cursor = db.fetchPhoneme();

        assertEquals(0, cursor.getCount());

        cursor.close();

        db.reset();
        db.close();
    }

}