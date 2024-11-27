package com.t_t_talk.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.t_t_talk.DB.LocalDB.LocalDB;
import com.t_t_talk.DB.Models.Level;
import com.t_t_talk.DB.Models.Phoneme;
import com.t_t_talk.DB.RemoteDB.FirestoreDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class AppDatabase {
    private LocalDB localDB;
    private FirestoreDbHelper remoteDB;
    private Context context;
    private AppDatabase instance;
    
    public AppDatabase(Context context) {
        this.context = context;
        this.localDB = new LocalDB(context);
        this.remoteDB = FirestoreDbHelper.getInstance();
        this.instance = this;
    }

    private void setLocalDBVersion(int version) {
        SharedPreferences refs = context.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = refs.edit();
        editor.putInt("Version", version);
        editor.apply();
    }

    private int getLocalDBVersion() {
        SharedPreferences refs = context.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
        return refs.getInt("Version", 0);
    }

    private int getFirestoreVersion() {
        AtomicInteger version = new AtomicInteger(0);
        remoteDB.getVersion().thenAccept(version::set);
        return version.get();
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Network activeNetwork = connectivityManager.getActiveNetwork();

        if (activeNetwork != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);

            if (networkCapabilities != null) {
                return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            }
        }

        return false;
    }

    public void updatePhonemeProgress(String levelCode, String phonemeCode, int starCount) {
        if (isOnline()) {
            remoteDB.updateUserProgress(levelCode, phonemeCode, starCount)
                .thenRun(() -> {
                    localDB.open();
                    localDB.updatePhonemeProgress(levelCode, phonemeCode, starCount);
                    localDB.close();
                })
                .exceptionally(e -> {
                    return null;
                });
        } else {
            localDB.open();
            localDB.updatePhonemeProgress(levelCode, phonemeCode, starCount);
            localDB.close();
            setLocalDBVersion(getLocalDBVersion() + 1);
        }
    }

    public CompletableFuture<List<Level>> fetchLevels() {
        int localDBVersion = getLocalDBVersion();
        int remoteDBVersion = getFirestoreVersion();

        if (isOnline()) {
            Log.d("TEST", "RV " + remoteDBVersion + " LV " + localDBVersion);
            if ((remoteDBVersion > localDBVersion) || (remoteDBVersion == 0 && localDBVersion == 0)) {
                // Fetch levels from the remote database
                return remoteDB.asyncFetchLevels().thenCompose(levelsList -> {
                    // Open local database and save the fetched levels
                    localDB.open();
                    localDB.reset();
                    for (Level level : levelsList) {
                        localDB.insert(level);
                    }
                    localDB.close();

                    if (remoteDBVersion == 0 && localDBVersion == 0) {
                        remoteDB.setVersion(1);
                        setLocalDBVersion(1);
                    } else {
                        setLocalDBVersion(remoteDBVersion);
                    }
                    // Return the fetched levels
                    return CompletableFuture.completedFuture(levelsList);
                }).exceptionally(e -> {
                    Log.e("FETCH_LEVELS", "Error fetching levels from remote DB", e);
                    return new ArrayList<>(); // Return an empty list on failure
                });
            } else {
                localDB.open();
                List<Level> levels = localDB.fetchLevels();
                localDB.close();
                if (remoteDBVersion < localDBVersion) {
                    for (Level level : levels) {
                        for (Phoneme phoneme : level.getPhonemeList()) {
                            remoteDB.updateUserProgress(level.getCode(), phoneme.getCode(), phoneme.getStarCount());
                        }
                    }
                    remoteDB.setVersion(localDBVersion);
                }

                return CompletableFuture.completedFuture(levels);
            }
        }

        // Fetch levels from the local database if offline
        return CompletableFuture.supplyAsync(() -> {
            localDB.open();
            List<Level> levels = localDB.fetchLevels();
            localDB.close();
            return levels;
        }).exceptionally(e -> {
            Log.e("FETCH_LEVELS", "Error fetching levels from local DB", e);
            return new ArrayList<>(); // Return an empty list on failure
        });
    }

    public ArrayList<Phoneme> localFetchPhonemes(String levelCode) {
        localDB.open();
        ArrayList<Phoneme> phonemes = localDB.fetchPhoneme(levelCode);
        localDB.close();
        return phonemes;
    }

    public CompletableFuture<Void> createRemoteUser() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        remoteDB.createRemoteUser().thenRun(() -> {
            future.complete(null);
        }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<Void> createRemoteUserVersion() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        remoteDB.createRemoteUserVersion().thenRun(() -> {
            future.complete(null);
        }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }
}
