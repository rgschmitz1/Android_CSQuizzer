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
 * ResetPasswordFragment sets up the action listener for password reset
 *
 * @author Bob Schmitz
 * @version 1.0
 */
public class ResetPasswordFragment extends Fragment {
    /* This is the action listener member variable */
    private ResetPasswordListener mResetPasswordListener;

    /**
     * This is the interface for the action listener,
     * methods will be implemented in Authentication Activity
     */
    public interface ResetPasswordListener {
        void resetPassword(String email);
    }

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Initializes the mResetPasswordListener
     *
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResetPasswordListener = (ResetPasswordListener) getActivity();
    }

    /**
     * onCreateView sets up action listener for submit button as well as
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
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        mResetPasswordListener = (ResetPasswordListener) getActivity();

        // Configure an onClickListener for the Reset button
        final EditText email = view.findViewById(R.id.et_email);
        Button btnReset = view.findViewById(R.id.btn_resetpassword);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                if (TextUtils.isEmpty(emailText)) {
                    Toast.makeText(v.getContext(), "Enter a email",
                            Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                } else {
                    mResetPasswordListener.resetPassword(emailText);
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