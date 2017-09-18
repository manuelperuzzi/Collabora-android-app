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

    private static final String DOING_STATE = "doing";
    private static final String TODO_STATE = "todo";

    private static final String MODULE_ID = "moduleId";
    private static final String MODULE_NAME = "SampleModule";
    private static final String NEW_MODULE_NAME = "NewModuleName";
    private static final String FIRST_NOTE_CONTENT = "myNote";
    private static final String FIRST_NOTE_ID = "myNoteId";
    private static final String SECOND_NOTE_CONTENT = "SecondNote";
    private static final String SECOND_NOTE_ID = "secondNoteId";

    private static final Double LATITUDE = 235.0;
    private static final Double LONGITUDE = 456.0;

    private Module module;
    private Note firstNote;
    private Note secondNote;

    @Before
    public void setUp() throws Exception {
        module = new ConcreteModule(MODULE_ID, MODULE_NAME, TODO_STATE);
        firstNote = new SimpleNoteBuilder(FIRST_NOTE_CONTENT, new NoteState(TODO_STATE))
                .setNoteID(FIRST_NOTE_ID)
                .setLocation(new NoteLocation(LATITUDE, LONGITUDE))
                .buildNote();
        secondNote = new SimpleNoteBuilder(SECOND_NOTE_CONTENT, new NoteState(TODO_STATE))
                .setNoteID(SECOND_NOTE_ID)
                .buildNote();
        module.addNote(firstNote);
        module.addNote(secondNote);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(MODULE_ID, module.getId());
    }

    @Test
    public void handleDescription() throws Exception {
        assertEquals(MODULE_NAME, module.getDescription());
        module.setDescription(NEW_MODULE_NAME);
        assertEquals(NEW_MODULE_NAME, module.getDescription());
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
        assertEquals(module.getStateDefinition(), TODO_STATE);
        module.setStateDefinition(DOING_STATE);
        assertEquals(module.getStateDefinition(), DOING_STATE);
    }

}