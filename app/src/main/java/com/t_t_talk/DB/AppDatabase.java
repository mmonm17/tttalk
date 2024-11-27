package com.t_t_talk.DB;

import android.content.Context;
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
                    Log.e("UPDATE", "Failed to update progress", e);
                    return null;
                });
        } else {
            localDB.open();
            localDB.updatePhonemeProgress(levelCode, phonemeCode, starCount);
            localDB.close();
        }
    }

    public CompletableFuture<List<Level>> fetchLevels() {
        if (isOnline()) {
            // Fetch levels from the remote database
            return remoteDB.asyncFetchLevels().thenCompose(levelsList -> {
                // Open local database and save the fetched levels
                localDB.open();
                localDB.reset();
                for (Level level : levelsList) {
                    localDB.insert(level);
                }
                localDB.close();
                // Return the fetched levels
                return CompletableFuture.completedFuture(levelsList);
            }).exceptionally(e -> {
                Log.e("FETCH_LEVELS", "Error fetching levels from remote DB", e);
                return new ArrayList<>(); // Return an empty list on failure
            });
        } else {
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
    }

    public ArrayList<Phoneme> localFetchPhonemes(String levelCode) {
        localDB.open();
        ArrayList<Phoneme> phonemes = localDB.fetchPhoneme(levelCode);
        localDB.close();
        return phonemes;
    }
}
