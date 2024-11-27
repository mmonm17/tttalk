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
import java.util.Collections;
import java.util.Comparator;
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
        CompletableFuture<Map<String, Integer>> future = new CompletableFuture<>();

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
                                future.complete(progress);
                            }
                        }
                    }
                });
        return future;
    }

    private CompletableFuture<ArrayList<Phoneme>> fetchPhonemes(List<DocumentReference> phonemeRefs, int levelNumber) {
        // Create a future to store the phonemes
        ArrayList<Phoneme> phonemes = new ArrayList<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Call fetchUserProgress asynchronously to get user progress
        CompletableFuture<Map<String, Integer>> userProgressFuture = fetchUserProgress();

        // Combine the two futures: user progress and phoneme fetch
        return userProgressFuture.thenCompose(userProgress -> {
            // For each phoneme reference, we fetch the corresponding document
            for (DocumentReference ref : phonemeRefs) {
                // Create a CompletableFuture for each phoneme fetch
                CompletableFuture<Void> phonemeFuture = new CompletableFuture<>();

                ref.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentPhoneme = task.getResult();
                        String[] documentId = documentPhoneme.getId().split("-");
                        String language = documentId[0].substring(0, 1);
                        String code = documentId[1];

                        // Extract the fields from the document
                        List<String> sentences = (List<String>) documentPhoneme.get("sentences");
                        int order = documentPhoneme.getLong("order").intValue();

                        // Use the user progress here (optional, depending on your needs)
                        Integer starCount = userProgress.get(language + "-" + levelNumber + "-" + code);
                        if (starCount == null) {
                            starCount = 0;
                        }

                        // Create a Phoneme object
                        Phoneme phoneme = new Phoneme(sentences, starCount, code, order);
                        phonemes.add(phoneme);

                        Log.d("TEST", "FETCHPHONEME DONE: " + documentPhoneme.getId());
                    } else {
                        Log.e("TEST", "Error fetching phoneme: " + task.getException());
                    }
                    // Complete the individual future
                    phonemeFuture.complete(null);
                });

                futures.add(phonemeFuture);
            }

            // Wait for all phoneme fetch operations to finish
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> {
                        phonemes.sort((p1, p2) -> Integer.compare(p1.getOrder(), p2.getOrder()));
                        return phonemes;
                    });  // Return the phonemes once all operations are complete
        }).exceptionally(e -> {
            Log.e("TEST", "Error fetching phonemes", e);
            return phonemes; // Return the phonemes (empty or partial) in case of error
        });
    }

    public static FirestoreDbHelper getInstance() {
        if (instance == null) {
            instance = new FirestoreDbHelper();
        }
        return instance;
    }

    public CompletableFuture<List<Level>> asyncFetchLevels() {
        CompletableFuture<List<Level>> future = new CompletableFuture<>();
        List<Level> levels = new ArrayList<>();

        db.collection("Levels").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<CompletableFuture<Void>> levelFutures = new ArrayList<>();

                for (DocumentSnapshot document : task.getResult()) {
                    int levelNumber = document.getLong("level_number").intValue();
                    int age = document.getLong("age").intValue();
                    int color = document.getLong("color").intValue();
                    String language = document.getString("language");
                    List<DocumentReference> phonemeRef = (List<DocumentReference>) document.get("phonemes");

                    // Fetch phonemes asynchronously
                    CompletableFuture<Void> levelFuture = fetchPhonemes(phonemeRef, levelNumber).thenAccept(phonemeArrayList -> {
                        Level level = new Level(levelNumber, age, color, language, phonemeArrayList);
                        levels.add(level);
                    });

                    levelFutures.add(levelFuture);
                }

                // Combine all level futures
                CompletableFuture.allOf(levelFutures.toArray(new CompletableFuture[0]))
                        .thenRun(() -> {
                            levels.sort((l1, l2) -> Integer.compare(l1.getLevelNumber(), l2.getLevelNumber()));
                            future.complete(levels);
                        })
                        .exceptionally(e -> {
                            Log.e("TEST", "Error fetching levels: " + e.getMessage(), e);
                            future.completeExceptionally(e);
                            return null;
                        });
            } else {
                Log.e("TEST", "Error fetching Levels collection: " + task.getException());
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    public CompletableFuture<Void> updateUserProgress(String levelCode, String phonemeCode, int starCount) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture<Map<String, Integer>> userProgressFuture = fetchUserProgress();

        return userProgressFuture.thenCompose(userProgress -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) {
                future.completeExceptionally(new IllegalStateException("User not authenticated"));
                return future;
            }

            String userID = user.getUid();
            String language = levelCode.substring(0, 1);
            String levelNumber = levelCode.split("-")[1];
            String progressKey = language + "-" + levelNumber + "-" + phonemeCode;

            Log.d("TEST", "IS PROGRESS KEY IN DB " + userProgress.containsKey(progressKey));
//            db.collection("UserProgress").document(userID)
//                    .update(progressKey, starCount)
//                    .addOnCompleteListener(task -> {
//                        Log.d("TEST", "IS TASK SUCCESSFUL " + task.isSuccessful());
//                        if (task.isSuccessful()) {
//                            Log.d("DATABASE", "Successfully updated progress for " + progressKey);
//                            future.complete(null);
//                        } else {
//                            Log.e("DATABASE", "Failed to update progress for " + progressKey, task.getException());
//                            future.completeExceptionally(task.getException());
//                        }
//                    });

            return future;
        });
    }
}
