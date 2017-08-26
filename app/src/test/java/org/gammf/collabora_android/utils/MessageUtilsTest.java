package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.notification.NotificationMessage;
import org.gammf.collabora_android.communication.notification.NotificationMessageType;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.notes.NoteUpdateMessage;
import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 08/08/2017.
 */
public class MessageUtilsTest {
    private Note note;

    @Before
    public void init() {
        this.note = new SimpleNoteBuilder("this is a test", new NoteState("doing", "fone"))
                .setExpirationDate(new DateTime(772408800000L))
                .setLocation(new NoteLocation(44.24,53.21))
                .buildNote();
    }

    @Test
    public void testMessageToJSON() {
        UpdateMessage message = new ConcreteNoteUpdateMessage("fone", this.note, UpdateMessageType.CREATION, "collaborationId");
        try {
            JSONObject jsn = MessageUtils.updateMessageToJSON(message);
            System.out.println("[MessageUtilsTest]: Update message is ->" + jsn.toString());
            JSONObject noteJSON = (JSONObject)jsn.get("note");
            assertEquals(((JSONObject)noteJSON.get("location")).getDouble("latitude"), 44.24, 0.00001);
            assertEquals(noteJSON.get("content"), "this is a test");
            assertEquals(jsn.get("target"), UpdateMessageTarget.NOTE.name());
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testJSONtoMessage() {
        try {
            final JSONObject jsn = new JSONObject().put("user", "peru")
                                                   .put("note", NoteUtils.noteToJSON(note))
                                                   .put("target", UpdateMessageTarget.NOTE.name())
                                                   .put("messageType", UpdateMessageType.CREATION.name())
                                                   .put("collaborationId", "id");

            Message message = MessageUtils.jsonToUpdateMessage(jsn);
            if(jsn.has("messageType")) {
                NoteUpdateMessage updateMessage = (NoteUpdateMessage) message;
                assertEquals(updateMessage.getUsername(), "peru");
                assertEquals(updateMessage.getNote().getExpirationDate(), new DateTime(772408800000L));
                assertEquals(updateMessage.getNote().getState().getCurrentState(), "doing");
                assertEquals(updateMessage.getUpdateType().name(), UpdateMessageType.CREATION.name());
                assertEquals(updateMessage.getNote().getLocation().getLongitude(), 53.21, 0.000001);
            } else {
                fail();
            }

        } catch (JSONException e) {
            fail();
        }
    }
}