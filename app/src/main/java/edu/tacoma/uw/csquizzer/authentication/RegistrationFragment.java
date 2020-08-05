package edu.tacoma.uw.csquizzer.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.tacoma.uw.csquizzer.R;

/**
 * RegistrationFragment sets up the action listener for user registration
 *
 * @author Bob Schmitz
 * @version 1.0
 */
public class RegistrationFragment extends Fragment {
    /* This is the action listener member variable */
    private RegistrationFragmentListener mRegistrationFragmentListener;

    /**
     * This is the interface for the action listener,
     * methods will be implemented in Authentication Activity
     */
    public interface RegistrationFragmentListener {
        void register(String firstname, String lastname, String email,
                      String username, String password);
    }

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Initializes the mRegistrationFragmentListener
     *
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegistrationFragmentListener = (RegistrationFragmentListener) getActivity();
    }

    /**
     * onCreateView sets up action listener for register button as well as
     * back to login view
     *
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState the savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        mRegistrationFragmentListener = (RegistrationFragmentListener) getActivity();

        // Configure an onClickListener for the Register button
        final EditText firstname = view.findViewById(R.id.et_firstname);
        final EditText lastname = view.findViewById(R.id.et_lastname);
        final EditText email = view.findViewById(R.id.et_email);
        final EditText username = view.findViewById(R.id.et_username);
        final EditText password = view.findViewById(R.id.et_password);
        final EditText confirmPassword = view.findViewById(R.id.et_confirm_password);
        Button btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstText = firstname.getText().toString();
                String lastText = lastname.getText().toString();
                String emailText = email.getText().toString();
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                String confirmPasswordText = confirmPassword.getText().toString();
                if (TextUtils.isEmpty(firstText)) {
                    Toast.makeText(v.getContext(), "Enter a first name",
                            Toast.LENGTH_SHORT).show();
                    firstname.requestFocus();
                } else if (TextUtils.isEmpty(lastText)) {
                    Toast.makeText(v.getContext(), "Enter a last name",
                            Toast.LENGTH_SHORT).show();
                    lastname.requestFocus();
                } else if (TextUtils.isEmpty(emailText)) {
                    Toast.makeText(v.getContext(), "Enter a email",
                            Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                } else if (TextUtils.isEmpty(usernameText)) {
                    Toast.makeText(v.getContext(), "Enter a username",
                            Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                } else if (TextUtils.isEmpty(passwordText) || passwordText.length() < 6) {
                    Toast.makeText(v.getContext(),
                            "Enter a password at least 6 characters long",
                            Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                } else if (!passwordText.equals(confirmPasswordText)) {
                    Toast.makeText(v.getContext(),
                            "New and Confirm Password do not match",
                            Toast.LENGTH_SHORT).show();
                    confirmPassword.requestFocus();
                } else {
                    mRegistrationFragmentListener.register(firstText, lastText, emailText, usernameText, passwordText);
                }
            }
        });

        // Configure onClickListener for login
        TextView login = view.findViewById(R.id.tv_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}