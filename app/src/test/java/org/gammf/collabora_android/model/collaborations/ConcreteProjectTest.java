package org.gammf.collabora_android.model.collaborations;

import org.gammf.collabora_android.model.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.model.modules.ConcreteModule;
import org.gammf.collabora_android.model.modules.Module;
import org.gammf.collabora_android.model.notes.ModuleNote;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleModuleNote;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.model.users.CollaborationMember;
import org.gammf.collabora_android.model.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.model.CollaborationType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Peruzzi
 * Simple tests for the project implementation of a collaboration.
 */
public class ConcreteProjectTest {

    private static final String FIRST_USER = "Wayne Rooney";
    private static final String SECOND_USER = "Nelson Dida";

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

        final CollaborationMember firstMember = new SimpleCollaborationMember(FIRST_USER, AccessRight.ADMIN);
        final CollaborationMember secondMember = new SimpleCollaborationMember(SECOND_USER, AccessRight.READ);
        project.addMember(firstMember);
        project.addMember(secondMember);
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

    @Test
    public void checkCollaborationType() {
        assertEquals(project.getCollaborationType(), CollaborationType.PROJECT);
    }

    @Test
    public void getAllMembers() throws Exception {
        assertEquals(2, project.getAllMembers().size());
    }

    @Test
    public void getMember() throws Exception {
        final CollaborationMember member = new SimpleCollaborationMember(FIRST_USER, AccessRight.ADMIN);
        assertEquals(member, project.getMember(FIRST_USER));
    }

    @Test
    public void removeMember() throws Exception {
        assertTrue(project.containsMember(SECOND_USER));
        assertTrue(project.removeMember(SECOND_USER));
        assertFalse(project.containsMember(SECOND_USER));
    }

}