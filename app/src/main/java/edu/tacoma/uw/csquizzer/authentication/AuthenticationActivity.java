
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

public class AuthenticationActivity extends AppCompatActivity implements
        LoginFragment.LoginFragmentListener,
        RegistrationFragment.RegistrationFragmentListener,
        ResetPasswordFragment.ResetPasswordListener {

    private SharedPreferences mSharedPreferences;
    private JSONObject mUserJSON;

    private class AuthenticateUserAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(AuthenticationActivity.this);

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
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    String mode = jsonObject.optString("mode", "undefined");
                    switch(mode) {
                        case "login":
//                            mSharedPreferences
//                                    .edit()
//                                    .putBoolean(getString(R.string.LOGGEDIN), true)
//                                    .putString(getString(R.string.USERNAME),
//                                            mUserJSON.getString("username"))
//                                    .apply();
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

    @Override
    public void register(String firstname, String lastname, String email, String username, String password) {
        StringBuilder url = new StringBuilder(getString(R.string.post_register));
        try {
            mUserJSON = new User(firstname, lastname, email, username, password).getUserJson();
            new AuthenticateUserAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set persistent parameters and launches the MainActivity
     * @param username user provided username
     * @param password user provided password
     */
    @Override
    public void login(String username, String password) {
        StringBuilder url = new StringBuilder(getString(R.string.post_login));
        try {
            mUserJSON = new User(username, password).getUserJson();
            new AuthenticateUserAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set resets user password and goes back to login screen
     * @param email user provided email
     */
    @Override
    public void resetPassword(String email) {
        StringBuilder url = new StringBuilder(getString(R.string.post_reset));
        mUserJSON = new JSONObject();
        try {
            mUserJSON.put("email", email);
            new AuthenticateUserAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchRegistration() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authentication_fragment_id, new RegistrationFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void launchResetPassword() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authentication_fragment_id, new ResetPasswordFragment())
                .addToBackStack(null)
                .commit();
    }

    private void launchMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Access login preferences to determine if user has already logged in
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.authentication_fragment_id, new LoginFragment())
                    .commit();
        } else {
            launchMainMenu();
        }
    }
}