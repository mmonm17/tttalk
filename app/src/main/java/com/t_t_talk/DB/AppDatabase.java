package com.t_t_talk.DB;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.t_t_talk.DB.LocalDB.LocalDB;
import com.t_t_talk.DB.Models.Level;
import com.t_t_talk.DB.RemoteDB.FirestoreDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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

    public void updatePhonemeProgress(int levelCode, String phonemeCode, int starCount) {
        if (isOnline()) {
            Log.d("AppDatabase", "Updating phoneme progress in remote database");
            localDB.open();
            localDB.updatePhonemeProgress(levelCode, phonemeCode, starCount);
            localDB.close();
        } else {
            Log.d("AppDatabase", "is offline, updating phoneme progress in local database");
        }
    }

    public interface LevelsCallback {
        void onLevelsFetched(List<Level> levels);
    }

    public void fetchLevels(LevelsCallback callback) {
        AtomicReference<List<Level>> levels = new AtomicReference<>(new ArrayList<>());

        if (isOnline()) {
            remoteDB.asyncFetchLevels().thenAccept(levelsList -> {
                levels.set(levelsList);
                localDB.open();
                for (Level level : levelsList) {
                    localDB.insert(level);
                }
                localDB.close();

                callback.onLevelsFetched(levelsList);  // Notify when levels are fetched
            });
        } else {
            localDB.open();
            levels.set(localDB.fetchLevels());
            localDB.close();
            callback.onLevelsFetched(levels.get());  // Notify when levels are fetched
        }
    }

    public CompletableFuture<List<Level>> fetchLevels() {
        return CompletableFuture.supplyAsync(() -> {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<List<Level>> levels = new AtomicReference<>(new ArrayList<>());

            if (isOnline()) {
                remoteDB.asyncFetchLevels().thenAccept(levelsList -> {
                    levels.set(levelsList);
                    localDB.open();
                    for (Level level : levelsList) {
                        localDB.insert(level);
                    }
                    latch.countDown();
                    localDB.close();
                });
            } else {
                localDB.open();
                levels.set(localDB.fetchLevels());
                localDB.close();
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return levels.get();
        });
    }

    public LocalDB getLocalDB() {
        return this.localDB;
    }

    public FirestoreDbHelper getRemoteDB() { return this.remoteDB; }
}
