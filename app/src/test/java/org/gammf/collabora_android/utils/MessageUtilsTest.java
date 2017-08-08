package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.notification.ConcreteNotificationMessage;
import org.gammf.collabora_android.communication.notification.NotificationMessage;
import org.gammf.collabora_android.communication.notification.NotificationMessageType;
import org.gammf.collabora_android.communication.update.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.UpdateMessageType;
import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 08/08/2017.
 */
public class MessageUtilsTest {
    private Note note;

    @Before
    public void init() {
        this.note = new SimpleNoteBuilder("fone")
                .setContent("this is a test")
                .setExpirationDate(new Date(772408800000L))
                .setState(new NoteState("created", "fone"))
                .setLocation(new NoteLocation(44.24,53.21))
                .buildNote();
    }

    @Test
    public void testMessageToJSON() {
        Message message = new ConcreteNoteUpdateMessage("fone", this.note, UpdateMessageType.CREATION);
        try {
            JSONObject jsn = MessageUtils.messageToJSON(message);
            System.out.println("[MessageUtilsTest]: Update message is ->" + jsn.toString());
            JSONObject noteJSON = (JSONObject)jsn.get("note");
            assertEquals(((JSONObject)noteJSON.get("location")).getDouble("latitude"), 44.24, 0.00001);
            assertEquals(noteJSON.get("content"), "this is a test");
            assertEquals(jsn.get("target"), UpdateMessageTarget.NOTE);
        } catch (JSONException e) {
            fail();
        }

        Message message2 = new ConcreteNotificationMessage("fonefone", this.note, NotificationMessageType.NOTE_CREATED);
        try {
            JSONObject jsn = MessageUtils.messageToJSON(message2);
            System.out.println("[MessageUtilsTest]: Notification message is ->" + jsn.toString());
            JSONObject noteJSON = (JSONObject)jsn.get("note");
            assertEquals(((JSONObject)noteJSON.get("location")).getDouble("latitude"), 44.24, 0.00001);
            assertEquals(noteJSON.get("content"), "this is a test");
            assertEquals(jsn.get("messageType"), NotificationMessageType.NOTE_CREATED);
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testJSONtoMessage() {
        try {
            final JSONObject jsn = new JSONObject().put("user", "peru")
                                                   .put("note", NoteUtils.noteToJSON(note))
                                                   .put("messageType", NotificationMessageType.NOTE_CREATED);

            Message message = MessageUtils.jsonToMessage(jsn);

            if(jsn.has("messageType")) {
                NotificationMessage notificationMessage = (NotificationMessage)message;
                assertEquals(notificationMessage.getNote().getUsername(), "fone");
                assertEquals(notificationMessage.getUsername(), "peru");
                assertEquals(notificationMessage.getNote().getExpirationDate(), new Date(772408800000L));
                assertEquals(notificationMessage.getNote().getState().getCurrentState(), "created");
                assertEquals(notificationMessage.getNotificationType(), NotificationMessageType.NOTE_CREATED);
                assertEquals(notificationMessage.getNote().getLocation().getLongitude(), 53.21, 0.000001);
            } else {
                fail();
            }

        } catch (JSONException e) {
            fail();
        }
    }
}