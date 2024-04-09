package com.example.louxiaotian.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.louxiaotian.MessageManager.Message;
import com.example.louxiaotian.database.DatabaseManager;
import com.example.louxiaotian.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.louxiaotian.BlankFragment.chat_username;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final TextView displayText1 = binding.displayText1;
        final TextView displayText2 = binding.displayText2;
        final TextView displayText3 = binding.displayText3;
        final TextView displayTextNew = binding.displayNew;


        List<TextView> textdisplays = new ArrayList<>(Arrays.asList(displayText3, displayText2, displayText1));
        Iterator<TextView> t_iter = textdisplays.iterator();

        final TextView chat_Username = binding.textViewLeftTop;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        final EditText editText = binding.editTextInput; // Assuming you have an EditText with this ID in your FragmentHomeBinding
        final Button button = binding.buttonPrint; // Assuming you have a Button with this ID in your FragmentHomeBinding
        chat_Username.setText(chat_username);

        DatabaseManager dbm = new DatabaseManager();
        dbm.getAllMessages(Message.KEY_SENDER, chat_username, new DatabaseManager.GetAllMessagesListener() {
            @Override
            public void onMessagesRetrieved(List<String> messages) {
                if (messages != null) {
                    // Messages retrieved successfully
                    Log.d("Messages", "Num messages:"+messages.size());
                    Log.d("Messages", "Messages"+messages);
//                    for (Message message : messages) {
//                        // Process each message
////                        displayText.setText(message.getText());
//                        Log.d("Messages", "Recipient: " + message.getReciever());
//                        Log.d("Messages", "Message: " + message.getText());
//                    }
                    for (String message : messages) {
                        if(t_iter.hasNext()){
                            t_iter.next().setText(message);
                        }
//                        displayText.setText(message);
                        Log.d("Messages", "Message: " + message);
                    }
                } else {
                    // Failed to retrieve messages
                    Log.d("Messages", "Failed to retrieve messages");
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editText.getText().toString();
                displayTextNew.setText(inputText);
                addMessageToFirestore(inputText, chat_username);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void addMessageToFirestore(String text, String reciever){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("sender", Message.KEY_SENDER);
        data.put("receiver", reciever);
        data.put("time_date",(new Date()).toString());

        database.collection("Message")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}