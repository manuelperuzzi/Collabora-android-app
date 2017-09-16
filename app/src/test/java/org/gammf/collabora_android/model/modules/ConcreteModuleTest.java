package org.gammf.collabora_android.model.modules;

import org.gammf.collabora_android.model.notes.ModuleNote;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.NoteLocation;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleModuleNote;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Peruzzi
 * Simple tests for module implementation.
 */
public class ConcreteModuleTest {

    private Module module;
    private Note firstNote;
    private Note secondNote;

    @Before
    public void setUp() throws Exception {
        module = new ConcreteModule("moduleId", "SampleModule", "toDo");
        firstNote = new SimpleNoteBuilder("FirstNote", new NoteState("toDo"))
                .setNoteID("FirstNoteId")
                .setLocation(new NoteLocation(235.0, 456.0))
                .buildNote();
        secondNote = new SimpleNoteBuilder("SecondNote", new NoteState("toDo"))
                .setNoteID("SecondNoteId")
                .buildNote();
        module.addNote(firstNote);
        module.addNote(secondNote);
    }

    @Test
    public void getId() throws Exception {
        assertEquals("moduleId", module.getId());
    }

    @Test
    public void handleDescription() throws Exception {
        assertEquals("SampleModule", module.getDescription());
        module.setDescription("newDescription");
        assertEquals("newDescription", module.getDescription());
    }

    @Test
    public void getAllNotes() throws Exception {
        assertEquals(2, module.getAllNotes().size());
    }

    @Test
    public void getNote() throws Exception {
        final ModuleNote expectedNote = new SimpleModuleNote(firstNote, module.getId());
        final ModuleNote actualNote = module.getNote(firstNote.getNoteID());
        assertEquals(expectedNote, actualNote);
    }

    @Test
    public void removeNote() throws Exception {
        assertTrue(module.containsNote(secondNote.getNoteID()));
        assertTrue(module.removeNote(secondNote.getNoteID()));
        assertFalse(module.containsNote(secondNote.getNoteID()));
    }

    @Test
    public void checkModuleState() {
        assertEquals(module.getStateDefinition(), "toDo");
        module.setStateDefinition("Doing");
        assertEquals(module.getStateDefinition(), "Doing");
    }

}