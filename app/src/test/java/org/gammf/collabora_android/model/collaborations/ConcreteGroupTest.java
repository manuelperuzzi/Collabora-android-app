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

    private static final String FIRST_USER = "peru";
    private static final String SECOND_USER = "maffone";

    private static final String GROUP_ID = "collaborationId";
    private static final String GROUP_NAME = "collaborationName";
    private static final String NEW_GROUP_NAME = "groupName";

    private static final String NOTE_CONTENT = "myNote";
    private static final String NOTE_ID = "myNoteId";

    private static final String DOING_STATE = "doing";

    private SharedCollaboration group;
    private Note note;

    @Before
    public void setUp() throws Exception {
        group = new ConcreteGroup(GROUP_ID, GROUP_NAME);
        final CollaborationMember fm = new SimpleCollaborationMember(FIRST_USER, AccessRight.ADMIN);
        final CollaborationMember sm = new SimpleCollaborationMember(SECOND_USER, AccessRight.READ);
        group.addMember(fm);
        group.addMember(sm);
        note = new SimpleNoteBuilder(NOTE_CONTENT, new NoteState(DOING_STATE, SECOND_USER))
                .setNoteID(NOTE_ID)
                .buildNote();
        group.addNote(note);
    }

    @Test
    public void handleName() throws Exception {
        assertEquals(GROUP_NAME, group.getName());
        group.setName(NEW_GROUP_NAME);
        assertEquals(NEW_GROUP_NAME, group.getName());
    }

    @Test
    public void getAllMembers() throws Exception {
        assertEquals(2, group.getAllMembers().size());
    }

    @Test
    public void getMember() throws Exception {
        final CollaborationMember member = new SimpleCollaborationMember(FIRST_USER, AccessRight.ADMIN);
        assertEquals(member, group.getMember(FIRST_USER));
    }

    @Test
    public void removeMember() throws Exception {
        assertTrue(group.containsMember(SECOND_USER));
        assertTrue(group.removeMember(SECOND_USER));
        assertFalse(group.containsMember(SECOND_USER));
    }

    @Test
    public void getAllNotes() throws Exception {
        assertEquals(1, group.getAllNotes().size());
    }

    @Test
    public void getNote() throws Exception {
        final Note n = new SimpleNoteBuilder(NOTE_CONTENT, new NoteState(DOING_STATE, SECOND_USER))
                .setNoteID(NOTE_ID)
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