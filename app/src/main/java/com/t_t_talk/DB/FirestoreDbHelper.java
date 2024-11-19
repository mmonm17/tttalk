package com.t_t_talk.DB;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreDbHelper {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Listener listener;

    public FirestoreDbHelper(Listener listener) {
        this.listener = listener;
    }

    public void registerUser(String username, String password) {
        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);
        db.collection("Users")
                .document(username).set(userData)
                .addOnSuccessListener(aVoid -> {
                    Response response = new Response(true, "Registered Successfully.");
                    listener.onSuccess(response);
                })
                .addOnFailureListener(e -> {
                    Response response = new Response(false, e.getMessage());
                    listener.onFailure(response);
                });
    }
}
