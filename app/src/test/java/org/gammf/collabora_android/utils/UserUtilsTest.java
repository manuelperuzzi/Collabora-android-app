package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.model.users.SimpleUser;
import org.gammf.collabora_android.model.users.User;
import org.gammf.collabora_android.utils.model.UserUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mattia on 16/09/2017.
 */

public class UserUtilsTest {

    private static final String USERNAME = "mrashford";
    private static final String EMAIL = "mrashford@manutd.en";
    private static final String NAME = "Marcus";
    private static final String SURNAME = "Rashford";
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
    public void userToJson() {
        final JSONObject json = UserUtils.userToJson(user);
        System.out.println("[UserUtilsTest]: " + json);
        final User userTest = UserUtils.jsonToUser(json);
        assertEquals(userTest.getUsername(), user.getUsername());
        assertEquals(userTest.getEmail(), user.getEmail());
        assertEquals(userTest.getName(), user.getName());
        assertEquals(userTest.getSurname(), user.getSurname());
        assertEquals(userTest.getBirthday(), user.getBirthday());
    }
}
