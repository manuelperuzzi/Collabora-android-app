package org.gammf.collabora_android.collaborations;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mperuzzi on 12/08/17.
 */
public class SimpleUserTest {

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new SimpleUser.Builder()
                .username("peru")
                .email("manuel.peruzzi@studio.unibo.it")
                .name("Manuel")
                .surname("Peruzzi")
                .birthday(new DateTime(1994, 3, 7, 0, 0))
                .build();
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(user.getUsername(), "peru");
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals(user.getEmail(), "manuel.peruzzi@studio.unibo.it");
    }

    @Test
    public void getName() throws Exception {
        assertEquals(user.getName(), "Manuel");
    }

    @Test
    public void getSurname() throws Exception {
        assertEquals(user.getSurname(), "Peruzzi");
    }

    @Test
    public void getBirthday() throws Exception {
        assertEquals(user.getBirthday(), new DateTime(1994, 3, 7, 0, 0));
    }

}