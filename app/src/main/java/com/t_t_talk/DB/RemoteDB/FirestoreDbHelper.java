package com.t_t_talk.DB.RemoteDB;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.t_t_talk.DB.Models.Level;
import com.t_t_talk.DB.Models.Phoneme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class FirestoreDbHelper {
    private FirebaseFirestore db;
    private static FirestoreDbHelper instance;
    private FirebaseAuth mAuth;
    private int current_level;

    private FirestoreDbHelper() {
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    private CompletableFuture<Integer> fetchUserProgress(String code) {
        return CompletableFuture.supplyAsync(() -> {
            CountDownLatch latch = new CountDownLatch(1);
            FirebaseUser user = mAuth.getCurrentUser();
            String userID = user.getUid();
            AtomicReference<Integer> starCount = new AtomicReference<>(0);
            db.collection("UserProgress").document(userID + "-" + this.current_level + "-" + code)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.getLong("starCount") != null)
                                starCount.set(document.getLong("starCount").intValue());
                        }
                        latch.countDown();
                    });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return starCount.get();
        });
    }

    private CompletableFuture<ArrayList<Phoneme>> fetchPhonemes(List<DocumentReference> phonemeRef) {
        return CompletableFuture.supplyAsync(() -> {
            CountDownLatch latch = new CountDownLatch(phonemeRef.size());
            ArrayList<Phoneme> phonemes = new ArrayList<>();
            for (DocumentReference ref : phonemeRef) {
                CountDownLatch latch2 = new CountDownLatch(1);
                ref.get().addOnCompleteListener(phonemeTask -> {
                    if (phonemeTask.isSuccessful() && phonemeTask.getResult() != null) {
                        DocumentSnapshot documentPhoneme = phonemeTask.getResult();
                        List<String> sentences = (List<String>) documentPhoneme.get("sentences");
                        fetchUserProgress(documentPhoneme.getId()).thenAccept(starCount -> {
                            latch2.countDown();
                            Phoneme phoneme = new Phoneme(sentences, starCount, documentPhoneme.getId());
                            phonemes.add(phoneme);
                            latch.countDown();
                        });
                    }
                });
                try {
                    latch2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return phonemes;
        });
    }

    public static FirestoreDbHelper getInstance() {
        if (instance == null) {
            instance = new FirestoreDbHelper();
        }
        return instance;
    }

    public CompletableFuture<List<Level>> asyncFetchLevels() {
        return CompletableFuture.supplyAsync(() -> {
            List<Level> levels = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(1);
            db.collection("Levels").get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot document : task.getResult()) {
                                this.current_level = document.getLong("level_number").intValue();
                                int age = document.getLong("age").intValue();
                                int color = document.getLong("color").intValue();
                                String language = document.getString("language");
                                List<DocumentReference> phonemeRef = (List<DocumentReference>) document.get("phonemes");
                                if (!phonemeRef.isEmpty()) {
                                    fetchPhonemes(phonemeRef).thenAccept(phonemeArrayList -> {
                                        Level level = new Level(this.current_level, age, color, language, phonemeArrayList);
                                        levels.add(level);
                                        latch.countDown();
                                    });
                                }
                            }
                        }
                    });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return levels;
        });
    }
}
