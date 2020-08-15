package edu.tacoma.uw.csquizzer;

import org.junit.Test;

import edu.tacoma.uw.csquizzer.model.User;

import static org.junit.Assert.fail;

/**
 * Test User model
 * @author Bob Schmitz
 * @version 1.0
 */
public class UserModelTest {
    @Test
    public void testUserConstructor1() {
        try {
            new User("Bob", "Schmitz",
                    "rgs1@uw.edu", "bschmitz", "123456");
        } catch (IllegalArgumentException e) {
            fail("Account creation failure");
        }
    }

    @Test
    public void testUserConstructor2() {
        try {
            new User("bschmitz", "123456");
        } catch (IllegalArgumentException e) {
            fail("Account creation failure");
        }
    }

    @Test
    public void testUserConstructor3() {
        try {
            new User("rgs1@uw.edu");
        } catch(IllegalArgumentException e) {
            fail("Account creation failure");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullEmailException() {
        new User(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidEmailException1() {
        new User("rgs1@uwedu");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidEmailException2() {
        new User("rgs1uw.edu");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidEmailException3() {
        new User("@uw.edu");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPasswordException() {
        new User("bschmitz", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shortPasswordException() {
        new User("bschmitz", "12345");
    }
}