package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.complete_collaborations.ConcreteProject;
import org.gammf.collabora_android.collaborations.complete_collaborations.Project;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.users.SimpleCollaborationMember;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author ManuelPeruzzi
 * Simple tests for the collaboration/json conversion.
 */
public class CollaborationUtilsTest {

    private Project project;

    @Before
    public void setUp() throws Exception {
        project = new ConcreteProject("myProjectId", "MyProject");

        final CollaborationMember member = new SimpleCollaborationMember("myUsername", AccessRight.ADMIN);
        project.addMember(member);
        final Note singleNote = new SimpleNoteBuilder("SingleNote", new NoteState("toDo"))
                .setNoteID("singleNoteId")
                .buildNote();
        project.addNote(singleNote);

        final Module firstModule = new ConcreteModule("firstModuleId", "FirstModule", "toDo");
        final Note firstNote = new SimpleNoteBuilder("FirstNote", new NoteState("toDo"))
                .setNoteID("firstNoteId")
                .buildNote();
        firstModule.addNote(firstNote);
        project.addModule(firstModule);

        final Module secondModule = new ConcreteModule("secondModuleId", "SecondModule", "toDo");
        final Note secondNote = new SimpleNoteBuilder("SecondNote", new NoteState("toDo"))
                .setNoteID("secondNoteId")
                .buildNote();
        secondModule.addNote(secondNote);
        project.addModule(secondModule);

        final Note thirdNote = new SimpleNoteBuilder("ThirdNote", new NoteState("toDo"))
                .setNoteID("thirdNoteId")
                .buildNote();
        project.addNote(thirdNote, firstModule.getId());
    }

    @Test
    public void collaborationToJson() throws Exception {
        final JSONObject json = CollaborationUtils.collaborationToJson(project);
        System.out.println("[CollaborationUtilsTest]: " + json);
        final Project collaboration = (Project) CollaborationUtils.jsonToCollaboration(json);
        assertEquals(project.getId(), collaboration.getId());
        assertEquals(project.getName(), collaboration.getName());
    }

}