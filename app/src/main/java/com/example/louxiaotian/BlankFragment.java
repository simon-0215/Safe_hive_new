package com.example.louxiaotian;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;

import com.example.louxiaotian.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    public static String chat_username = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get Users
        DatabaseManager fba = new DatabaseManager();

        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_blank, container, false);
        Button user1 = view.findViewById(R.id.button1);
        Button user2 = view.findViewById(R.id.button2);
        Button user3 = view.findViewById(R.id.button3);
        Button user4 = view.findViewById(R.id.button4);
        Button user5 = view.findViewById(R.id.button5);
        List<Button> buttons = new ArrayList<>(Arrays.asList(user1, user2, user3, user4, user5));
        fba.getAllUsernames(new DatabaseManager.GetAllUsernamesListener() {
            @Override
            public void onUsernamesRetrieved(List<String> usernames) {
                if (usernames != null) {
                    for(int i=0; i<buttons.size(); i++ ){
                        final String username = usernames.get(i);
                        buttons.get(i).setText(username);
                        buttons.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chat_username = username;
                                Navigation.findNavController(v).navigate(R.id.action_blankFragment_to_navigation_home);
                            }
                        });
                    }
                } else {
                    // Failed to retrieve usernames
                    Log.d("Usernames", "Failed to retrieve usernames");
                }
            }
        });

        return view;
    }


}