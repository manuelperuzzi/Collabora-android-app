package org.gammf.collabora_android.model.collaborations;

import org.gammf.collabora_android.model.collaborations.private_collaborations.ConcretePrivateCollaboration;
import org.gammf.collabora_android.model.collaborations.private_collaborations.PrivateCollaboration;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.model.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.model.CollaborationType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Mattia Oriani
 * Simple tests for a concrete private collaborations.
 *
 */

public class ConcretePrivateCollaborationTest {

    private static final String PRIVATE_COLLAB_ID = "privateCollaborationID";
    private static final String PRIVATE_COLLAB_NAME = "myCollaboration";
    private static final String NEW_PRIVATE_COLLAB_NAME = "myShoppingList";

    private static final String USERNAME = "mrashford";

    private static final String FIRST_NOTE_CONTENT = "myNote";
    private static final String FIRST_NOTE_ID = "myNoteId";
    private static final String DENTIST_NOTE_CONTENT = "Go to dentist";
    private static final String DENTIST_NOTE_ID = "dentistNoteId";

    private static final String DOING_STATE = "doing";
    private static final String TODO_STATE = "todo";

    private PrivateCollaboration privateCollaboration;
    private Note myFirstNote;
    private Note dentistNote;

    @Before
    public void setUp() {
        privateCollaboration = new ConcretePrivateCollaboration(PRIVATE_COLLAB_ID, PRIVATE_COLLAB_NAME, USERNAME);
        myFirstNote = new SimpleNoteBuilder(FIRST_NOTE_CONTENT, new NoteState(DOING_STATE, USERNAME))
                .setNoteID(FIRST_NOTE_ID)
                .buildNote();
        dentistNote = new SimpleNoteBuilder(DENTIST_NOTE_CONTENT, new NoteState(TODO_STATE, USERNAME))
                .setNoteID(DENTIST_NOTE_ID)
                .buildNote();
        privateCollaboration.addNote(myFirstNote);
    }

    @Test
    public void handleName() throws Exception {
        assertEquals(PRIVATE_COLLAB_NAME, privateCollaboration.getName());
        privateCollaboration.setName(NEW_PRIVATE_COLLAB_NAME);
        assertEquals(NEW_PRIVATE_COLLAB_NAME, privateCollaboration.getName());
    }

    @Test
    public void getAllNotes() throws Exception {
        assertEquals(1, privateCollaboration.getAllNotes().size());
    }

    @Test
    public void checkIfNoteIsContained() {
        assertFalse(privateCollaboration.containsNote(dentistNote.getNoteID()));
        privateCollaboration.addNote(dentistNote);
        assertTrue(privateCollaboration.containsNote(dentistNote.getNoteID()));
    }

    @Test
    public void getNote() throws Exception {
        final Note n = new SimpleNoteBuilder(FIRST_NOTE_CONTENT, new NoteState(DOING_STATE, USERNAME))
                .setNoteID(FIRST_NOTE_ID)
                .buildNote();
        assertEquals(n, privateCollaboration.getNote(n.getNoteID()));
    }

    @Test
    public void removeNote() throws Exception {
        assertTrue(privateCollaboration.containsNote(myFirstNote.getNoteID()));
        assertTrue(privateCollaboration.removeNote(myFirstNote.getNoteID()));
        assertFalse(privateCollaboration.containsNote(myFirstNote.getNoteID()));
    }

    @Test
    public void checkType() {
        assertEquals(privateCollaboration.getCollaborationType(), CollaborationType.PRIVATE);
    }

    @Test
    public void checkUser() {
        assertEquals(privateCollaboration.getUser(), new SimpleCollaborationMember(USERNAME, AccessRight.ADMIN));
    }
}
