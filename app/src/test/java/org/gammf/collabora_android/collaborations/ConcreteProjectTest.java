package org.gammf.collabora_android.collaborations;

import org.gammf.collabora_android.collaborations.complete_collaborations.ConcreteProject;
import org.gammf.collabora_android.collaborations.complete_collaborations.Project;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleModuleNote;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Peruzzi
 * Simple tests for the project implementation of a collaboration.
 */
public class ConcreteProjectTest {

    private Project project;
    private Note singleNote;
    private Module firstModule;
    private Module secondModule;
    private Note firstNote;
    private Note thirdNote;

    @Before
    public void setUp() throws Exception {
        project = new ConcreteProject("myProjectId", "MyProject");

        singleNote = new SimpleNoteBuilder("SingleNote", new NoteState("toDo"))
                .setNoteID("singleNoteId")
                .buildNote();
        project.addNote(singleNote);

        firstModule = new ConcreteModule("firstModuleId", "FirstModule", "toDo");
        firstNote = new SimpleNoteBuilder("FirstNote", new NoteState("toDo"))
                .setNoteID("firstNoteId")
                .buildNote();
        firstModule.addNote(firstNote);
        project.addModule(firstModule);

        secondModule = new ConcreteModule("secondModuleId", "SecondModule", "toDo");
        final Note secondNote = new SimpleNoteBuilder("SecondNote", new NoteState("toDo"))
                .setNoteID("secondNoteId")
                .buildNote();
        secondModule.addNote(secondNote);
        project.addModule(secondModule);

        thirdNote = new SimpleNoteBuilder("ThirdNote", new NoteState("toDo"))
                .setNoteID("thirdNoteId")
                .buildNote();
        project.addNote(thirdNote, firstModule.getId());
    }

    @Test
    public void getAllModules() throws Exception {
        assertEquals(2, project.getAllModules().size());
    }

    @Test
    public void getModule() throws Exception {
        assertEquals(firstModule, project.getModule(firstModule.getId()));
    }

    @Test
    public void removeModule() throws Exception {
        assertTrue(project.containsModule(secondModule.getId()));
        assertTrue(project.removeModule(secondModule.getId()));
        assertFalse(project.containsModule(secondModule.getId()));
    }

    @Test
    public void getAllNotes() throws Exception {
        assertEquals(4, project.getAllNotes().size());
    }

    @Test
    public void getNote() throws Exception {
        assertEquals(singleNote, project.getNote(singleNote.getNoteID()));
        final ModuleNote mn = new SimpleModuleNote(firstNote, firstModule.getId());
        assertEquals(mn, project.getNote(firstNote.getNoteID()));
    }

    @Test
    public void removeNote() throws Exception {
        assertTrue(project.removeNote(singleNote.getNoteID()));
        assertFalse(project.containsNote(singleNote.getNoteID()));

        assertTrue(project.removeNote(thirdNote.getNoteID()));
        assertFalse(project.containsNote(thirdNote.getNoteID()));
    }

}