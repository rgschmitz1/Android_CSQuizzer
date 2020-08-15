package edu.tacoma.uw.csquizzer.model;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class User {
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mUsername;
    private String mPassword;

    public static final String FIRSTNAME = "first";
    public static final String LASTNAME = "last";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                    Pattern.CASE_INSENSITIVE);

    // Password minimum length
    private static final int PASSWORD_LEN = 6;

    public User(String FirstName, String LastName, String Email, String Username, String Password) {
        this.mFirstName = FirstName;
        this.mLastName = LastName;
        setmEmail(Email);
        this.mUsername = Username;
        setmPassword(Password);
    }

    public User(String Username, String Password) {
        this.mUsername = Username;
        setmPassword(Password);
    }

    public User(String Email) {
        setmEmail(Email);
    }

    /**
     * Return User information as JSONObject
     * @return User information as JSONObject
     */
    public JSONObject getUserJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (!TextUtils.isEmpty(mFirstName) && !TextUtils.isEmpty(mLastName)) {
            jsonObject.put(FIRSTNAME, mFirstName);
            jsonObject.put(LASTNAME, mLastName);
        }
        if (!TextUtils.isEmpty(mEmail))
            jsonObject.put(EMAIL, mEmail);
        if (!TextUtils.isEmpty(mUsername) && !TextUtils.isEmpty(mPassword)) {
            jsonObject.put(USERNAME, mUsername);
            jsonObject.put(PASSWORD, mPassword);
        }
        return jsonObject;
    }

    /**
     * Validates if the given input is a valid email address.
     *
     * @param email The email to validate.
     * @return {@code true} if the input is a valid email. {@code false} otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if the given password is valid.
     * Valid password must be at last 6 characters long.
     *
     * @param password The password to validate.
     * @return {@code true} if the input is a valid password. {@code false} otherwise.
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= PASSWORD_LEN;
    }

    public void setmEmail(String email) {
        if (isValidEmail(email)) {
            mEmail = email;
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public void setmPassword(String password) {
        if (isValidPassword(password)) {
            mPassword = password;
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }
}