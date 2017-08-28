package org.gammf.collabora_android.communication.update.collaborations;

import org.gammf.collabora_android.collaborations.complete_collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.collaborations.complete_collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.utils.MessageUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Peruzzi
 * Simple tests for the update message about collaborations.
 */
public class ConcreteCollaborationUpdateMessageTest {

    private CollaborationUpdateMessage collaborationUpdateMessage;
    private Project project;

    @Before
    public void setUp() throws Exception {
        project = new ConcreteProject("myProjectId", "MyProject");

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

        collaborationUpdateMessage = new ConcreteCollaborationUpdateMessage("peru", project, UpdateMessageType.CREATION);
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals("peru", collaborationUpdateMessage.getUsername());
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