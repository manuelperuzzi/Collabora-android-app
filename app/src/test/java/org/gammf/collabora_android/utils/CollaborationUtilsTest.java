package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.model.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.model.modules.ConcreteModule;
import org.gammf.collabora_android.model.modules.Module;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.model.users.CollaborationMember;
import org.gammf.collabora_android.model.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.model.CollaborationUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author ManuelPeruzzi
 * Simple tests for the collaboration/json conversion.
 */
public class CollaborationUtilsTest {

    private static final String CONCRETE_PROJECT_ID = "myProjectId";
    private static final String CONCRETE_PROJECT_NAME = "MyProject";

    private static final String MEMBER_USERNAME = "myUsername";

    private static final String TODO_STATE = "toDo";

    private static final String SINGLE_NOTE_CONTENT = "SingleNote";
    private static final String SINGLE_NOTE_ID = "singleNoteId";

    private static final String FIRST_MODULE_ID = "firstModuleId";
    private static final String FIRST_MODULE_NAME = "FirstModule";
    private static final String SECOND_MODULE_ID = "secondModuleId";
    private static final String SECOND_MODULE_NAME = "SecondModule";

    private static final String FIRST_NOTE_CONTENT = "FirstNote";
    private static final String FIRST_NOTE_ID = "firstNoteId";

    private static final String SECOND_NOTE_CONTENT = "SecondNote";
    private static final String SECOND_NOTE_ID = "secondNoteId";

    private static final String THIRD_NOTE_CONTENT = "ThirdNote";
    private static final String THIRD_NOTE_ID = "thirdNoteId";

    private Project project;

    @Before
    public void setUp() throws Exception {
        project = new ConcreteProject(CONCRETE_PROJECT_ID, CONCRETE_PROJECT_NAME);

        final CollaborationMember member = new SimpleCollaborationMember(MEMBER_USERNAME, AccessRight.ADMIN);
        project.addMember(member);
        final Note singleNote = new SimpleNoteBuilder(SINGLE_NOTE_CONTENT, new NoteState(TODO_STATE))
                .setNoteID(SINGLE_NOTE_ID)
                .buildNote();
        project.addNote(singleNote);

        final Module firstModule = new ConcreteModule(FIRST_MODULE_ID, FIRST_MODULE_NAME, TODO_STATE);
        final Note firstNote = new SimpleNoteBuilder(FIRST_NOTE_CONTENT, new NoteState(TODO_STATE))
                .setNoteID(FIRST_NOTE_ID)
                .buildNote();
        firstModule.addNote(firstNote);
        project.addModule(firstModule);

        final Module secondModule = new ConcreteModule(SECOND_MODULE_ID, SECOND_MODULE_NAME, TODO_STATE);
        final Note secondNote = new SimpleNoteBuilder(SECOND_NOTE_CONTENT, new NoteState(TODO_STATE))
                .setNoteID(SECOND_NOTE_ID)
                .buildNote();
        secondModule.addNote(secondNote);
        project.addModule(secondModule);

        final Note thirdNote = new SimpleNoteBuilder(THIRD_NOTE_CONTENT, new NoteState(TODO_STATE))
                .setNoteID(THIRD_NOTE_ID)
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