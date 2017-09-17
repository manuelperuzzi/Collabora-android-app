package org.gammf.collabora_android.communication.update.collaborations;

import org.gammf.collabora_android.model.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.model.modules.ConcreteModule;
import org.gammf.collabora_android.model.modules.Module;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.model.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.communication.MessageUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Peruzzi
 * Simple tests for the update message about collaborations.
 */
public class ConcreteCollaborationUpdateMessageTest {

    private static final String CONCRETE_PROJECT_ID = "myProjectId";
    private static final String CONCRETE_PROJECT_NAME = "MyProject";

    private static final String MEMBER_USERNAME = "maffone";
    private static final String UPDATER_USER = "peru";

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


    private CollaborationUpdateMessage collaborationUpdateMessage;
    private Project project;

    @Before
    public void setUp() throws Exception {
        project = new ConcreteProject(CONCRETE_PROJECT_ID, CONCRETE_PROJECT_NAME);

        project.addMember(new SimpleCollaborationMember(MEMBER_USERNAME, AccessRight.ADMIN));

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

        collaborationUpdateMessage = new ConcreteCollaborationUpdateMessage(UPDATER_USER, project, UpdateMessageType.CREATION);
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(UPDATER_USER, collaborationUpdateMessage.getUsername());
    }

    @Test
    public void getMessageType() throws Exception {
        assertEquals(MessageType.UPDATE, collaborationUpdateMessage.getMessageType());
    }

    @Test
    public void getUpdateType() throws Exception {
        assertEquals(UpdateMessageType.CREATION, collaborationUpdateMessage.getUpdateType());
    }

    @Test
    public void getTarget() throws Exception {
        assertEquals(UpdateMessageTarget.COLLABORATION, collaborationUpdateMessage.getTarget());
    }

    @Test
    public void getCollaboration() throws Exception {
        assertEquals(project, collaborationUpdateMessage.getCollaboration());
    }

    @Test
    public void collaborationUpdateMessageToJson() throws Exception {
        final JSONObject json = MessageUtils.updateMessageToJSON(collaborationUpdateMessage);
        System.out.println("[ConcreteCollaborationUpdateMessageTest]: " + json);
        final CollaborationUpdateMessage message = (CollaborationUpdateMessage) MessageUtils.jsonToUpdateMessage(json);
        assertEquals(collaborationUpdateMessage.getCollaboration().getId(), message.getCollaboration().getId());
    }

}