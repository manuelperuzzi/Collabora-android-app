package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.ConcreteProject;
import org.gammf.collabora_android.collaborations.Project;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.users.SimpleUser;
import org.joda.time.DateTime;
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
    private CollaborationMember member;

    @Before
    public void setUp() throws Exception {
        project = new ConcreteProject("myProjectId", "MyProject");

        member = new CollaborationMember(
                new SimpleUser.Builder()
                        .username("myUsername")
                        .email("myEmail@gmail.com")
                        .name("myName")
                        .surname("mySurname")
                        .birthday(new DateTime(1994, 3, 7, 0, 0))
                        .build(), AccessRight.ADMIN);
        project.addMember(member);
        final Note singleNote = new SimpleNoteBuilder("SingleNote")
                .setNoteID("singleNoteId")
                .buildNote();
        project.addNote(singleNote);

        final Module firstModule = new ConcreteModule("firstModuleId", "FirstModule", "toDo");
        final Note firstNote = new SimpleNoteBuilder("FirstNote")
                .setNoteID("firstNoteId")
                .buildNote();
        firstModule.addNote(firstNote);
        project.addModule(firstModule);

        final Module secondModule = new ConcreteModule("secondModuleId", "SecondModule", "toDo");
        final Note secondNote = new SimpleNoteBuilder("SecondNote")
                .setNoteID("secondNoteId")
                .buildNote();
        secondModule.addNote(secondNote);
        project.addModule(secondModule);

        final Note thirdNote = new SimpleNoteBuilder("ThirdNote")
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