package com.example.louxiaotian.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.Navigation;

import com.example.louxiaotian.MessageManager.Message;
import com.example.louxiaotian.databinding.FragmentLoginBinding;

import com.example.louxiaotian.R;


import com.example.louxiaotian.firebase.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Objects;
import java.util.Queue;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;


        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button_login = view.findViewById(R.id.login);
                Button button_register = view.findViewById(R.id.register);
                EditText password = view.findViewById(R.id.password);
                EditText username = view.findViewById(R.id.username);

//                button_login.setOnClickListener(vv ->
//                        // authenticateUser returns true if user and pass are found in the database and false otherwise
//                        // TODO: if true, go to home page, else prompt user to check credentialsS
//                        authenticateUser(username.getText().toString(), password.getText().toString())
//                );
//                Message.KEY_SENDER = username.getText().toString();
                button_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
////                        if(authenticateUser((username.getText().toString()),password.getText().toString())){
//                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_blankFragment);
////                        }
//                        // 在这里实现点击事件的处理逻辑

                        FirebaseAuth firebaseAuth = new FirebaseAuth();
                        firebaseAuth.authenticateUser(username.getText().toString(), password.getText().toString(), new FirebaseAuth.AuthenticationListener() {
                            @Override
                            public void onAuthenticated(boolean isAuthenticated) {
//                                if (isAuthenticated) {
                                    // Authentication successful
                                    Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_blankFragment);
//                                } else {
                                    // Authentication failed
//                                    Log.d("Authentication", "Authentication failed");
//                                }
                            }
                        });

                    }
                });




            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button_login = view.findViewById(R.id.login);
                Button button_register = view.findViewById(R.id.register);
                EditText password = view.findViewById(R.id.password);
                EditText username = view.findViewById(R.id.username);

                button_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String new_username = username.getText().toString();
                        String new_password = password.getText().toString();
                        //those are the username and password user enter and want to create a new account.
                        //enter user into db
                        registerUser(new_username, new_password);

                    }
                });


            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void registerUser(String username, String password) {

        // TODO: implement checks for uniqueness of username, maybe ask for additional fields for employee database interfacing
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", username);
//        data.put("last_name", lastname);
//        data.put("email", email);
        data.put("password", password);

        database.collection("users")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    public boolean authenticateUser(String username, String password){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("users");
        final boolean[] foundUser = new boolean[1];
        userRef.whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                        && !task.getResult().getDocuments().isEmpty()){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        foundUser[0] = true;
                    } else {
                        foundUser[0] = false;
                    }
                });
        return foundUser[0];
    }
}