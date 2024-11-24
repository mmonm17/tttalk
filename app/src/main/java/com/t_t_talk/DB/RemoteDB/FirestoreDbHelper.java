package com.t_t_talk.DB.RemoteDB;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreDbHelper {
    private FirebaseFirestore db;
    private static FirestoreDbHelper instance;

    private FirestoreDbHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    public static FirestoreDbHelper getInstance() {
        if (instance == null) {
            instance = new FirestoreDbHelper();
        }
        return instance;
    }

}
