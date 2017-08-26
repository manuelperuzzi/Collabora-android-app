package org.gammf.collabora_android.app;

import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.junit.Test;

/**
 * @author Alfredo Maffi
 * Simple test used to test SendToMessageServerTask.
 */
public class SendMessageToServerTaskTest {
    @Test
    public void testService() {
        Note note = new SimpleNoteBuilder("some content", new NoteState("doing", "fone")).buildNote();
        UpdateMessage message = new ConcreteNoteUpdateMessage("fone", note, UpdateMessageType.CREATION, "collaborationId");
        new SendMessageToServerTask().execute(message);
    }
}