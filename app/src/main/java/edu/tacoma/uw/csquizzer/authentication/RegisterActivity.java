package edu.tacoma.uw.csquizzer.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.csquizzer.R;
import edu.tacoma.uw.csquizzer.model.User;

public class RegisterActivity extends AppCompatActivity {
    private JSONObject mUserJSON;

    private class RegisterUserAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(RegisterActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // This method will be running on UI thread
            pdLoading.setMessage(getString(R.string.wait_msg));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;
            for (String url : strings) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);

                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(mUserJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response.append(s);
                    }
                    buffer.close();
                } catch (Exception e) {
                    response.append("Authentication exception occurred: ");
                    response.append(e.getMessage());
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            // Disable modal message
            pdLoading.dismiss();
            if (s.startsWith("Unable to register")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "User registration successfully"
                            , Toast.LENGTH_SHORT).show();
                    launchLoginActivity();
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on register: "
                        + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Set persistent parameters and execute the MainActivity
     * @param firstname user provided first name
     * @param lastname user provided last name
     * @param email user provided email
     * @param username user provided username
     * @param password user provided password
     */
    public void register(String firstname, String lastname, String email, String username, String password) {
        StringBuilder url = new StringBuilder(getString(R.string.post_register));
        try {
            mUserJSON = new User(firstname, lastname, email, username, password).getUserJson();
            new RegisterUserAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Configure an onClickListener for the Register button
        Button btnRegister = findViewById(R.id.btn_register);
        final EditText firstname = findViewById(R.id.et_firstname);
        final EditText lastname = findViewById(R.id.et_lastname);
        final EditText email = findViewById(R.id.et_email);
        final EditText username = findViewById(R.id.et_username);
        final EditText password = findViewById(R.id.et_password);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstText = firstname.getText().toString();
                String lastText = lastname.getText().toString();
                String emailText = email.getText().toString();
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
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
                } else {
                    register(firstText, lastText, emailText, usernameText, passwordText);
                }
            }
        });

        // Configure onClickListener for login textview
        TextView login = findViewById(R.id.tv_loginme);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });
    }
}