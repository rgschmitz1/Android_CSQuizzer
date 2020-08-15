package edu.tacoma.uw.csquizzer;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import edu.tacoma.uw.csquizzer.authentication.AuthenticationActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 */
@RunWith(AndroidJUnit4.class)
public class AuthenticationActivityTest {
    @Rule
    public ActivityTestRule<AuthenticationActivity> mActivityRule = new ActivityTestRule<>(
            AuthenticationActivity.class);

    @Test
    public void testLogin() {
        String user = "bschmitz";
        String pass = "bigdawgs";

        // Type text and then press the button.
        onView(withId(R.id.et_username))
                .perform(typeText(user));
        onView(withId(R.id.et_password))
                .perform(typeText(pass));

        // We need to close the keyboard or button press will not work
        onView(withId(R.id.et_password)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_login))
                .perform(click());

        // Check for welcome message
        onView(withText("Welcome " + user + "!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginInvalidUsername() {
        String user = "foobar";
        String pass = "123456";

        // Type text and then press the button.
        onView(withId(R.id.et_username))
                .perform(typeText(user));
        onView(withId(R.id.et_password))
                .perform(typeText(pass));

        // We need to close the keyboard or button press will not work
        onView(withId(R.id.et_password)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withText("username and/or password was not found"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginInvalidPassword() {
        String user = "bschmitz";
        String pass = "123456";

        // Type text and then press the button.
        onView(withId(R.id.et_username))
                .perform(typeText(user));
        onView(withId(R.id.et_password))
                .perform(typeText(pass));

        // We need to close the keyboard or button press will not work
        onView(withId(R.id.et_password)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withText("username and/or password was not found"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRegistration() {
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(10000) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1)
                + (random.nextInt(400) + 1) + (random.nextInt(100) + 1)
                + "@uw.edu";
        String first = "John";
        String last = "Doe";
        String pass = "pass" + (random.nextInt(10000) + 1);

        onView(withId(R.id.tv_signup))
                .perform(click());

        // Type text and then press the button.
        onView(withId(R.id.et_firstname))
                .perform(typeText(first));
        onView(withId(R.id.et_lastname))
                .perform(typeText(last));
        onView(withId(R.id.et_email))
                .perform(typeText(email));
        onView(withId(R.id.et_username))
                .perform(typeText(email));
        onView(withId(R.id.et_password))
                .perform(typeText(pass));
        // We need to close the keyboard or button press will not work
        onView(withId(R.id.et_password)).perform(closeSoftKeyboard());
        onView(withId(R.id.et_confirm_password))
                .perform(typeText(pass));
        // We need to close the keyboard or button press will not work
        onView(withId(R.id.et_confirm_password)).perform(closeSoftKeyboard());

        onView(withId(R.id.btn_register))
                .perform(click());

        onView(withText("Registration successful!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }
}