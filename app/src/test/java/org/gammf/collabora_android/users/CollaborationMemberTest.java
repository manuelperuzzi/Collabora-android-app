package org.gammf.collabora_android.users;

import org.gammf.collabora_android.utils.AccessRight;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Peruzzi
 * Simple tests for collaboration member class.
 */
public class CollaborationMemberTest {

    private SimpleUser user;
    private CollaborationMember member;
    private AccessRight right;

    @Before
    public void setUp() throws Exception {
        user = new SimpleUser.Builder()
                .username("peru")
                .email("manuel.peruzzi@studio.unibo.it")
                .name("Manuel")
                .surname("Peruzzi")
                .birthday(new DateTime(1994, 3, 7, 0, 0))
                .build();
        right = AccessRight.ADMIN;
        member = new CollaborationMember(user, right);
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(member.getUsername(), user.getUsername());
    }

    @Test
    public void getBirthday() throws Exception {
        assertEquals(member.getBirthday(), user.getBirthday());
    }

    @Test
    public void getAccessRight() throws Exception {
        assertEquals(member.getAccessRight(), AccessRight.ADMIN);
    }

}