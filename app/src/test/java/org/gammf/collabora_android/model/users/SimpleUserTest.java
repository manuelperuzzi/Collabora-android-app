package org.gammf.collabora_android.model.users;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Simple tests for simple user class.
 */
public class SimpleUserTest {

    private static final String USERNAME = "peru";
    private static final String EMAIL = "manuel.peruzzi@studio.unibo.it";
    private static final String NAME = "Manuel";
    private static final String SURNAME = "Peruzzi";
    private static final Integer BIRTH_YEAR = 1994;
    private static final Integer BIRTH_MONTH = 3;
    private static final Integer BIRTH_DAY = 7;
    private static final Integer NO_HOUR = 0;
    private static final Integer NO_MINUTE = 0;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new SimpleUser.Builder()
                .username(USERNAME)
                .email(EMAIL)
                .name(NAME)
                .surname(SURNAME)
                .birthday(new DateTime(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY, NO_HOUR, NO_MINUTE))
                .build();
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(user.getUsername(), USERNAME);
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals(user.getEmail(), EMAIL);
    }

    @Test
    public void getName() throws Exception {
        assertEquals(user.getName(), NAME);
    }

    @Test
    public void getSurname() throws Exception {
        assertEquals(user.getSurname(), SURNAME);
    }

    @Test
    public void getBirthday() throws Exception {
        assertEquals(user.getBirthday(), new DateTime(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY, NO_HOUR, NO_MINUTE));
    }

}