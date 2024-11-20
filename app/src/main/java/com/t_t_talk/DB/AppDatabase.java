package com.t_t_talk.DB;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.t_t_talk.DB.LocalDB.LocalDB;
import com.t_t_talk.DB.Models.Level;
import com.t_t_talk.DB.RemoteDB.FirestoreDbHelper;

import java.util.ArrayList;
import java.util.List;

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

    public List<Level> fetchLevels() {
        List<Level> levels = new ArrayList<>();

        if(isOnline()) {
            List<Level> cachedLevelData = localDB.fetchLevels();
            levels = remoteDB.fetchLevels();

            //Update remoteDB with new starCounts

            //Clear localDB cache

            //insert update levels to localDB cache
            for(Level l: levels) {
                localDB.insert();
            }
        } else {
            levels = localDB.fetchLevels();
        }

        return levels;
    }

    public LocalDB getLocalDB() {
        return this.localDB;
    }
}
