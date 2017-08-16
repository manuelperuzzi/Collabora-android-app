package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.ConcreteProject;
import org.gammf.collabora_android.collaborations.Project;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleModuleNote;
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
    private Note singleNote;
    private Module firstModule;
    private Module secondModule;
    private Note firstNote;
    private Note secondNote;
    private Note thirdNote;

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
        singleNote = new SimpleNoteBuilder("SingleNote")
                .setNoteID("singleNoteId")
                .buildNote();
        project.addNote(singleNote);

        firstModule = new ConcreteModule("firstModuleId", "FirstModule", "toDo");
        firstNote = new SimpleNoteBuilder("FirstNote")
                .setNoteID("firstNoteId")
                .buildNote();
        firstModule.addNote(firstNote);
        project.addModule(firstModule);

        secondModule = new ConcreteModule("secondModuleId", "SecondModule", "toDo");
        secondNote = new SimpleNoteBuilder("SecondNote")
                .setNoteID("secondNoteId")
                .buildNote();
        secondModule.addNote(secondNote);
        project.addModule(secondModule);

        thirdNote = new SimpleNoteBuilder("ThirdNote")
                .setNoteID("thirdNoteId")
                .buildNote();
        project.addNote(new SimpleModuleNote(thirdNote, firstModule.getId()));
    }

    @Test
    public void collaborationToJson() throws Exception {
        final JSONObject json = CollaborationUtils.collaborationToJson(project);
        System.out.println("[CollaborationUtilsTest]: " + json);
        final Project collaboration = (Project) CollaborationUtils.jsonToCollaboration(json);
        assertEquals(project.getId(), collaboration.getId());
        assertEquals(project.getName(), collaboration.getName());
        assertEquals(project.getMember(member.getUsername()), collaboration.getMember(member.getUsername()));
        assertEquals(project.getModule(firstModule.getId()), collaboration.getModule(firstModule.getId()));
        assertEquals(project.getModule(secondModule.getId()), collaboration.getModule(secondModule.getId()));
        assertEquals(project.getNote(singleNote.getNoteID()), collaboration.getNote(singleNote.getNoteID()));
        assertEquals(project.getNote(firstNote.getNoteID()), collaboration.getNote(firstNote.getNoteID()));
        assertEquals(project.getNote(secondNote.getNoteID()), collaboration.getNote(secondNote.getNoteID()));
        assertEquals(project.getNote(thirdNote.getNoteID()), collaboration.getNote(thirdNote.getNoteID()));
    }

}