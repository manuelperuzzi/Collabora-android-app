package org.gammf.collabora_android.model.users;

import org.gammf.collabora_android.utils.model.AccessRight;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Simple tests for collaboration member class.
 */
public class CollaborationMemberTest {

    private static final String USERNAME = "peru";

    private CollaborationMember member;

    @Before
    public void setUp() throws Exception {
        member = new SimpleCollaborationMember(USERNAME, AccessRight.ADMIN);
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(USERNAME, member.getUsername());
    }

    @Test
    public void getAccessRight() throws Exception {
        assertEquals(member.getAccessRight(), AccessRight.ADMIN);
    }

}