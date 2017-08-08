package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.update.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.UpdateMessageType;
import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 08/08/2017.
 */
public class MessageUtilsTest {
    @Test
    public void testMessageToJSON() {
        Note note = new SimpleNoteBuilder("fone")
                .setContent("this is a test")
                .setExpirationDate(new Date(772408800000L))
                .setState(new NoteState("created", "fone"))
                .setLocation(new NoteLocation(44.24,53.21))
                .buildNote();
        Message message = new ConcreteNoteUpdateMessage("fone", note, UpdateMessageType.CREATION);
        try {
            JSONObject jsn = MessageUtils.messageToJSON(message);
            System.out.println(jsn.toString());
            JSONObject noteJSON = (JSONObject)jsn.get("note");
            assertEquals(((JSONObject)noteJSON.get("location")).getDouble("latitude"), 44.24, 0.00001);
            assertEquals(noteJSON.get("content"), "this is a test");
            assertEquals(jsn.get("target"), UpdateMessageTarget.NOTE);
        } catch (JSONException e) {
            fail();
        }
    }
}