
package edu.tacoma.uw.csquizzer.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import edu.tacoma.uw.csquizzer.MainActivity;
import edu.tacoma.uw.csquizzer.R;
import edu.tacoma.uw.csquizzer.RecoveryPasswordActivity;
import edu.tacoma.uw.csquizzer.model.User;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private JSONObject mUserJSON;

    private static final String LOGIN = "LOGIN";

    private class AuthenticateUserAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);

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
                    // For Debugging
//                    Log.i(LOGIN, mUserJSON.toString());

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
            // For Debugging
//            Log.i(LOGIN, s);
            if (s.startsWith("Unable to login")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "User login successfully"
                            , Toast.LENGTH_SHORT).show();
//                    mSharedPreferences
//                            .edit()
//                            .putBoolean(getString(R.string.LOGGEDIN), true)
//                            .putString(getString(R.string.USERNAME), username)
//                            .commit();
                    launchMainMenu();
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    //Log.e(LOGIN, jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on login: "
                        + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Set persistent parameters and execute the MainActivity
     * @param username user provided username
     * @param password user provided password
     */
    public void login(String username, String password) {
        StringBuilder url = new StringBuilder(getString(R.string.post_login));
        try {
            mUserJSON = new User(username, password).getUserJson();
            Log.i("USERJSON", mUserJSON.toString());
            new AuthenticateUserAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Access login preferences to determine if user has already logged in
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            // Configure an onClickListener for the Login button
            Button btnLogin = findViewById(R.id.btn_Login);
            final EditText username = findViewById(R.id.et_username);
            final EditText password = findViewById(R.id.et_password);
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
                        login(usernameText, passwordText);
                    }
                }
            });

            // Configure onClickListener for password recovery
            TextView recovery = findViewById(R.id.tv_forgotpassword);
            recovery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RecoveryPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            // Configure onClickListener for registration
            TextView register = findViewById(R.id.tv_signup);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            launchMainMenu();
        }
    }
}