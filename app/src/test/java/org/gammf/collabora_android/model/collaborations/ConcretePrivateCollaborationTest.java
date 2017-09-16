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
 * Created by Mattia on 16/09/2017.
 */

public class ConcretePrivateCollaborationTest {

    private static final String USERNAME = "mrashford";
    private PrivateCollaboration privateCollaboration;
    private Note myFirstNote;
    private Note dentistNote;

    @Before
    public void setUp() {
        privateCollaboration = new ConcretePrivateCollaboration("privateCollaborationID", "myCollaboration", USERNAME);
        myFirstNote = new SimpleNoteBuilder("myNote", new NoteState("doing", USERNAME))
                .setNoteID("myNoteId")
                .buildNote();
        dentistNote = new SimpleNoteBuilder("Go to dentist", new NoteState("todo", USERNAME))
                .setNoteID("dentistNoteId")
                .buildNote();
        privateCollaboration.addNote(myFirstNote);
    }

    @Test
    public void handleName() throws Exception {
        assertEquals("myCollaboration", privateCollaboration.getName());
        privateCollaboration.setName("myShoppingList");
        assertEquals("myShoppingList", privateCollaboration.getName());
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
        final Note n = new SimpleNoteBuilder("myNote", new NoteState("doing", USERNAME))
                .setNoteID("myNoteId")
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
