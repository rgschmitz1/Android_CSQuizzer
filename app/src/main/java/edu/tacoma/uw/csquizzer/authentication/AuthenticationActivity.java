package edu.tacoma.uw.csquizzer.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import edu.tacoma.uw.csquizzer.model.User;

/**
 * AuthenticationActivity provides all functionality for logging into the application
 * as well as registration and reset password support.
 *
 * @author Bob Schmitz
 * @version 1.0
 */
public class AuthenticationActivity extends AppCompatActivity implements
        LoginFragment.LoginFragmentListener,
        RegistrationFragment.RegistrationFragmentListener,
        ResetPasswordFragment.ResetPasswordListener {

    /* Shared preferences member field will be used to store user login information */
    private SharedPreferences mSharedPreferences;
    /* This field will be used to store JSON information about the user to be sent to backend */
    private JSONObject mUserJSON;
    /* Set testing variable, set false when testing finished */
    private boolean mTesting = false;

    /**
     * AuthenticateUserAsyncTask will handle all backend transactions as well as transitioning
     * between the login, registration, reset password, and main menu views.
     *
     * @author Bob Schmitz and Phuc Pham
     * @version 1.0
     */
    private class AuthenticateUserAsyncTask extends AsyncTask<String, Void, String> {
        /* This member field will be used to display onscreen progress to the user */
        ProgressDialog pdLoading = new ProgressDialog(AuthenticationActivity.this);

        /**
         * onPreExecute will be used to setup and display the onscreen progress of backend task.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // This method will be running on UI thread
            pdLoading.setMessage(getString(R.string.wait_msg));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        /**
         * doInBackground is sending JSON data via a POST to the backend server.
         *
         * @param strings A URL to send the JSON POST
         * @return String a JSON formatted string for parsing status of POST request
         */
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

        /**
         * onPostExecute will navigate to the login screen or start the MainActivity if
         * doInBackground reports that the login mode was successful.  If doInBackground reports
         * success = false, an error message will be displayed for the user.
         *
         * @param s The JSON formatted data from the do in doInBackground method
         */
        @Override
        protected void onPostExecute(String s) {
            // Disable modal message
            pdLoading.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    String mode = jsonObject.optString("mode", "undefined");
                    switch(mode) {
                        case "login":
                            String user = mUserJSON.getString(User.USERNAME);
                            mSharedPreferences
                                    .edit()
                                    .putBoolean(getString(R.string.LOGGEDIN), true)
                                    .putString(getString(R.string.USERNAME), user)
                                    .apply();
                            // Display welcome message
                            String msg = "Welcome " + user + "!";
                            Toast.makeText(getApplicationContext(), msg,
                                    Toast.LENGTH_SHORT).show();
                            launchMainMenu();
                            break;
                        case "register":
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().popBackStack();
                            break;
                        case "reset":
                            Toast.makeText(getApplicationContext(),
                                    "Password reset successful!",
                                    Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().popBackStack();
                            break;
                        default:
                            Log.e("AuthenticationActivity",
                                    "Invalid mode passed to AsyncTask");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error: "
                        + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * register will create a JSON object using user provided information, then pass the JSON to
     * an instance of AuthenticateUserAsyncTask
     *
     * @param firstname the first name
     * @param lastname the last name
     * @param email the email
     * @param username the username
     * @param password the password
     */
    @Override
    public void register(String firstname, String lastname, String email, String username, String password) {
        StringBuilder url = new StringBuilder(getString(R.string.post_register));
        try {
            mUserJSON = new User(firstname, lastname, email, username, password).getUserJson();
            new AuthenticateUserAsyncTask().execute(url.toString());
        } catch (IllegalArgumentException | JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * login will create a JSON object using user provided information, then pass the JSON to
     * an instance of AuthenticateUserAsyncTask
     *
     * @param username the username
     * @param password the password
     */
    @Override
    public void login(String username, String password) {
        StringBuilder url = new StringBuilder(getString(R.string.post_login));
        try {
            mUserJSON = new User(username, password).getUserJson();
            new AuthenticateUserAsyncTask().execute(url.toString());
        } catch (IllegalArgumentException | JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * resetPassword will create a JSON object using user provided information, then pass the
     * JSON to an instance of AuthenticateUserAsyncTask
     *
     * @param email the email
     */
    @Override
    public void resetPassword(String email) {
        StringBuilder url = new StringBuilder(getString(R.string.post_reset));
        mUserJSON = new JSONObject();
        try {
            mUserJSON = new User(email).getUserJson();
            new AuthenticateUserAsyncTask().execute(url.toString());
        } catch (IllegalArgumentException | JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * launchRegistration will replace the login fragment with the registration fragment
     */
    @Override
    public void launchRegistration() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authentication_fragment_id, new RegistrationFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * launchResetPassword will replace the login fragment with the reset password fragment
     */
    @Override
    public void launchResetPassword() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authentication_fragment_id, new ResetPasswordFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * launchMainMenu will start the main activity
     */
    private void launchMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * onCreate will initialize the mSharedPreferences member field and either load the
     * login fragment or start the main activity
     *
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Access login preferences to determine if user has already logged in
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        if (mTesting || !mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.authentication_fragment_id, new LoginFragment())
                    .commit();
        } else {
            launchMainMenu();
        }
    }
}