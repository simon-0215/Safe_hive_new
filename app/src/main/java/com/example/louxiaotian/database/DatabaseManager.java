package com.example.louxiaotian.database;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.louxiaotian.MessageManager.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private FirebaseFirestore db;

    public DatabaseManager(){
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
                            String username = document.getString("username");
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

    public void getAllMessages(String sender, String receiver, final GetAllMessagesListener listener) {
        db.collection("Message")
                .whereEqualTo("sender", sender)
                .whereEqualTo("receiver", receiver)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        List<Message> messages = new ArrayList<>();
                        List<String> messages = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Assuming Message is a custom class representing your message data
                            Log.d("Messages_DB", "Message: "+ document.getString("text"));
//                            Message message = new Message(document.getString("receiver"), document.getString("text"), document.getString("time_date"));
                            messages.add(document.getString("text"));
//                            Log.d("Messages_DB", "Message: "+ message.getText());

                        }
                        for (String message: messages){
                            Log.d("Messages_DB", "Message: "+ message);
                        }
                        listener.onMessagesRetrieved(messages);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseAuthentication", "Error retrieving messages", e);
                        listener.onMessagesRetrieved(null);
                    }
                });
    }

    public void getSingleMessage(String sender, String receiver, String time, final GetSingleMessageListener listener) {
        Log.d("Sender:", sender);
        Log.d("Receiver:", receiver);
        Log.d("Time:", time);

        db.collection("Message")
                .whereEqualTo("sender", sender)
                .whereEqualTo("receiver", receiver)
                .whereEqualTo("time_date", time)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            if (documentSnapshot.exists()) {
                                String text = documentSnapshot.getString("text");
                                listener.onMessageRetrieved(text);
                            } else {
                                listener.onMessageRetrieved(null);
                            }
                        } else {
                            listener.onMessageRetrieved(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseAuthentication", "Error retrieving messages", e);
                        listener.onMessageRetrieved(null);
                    }
                });
    }




    public interface AuthenticationListener {
        void onAuthenticated(boolean isAuthenticated);
    }

    public interface GetAllUsernamesListener {
        void onUsernamesRetrieved(List<String> usernames);
    }

    public interface GetAllMessagesListener {
        void onMessagesRetrieved(List<String> messages);
    }

    public interface GetSingleMessageListener {
        void onMessageRetrieved(String message);
    }


}
