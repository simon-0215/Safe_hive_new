package com.example.louxiaotian.ui.dashboard;

import static com.example.louxiaotian.BlankFragment.chat_username;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.louxiaotian.MessageManager.Message;
import com.example.louxiaotian.database.DatabaseManager;
import com.example.louxiaotian.databinding.FragmentDashboardBinding;

import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView real_message_display_bar = binding.textReal;
        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        final EditText dateInputEdittext = binding.dateInputEdittext;
        final EditText keywordInputEdittext = binding.keywordInputEdittext;
        final Button button = binding.decrptionButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = dateInputEdittext.getText().toString();
                String keyWord = keywordInputEdittext.getText().toString();

                DatabaseManager dbm = new DatabaseManager();
                dbm.getSingleMessage(Message.KEY_SENDER, keyWord, date, new DatabaseManager.GetSingleMessageListener() {
                    @Override
                    public void onMessageRetrieved(String message) {

                        if (message != null) {
                            String message_after_decryption = "message from "+date+" "+ keyWord+": "+message;
                            real_message_display_bar.setText(message_after_decryption);

                        } else {
                            // Failed to retrieve messages
                            Log.d("Messages", "Failed to retrieve messages");
                        }
                    }
                });

            }
        });



        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}