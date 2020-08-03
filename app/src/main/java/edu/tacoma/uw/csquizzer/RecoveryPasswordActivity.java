package edu.tacoma.uw.csquizzer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import edu.tacoma.uw.csquizzer.authentication.LoginActivity;

public class RecoveryPasswordActivity extends AppCompatActivity {
    private JSONObject mEmailJSON = new JSONObject();
    public static final String EMAIL = "email";

    private class ResetAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(RecoveryPasswordActivity.this);

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
                    wr.write(mEmailJSON.toString());
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
            if (s.startsWith("Unable to reset")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "User reset password successfully"
                            , Toast.LENGTH_SHORT).show();
                    launchLoginActivity();
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on reset: "
                        + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Set persistent parameters and execute the MainActivity
     * @param email user provided email
     */
    public void reset(String email) {
        StringBuilder url = new StringBuilder(getString(R.string.post_reset));
        Log.i("email", email);
        try {
            mEmailJSON.put(EMAIL, email);
            Log.i("test", mEmailJSON.toString());
            new ResetAsyncTask().execute(url.toString());
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
        setContentView(R.layout.activity_recovery_password);

        Button btnReset = findViewById(R.id.btn_resetpassword);
        final EditText email = findViewById(R.id.et_email);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                if (TextUtils.isEmpty(emailText)) {
                    Toast.makeText(v.getContext(), "Enter a email",
                            Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                } else {
                    reset(emailText);
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