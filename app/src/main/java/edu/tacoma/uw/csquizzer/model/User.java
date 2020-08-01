package edu.tacoma.uw.csquizzer.model;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private final String mUsername;
    private final String mPassword;

    public static final String FIRSTNAME = "first";
    public static final String LASTNAME = "last";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public User(String FirstName, String LastName, String Email, String Username, String Password) {
        this.mFirstName = FirstName;
        this.mLastName = LastName;
        this.mEmail = Email;
        this.mUsername = Username;
        this.mPassword = Password;
    }

    public User(String Username, String Password) {
        this.mUsername = Username;
        this.mPassword = Password;
    }

    /**
     * Return User information as JSONObject
     * @return User information as JSONObject
     */
    public JSONObject getUserJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (!TextUtils.isEmpty(mFirstName) &&
                !TextUtils.isEmpty(mLastName) &&
                !TextUtils.isEmpty(mEmail)) {
            jsonObject.put(FIRSTNAME, mFirstName);
            jsonObject.put(LASTNAME, mLastName);
            jsonObject.put(EMAIL, mEmail);
        }
        jsonObject.put(USERNAME, mUsername);
        jsonObject.put(PASSWORD, mPassword);
        return jsonObject;
    }
}
