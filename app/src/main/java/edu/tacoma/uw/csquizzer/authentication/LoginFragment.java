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
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private LoginFragmentListener mLoginFragmentListener;

    public interface LoginFragmentListener {
        void login(String email, String pwd);
        void launchRegistration();
        void launchResetPassword();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginFragmentListener = (LoginFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginFragmentListener = (LoginFragmentListener) getActivity();

        // Configure an onClickListener for the Login button
        final EditText username = view.findViewById(R.id.et_username);
        final EditText password = view.findViewById(R.id.et_password);
        Button btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                if (TextUtils.isEmpty(usernameText)) {
                    Toast.makeText(v.getContext(), "Enter a username",
                            Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                } else if (TextUtils.isEmpty(passwordText) || passwordText.length() < 6) {
                    Toast.makeText(v.getContext(),
                            "Enter a password at least 6 characters long",
                            Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                } else {
                    mLoginFragmentListener.login(usernameText, passwordText);
                }
            }
        });

        // Configure onClickListener for password reset
        TextView recovery = view.findViewById(R.id.tv_forgotpassword);
        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginFragmentListener.launchResetPassword();
            }
        });

        // Configure onClickListener for registration
        TextView register = view.findViewById(R.id.tv_signup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginFragmentListener.launchRegistration();
            }
        });

        return view;
    }
}