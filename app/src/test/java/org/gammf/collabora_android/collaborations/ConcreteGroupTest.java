package org.gammf.collabora_android.collaborations;

import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.AccessRight;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.users.SimpleUser;
import org.gammf.collabora_android.users.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Peruzzi
 * Simple tests for the group implementation of a collaboration.
 */
public class ConcreteGroupTest {

    private Collaboration group;
    private User firstUser;
    private User secondUser;
    private Note note;

    @Before
    public void setUp() throws Exception {
        group = new ConcreteGroup("collaborationId", "collaborationName");
        firstUser = new SimpleUser.Builder()
                .username("peru")
                .email("manuel.peruzzi@studio.unibo.it")
                .surname("Peruzzi")
                .name("Manuel")
                .build();
        secondUser = new SimpleUser.Builder()
                .username("maffone")
                .email("alfredo.maffi@studio.unibo.it")
                .build();
        final CollaborationMember fm = new SimpleCollaborationMember(firstUser, AccessRight.ADMIN);
        final CollaborationMember sm = new SimpleCollaborationMember(secondUser, AccessRight.READ);
        group.addMember(fm);
        group.addMember(sm);
        note = new SimpleNoteBuilder("myNote")
                .setNoteID("myNoteId")
                .buildNote();
        group.addNote(note);
    }

    @Test
    public void handleName() throws Exception {
        assertEquals("collaborationName", group.getName());
        group.setName("groupName");
        assertEquals("groupName", group.getName());
    }

    @Test
    public void getAllMembers() throws Exception {
        assertEquals(2, group.getAllMembers().size());
    }

    @Test
    public void getMember() throws Exception {
        final CollaborationMember member = new SimpleCollaborationMember(firstUser, AccessRight.ADMIN);
        assertEquals(member, group.getMember(firstUser.getUsername()));
    }

    @Test
    public void removeMember() throws Exception {
        assertTrue(group.containsMember(secondUser.getUsername()));
        assertTrue(group.removeMember(secondUser.getUsername()));
        assertFalse(group.containsMember(secondUser.getUsername()));
    }

    @Test
    public void getAllNotes() throws Exception {
        assertEquals(1, group.getAllNotes().size());
    }

    @Test
    public void getNote() throws Exception {
        final Note n = new SimpleNoteBuilder("myNote")
                .setNoteID("myNoteId")
                .buildNote();
        assertEquals(n, group.getNote(n.getNoteID()));
    }

    @Test
    public void removeNote() throws Exception {
        assertTrue(group.containsNote(note.getNoteID()));
        assertTrue(group.removeNote(note.getNoteID()));
        assertFalse(group.containsNote(note.getNoteID()));
    }

}