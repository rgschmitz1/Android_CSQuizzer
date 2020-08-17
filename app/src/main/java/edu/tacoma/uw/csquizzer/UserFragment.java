package edu.tacoma.uw.csquizzer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The UserFragment is placed in MainActivity.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class UserFragment extends Fragment {
    private View rootView;
    private JSONObject mUserJSON;
    private Context mContext;

    /**
     * AuthenticateUserAsyncTask will handle all backend transactions as well as transitioning
     * between the login, registration, reset password, and main menu views.
     *
     * @author Bob Schmitz and Phuc Pham
     * @version 1.0
     */
    private class ChangePasswordAsyncTask extends AsyncTask<Void, Void, String> {
        /* This member field will be used to display onscreen progress to the user */
        private ProgressDialog pdLoading = new ProgressDialog(getActivity());

        /**
         * onPreExecute will be used to setup and display the onscreen progress of backend task.
         */
        @Override
        protected void onPreExecute() {
            // This method will be running on UI thread
            pdLoading.setMessage(getString(R.string.wait_msg));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        /**
         * doInBackground is sending JSON data via a POST to the backend server.
         *
         * @param arg0 null argument
         * @return String a JSON formatted string for parsing status of POST request
         */
        @Override
        protected String doInBackground(Void... arg0) {
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(getString(R.string.update_password));
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
                response.append("Exception occurred: ");
                response.append(e.getMessage());
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
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
                    Toast.makeText(getActivity(), "Password update successful!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), jsonObject.getString("error"),
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "JSON Parsing error: "
                        + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public UserFragment(Context context) {
        mContext = context;
    }

    /**
     * Render components to GUI
     * @param inflater a class used to instantiate layout XML file into its corresponding view objects
     * @param container a special view that can contain other views
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method.
     * @return view
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user, container, false);
        // Configure an onClickListener for the Login button
        final EditText oldPass = rootView.findViewById(R.id.old_password);
        final EditText newPass = rootView.findViewById(R.id.new_password);
        final EditText confirmPass = rootView.findViewById(R.id.confirm_password);
        Button btnLogin = rootView.findViewById(R.id.btn_submit);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassText = oldPass.getText().toString();
                String newPassText = newPass.getText().toString();
                String confirmPassText = confirmPass.getText().toString();
                if (TextUtils.isEmpty(oldPassText) || oldPassText.length() < 6) {
                    Toast.makeText(v.getContext(),
                            "Old password must be at least 6 characters long",
                            Toast.LENGTH_SHORT).show();
                    oldPass.requestFocus();
                } else if (TextUtils.isEmpty(newPassText) || newPassText.length() < 6) {
                    Toast.makeText(v.getContext(),
                            "New password must be at least 6 characters long",
                            Toast.LENGTH_SHORT).show();
                    newPass.requestFocus();
                } else if (!newPassText.equals(confirmPassText)) {
                    Toast.makeText(v.getContext(),
                            "New password and confirm password must match",
                            Toast.LENGTH_SHORT).show();
                    confirmPass.requestFocus();
                } else {
                    try {
                        SharedPreferences sharedPref =
                                mContext.getSharedPreferences(getString(R.string.LOGIN_PREFS),
                                        Context.MODE_PRIVATE);

                        mUserJSON = new JSONObject();
                        mUserJSON.put("username", sharedPref.getString(
                                getString(R.string.USERNAME), "undefined"));
                        mUserJSON.put("oldpw", oldPassText);
                        mUserJSON.put("newpw", newPassText);
                        new ChangePasswordAsyncTask().execute();
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(),
                                "An error occurred: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return rootView;
    }
}