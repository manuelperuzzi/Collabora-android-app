package org.gammf.collabora_android.app;

import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.junit.Test;

/**
 * Created by Alfredo on 07/08/2017.
 */
public class SendMessageToServerTaskTest {
    @Test
    public void testService() {
        Note note = new SimpleNoteBuilder("some content").buildNote();
        UpdateMessage message = new ConcreteNoteUpdateMessage("fone", note, UpdateMessageType.CREATION, "collaborationId");
        new SendMessageToServerTask().execute(message);
    }
}