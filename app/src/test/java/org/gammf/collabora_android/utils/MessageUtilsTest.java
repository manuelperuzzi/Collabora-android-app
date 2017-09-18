package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.notes.NoteUpdateMessage;
import org.gammf.collabora_android.model.notes.NoteLocation;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.utils.communication.MessageUtils;
import org.gammf.collabora_android.utils.model.NoteUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Simple test of the conversions in MessageUtils class.
 */
public class MessageUtilsTest {

    private static final String DOING_STATE = "doing";
    private static final String RESPONSIBLE = "Alfredo";
    private static final String USERNAME = "peru";
    private static final String COLLABORATION_ID = "collaborationId";
    private static final String NOTE_CONTENT = "this is a test";
    private static final Long FAKE_DATETIME = 772408800000L;
    private static final Double LATITUDE = 44.24;
    private static final Double LONGITUDE = 53.21;
    private static final Double NO_COORDINATE = 0.00001;

    private Note note;

    @Before
    public void init() {
        this.note = new SimpleNoteBuilder(NOTE_CONTENT, new NoteState(DOING_STATE, RESPONSIBLE))
                .setExpirationDate(new DateTime(FAKE_DATETIME))
                .setLocation(new NoteLocation(LATITUDE,LONGITUDE))
                .buildNote();
    }

    @Test
    public void testMessageToJSON() {
        UpdateMessage message = new ConcreteNoteUpdateMessage(RESPONSIBLE, this.note, UpdateMessageType.CREATION, COLLABORATION_ID);
        try {
            JSONObject jsn = MessageUtils.updateMessageToJSON(message);
            System.out.println("[MessageUtilsTest]: Update message is ->" + jsn.toString());
            JSONObject noteJSON = (JSONObject)jsn.get("note");
            assertEquals(((JSONObject)noteJSON.get("location")).getDouble("latitude"), LATITUDE, NO_COORDINATE);
            assertEquals(noteJSON.get("content"), NOTE_CONTENT);
            assertEquals(jsn.get("target"), UpdateMessageTarget.NOTE.name());
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testJSONtoMessage() {
        try {
            final JSONObject jsn = new JSONObject().put("user", USERNAME)
                                                   .put("note", NoteUtils.noteToJSON(note))
                                                   .put("target", UpdateMessageTarget.NOTE.name())
                                                   .put("messageType", UpdateMessageType.CREATION.name())
                                                   .put("collaborationId", COLLABORATION_ID);

            Message message = MessageUtils.jsonToUpdateMessage(jsn);
            if(jsn.has("messageType")) {
                NoteUpdateMessage updateMessage = (NoteUpdateMessage) message;
                assertEquals(updateMessage.getUsername(), USERNAME);
                assertEquals(updateMessage.getNote().getExpirationDate(), new DateTime(FAKE_DATETIME));
                assertEquals(updateMessage.getNote().getState().getCurrentDefinition(), DOING_STATE);
                assertEquals(updateMessage.getNote().getState().getCurrentResponsible(), RESPONSIBLE);
                assertEquals(updateMessage.getUpdateType().name(), UpdateMessageType.CREATION.name());
                assertEquals(updateMessage.getNote().getLocation().getLongitude(), LONGITUDE, NO_COORDINATE);
            } else {
                fail();
            }

        } catch (JSONException e) {
            fail();
        }
    }
}