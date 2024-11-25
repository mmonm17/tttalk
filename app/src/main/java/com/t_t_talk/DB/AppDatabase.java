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
    public interface LevelsCallback {
        void onLevelsFetched(List<Level> levels);
    }
    public void fetchLevels(LevelsCallback callback) {
        AtomicReference<List<Level>> levels = new AtomicReference<>(new ArrayList<>());

        if (isOnline()) {
            Log.d("AppDatabase", "Fetching levels from remote database");
            remoteDB.asyncFetchLevels().thenAccept(levelsList -> {
                levels.set(levelsList);
                callback.onLevelsFetched(levelsList);  // Notify when levels are fetched
            });
        } else {
            levels.set(localDB.fetchLevels());
            Log.d("AppDatabase", "is offline, fetching levels from local database");
            callback.onLevelsFetched(levels.get());  // Notify when levels are fetched
        }
    }


    public List<Level> fetchLevels() {
        AtomicReference<List<Level>> levels = new AtomicReference<>(new ArrayList<>());

        if (isOnline()) {
            Log.d("AppDatabase", "Fetching levels from remote database");
            remoteDB.asyncFetchLevels().thenAccept(levelsList -> {
                levels.set(levelsList);
            });
        } else {
            levels.set(localDB.fetchLevels());
            Log.d("AppDatabase", "is offline, fetching levels from local database");

        }
        return levels.get();
    }

    public LocalDB getLocalDB() {
        return this.localDB;
    }

    public FirestoreDbHelper getRemoteDB() { return this.remoteDB; }
}
