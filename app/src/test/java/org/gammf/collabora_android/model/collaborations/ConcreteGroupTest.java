package org.gammf.collabora_android.model.collaborations;

import org.gammf.collabora_android.model.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.ConcreteGroup;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.model.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.model.users.CollaborationMember;
import org.gammf.collabora_android.utils.model.CollaborationType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Peruzzi
 * Simple tests for the group implementation of a collaboration.
 */
public class ConcreteGroupTest {

    private SharedCollaboration group;
    private String firstUser = "peru";
    private String secondUser = "maffone";
    private Note note;

    @Before
    public void setUp() throws Exception {
        group = new ConcreteGroup("collaborationId", "collaborationName");
        final CollaborationMember fm = new SimpleCollaborationMember(firstUser, AccessRight.ADMIN);
        final CollaborationMember sm = new SimpleCollaborationMember(secondUser, AccessRight.READ);
        group.addMember(fm);
        group.addMember(sm);
        note = new SimpleNoteBuilder("myNote", new NoteState("doing", "fone"))
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
        assertEquals(member, group.getMember(firstUser));
    }

    @Test
    public void removeMember() throws Exception {
        assertTrue(group.containsMember(secondUser));
        assertTrue(group.removeMember(secondUser));
        assertFalse(group.containsMember(secondUser));
    }

    @Test
    public void getAllNotes() throws Exception {
        assertEquals(1, group.getAllNotes().size());
    }

    @Test
    public void getNote() throws Exception {
        final Note n = new SimpleNoteBuilder("myNote", new NoteState("doing", "fone"))
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

    @Test
    public void checkType() {
        assertEquals(group.getCollaborationType(), CollaborationType.GROUP);
    }

}