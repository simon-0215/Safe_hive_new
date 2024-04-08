package com.example.louxiaotian.ui.home;

import android.os.Bundle;
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
import com.example.louxiaotian.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final TextView displayText = binding.displayText;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        final EditText editText = binding.editTextInput; // Assuming you have an EditText with this ID in your FragmentHomeBinding
        final Button button = binding.buttonPrint; // Assuming you have a Button with this ID in your FragmentHomeBinding
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取EditText输入的内容，并设置到TextView上显示
                String inputText = editText.getText().toString();
                displayText.setText(inputText);
                addMessageToFirestore(inputText);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void addMessageToFirestore(String text){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        data.put("Text", text);
        data.put("Sender", Message.KEY_SENDER);
        data.put("Timestamp", new Date());

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