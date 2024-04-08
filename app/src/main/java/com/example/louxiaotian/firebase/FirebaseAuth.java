package com.example.louxiaotian.firebase;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAuth {

    private FirebaseFirestore db;

    public FirebaseAuth(){
        db = FirebaseFirestore.getInstance();

    }

    public void authenticateUser(final String username, final String password, final AuthenticationListener listener) {

        db.collection("users")
                .document(username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String storedPassword = documentSnapshot.getString("password");
                            if (storedPassword != null && storedPassword.equals(password)) {
                                listener.onAuthenticated(true); // Authentication successful
                            } else {
                                listener.onAuthenticated(false); // Incorrect password
                            }
                        } else {
                            listener.onAuthenticated(false); // User not found
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseAuthentication", "Error authenticating user", e);
                        listener.onAuthenticated(false); // Error occurred
                    }
                });
    }

    public void getAllUsernames(final GetAllUsernamesListener listener) {

        db.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> usernames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String username = document.getId();
                            usernames.add(username);
                        }
                        listener.onUsernamesRetrieved(usernames);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseAuthentication", "Error retrieving usernames", e);
                        listener.onUsernamesRetrieved(null);
                    }
                });
    }

    public interface AuthenticationListener {
        void onAuthenticated(boolean isAuthenticated);
    }

    public interface GetAllUsernamesListener {
        void onUsernamesRetrieved(List<String> usernames);
    }
}
