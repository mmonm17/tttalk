package com.t_t_talk;

import android.content.Context;
import android.graphics.Color;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.t_t_talk.DB.AppDatabase;
import com.t_t_talk.DB.Models.Level;
import com.t_t_talk.DB.Models.Phoneme;
import com.t_t_talk.DB.LocalDB.LocalDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class LocalDBTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    AppDatabase db = new AppDatabase(appContext);
    LocalDB ldb = db.getLocalDB();

    @Test
    public void level_insertion() {
        ArrayList<String> sentences = new ArrayList<String>(Arrays.asList(new String[]{
                "Sam the cat saw a snake in the grass",
                "The snake slid fast",
                "Sam sat and watched the snake go by",
                "The snake said, \"sss,\" and Sam said, \"Hi!\""
        }));

        ArrayList<Phoneme> phonemes = new ArrayList<>();
        phonemes.add(new Phoneme(sentences, 2, "S"));
        phonemes.add(new Phoneme(sentences, 1, "A"));
        phonemes.add(new Phoneme(sentences, 3, "T"));
        phonemes.add(new Phoneme(sentences, 0, "P"));

        Level l1 = new Level(1, 3, Color.rgb(249, 222, 104), "English", phonemes);
        Level l2 = new Level(2, 5, Color.rgb(249, 222, 104), "English", phonemes);

        ldb.open();
        ldb.reset();

        ldb.insert(l1);
        phonemes.add(new Phoneme(sentences, 0, "E"));
        ldb.insert(l2);

        AtomicReference<List<Level>> levels = null;
        db.fetchLevels().thenAccept(listLevels -> {
            levels.set(listLevels);
        });

        assertEquals(2, levels.get().size());

        for(Level level: levels.get()) {
            if(level.getLevelNumber() == 1) {
                assertEquals(4, level.getPhonemeList().size());
            } else {
                assertEquals(5, level.getPhonemeList().size());
            }
        }

        ldb.reset();
        ldb.close();
    }
}