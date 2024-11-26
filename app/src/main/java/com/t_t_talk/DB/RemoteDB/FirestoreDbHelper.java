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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class FirestoreDbHelper {
    private FirebaseFirestore db;
    private static FirestoreDbHelper instance;
    private FirebaseAuth mAuth;

    private FirestoreDbHelper() {
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    private CompletableFuture<Map<String, Integer>> fetchUserProgress() {
        return CompletableFuture.supplyAsync(() -> {
            CountDownLatch latch = new CountDownLatch(1);
            FirebaseUser user = mAuth.getCurrentUser();
            String userID = user.getUid();
            Map<String, Integer> progress = new HashMap<>();
            db.collection("UserProgress").document(userID)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()){
                                Map<String, Object> data = document.getData();
                                if (data != null) {
                                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();
                                        progress.put(key, ((Long) value).intValue());
                                    }
                                }
                            }
                        }
                        latch.countDown();
                    });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return progress;
        });
    }

    private CompletableFuture<ArrayList<Phoneme>> fetchPhonemes(List<DocumentReference> phonemeRef, int level_number) {
        return CompletableFuture.supplyAsync(() -> {
            CountDownLatch latch = new CountDownLatch(phonemeRef.size());
            ArrayList<Phoneme> phonemes = new ArrayList<>();
            fetchUserProgress().thenAccept(progress -> {
                for (DocumentReference ref : phonemeRef) {
                    CountDownLatch latch2 = new CountDownLatch(1);
                    ref.get().addOnCompleteListener(phonemeTask -> {
                        if (phonemeTask.isSuccessful() && phonemeTask.getResult() != null) {
                            DocumentSnapshot documentPhoneme = phonemeTask.getResult();
                            String[] documentId = documentPhoneme.getId().split("-");
                            String language = documentId[0].substring(0,1);
                            String code = documentId[1];
                            Integer starCount = progress.get(language + "-" + String.valueOf(level_number) + "-" + code);
                            if (starCount == null) {
                                starCount = 0;
                            }
                            List<String> sentences = (List<String>) documentPhoneme.get("sentences");
                            int order = documentPhoneme.getLong("order").intValue();
                            Phoneme phoneme = new Phoneme(sentences, starCount, code, order);
                            phonemes.add(phoneme);
                            latch.countDown();
                            latch2.countDown();
                        }
                    });
                    try {
                        latch2.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
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
                                int level_number = document.getLong("level_number").intValue();
                                int age = document.getLong("age").intValue();
                                int color = document.getLong("color").intValue();
                                String language = document.getString("language");
                                List<DocumentReference> phonemeRef = (List<DocumentReference>) document.get("phonemes");
                                if (!phonemeRef.isEmpty()) {
                                    fetchPhonemes(phonemeRef, level_number).thenAccept(phonemeArrayList -> {
                                        Level level = new Level(level_number, age, color, language, phonemeArrayList);
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
