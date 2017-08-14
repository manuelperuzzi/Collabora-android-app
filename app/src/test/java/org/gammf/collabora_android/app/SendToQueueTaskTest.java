package org.gammf.collabora_android.app;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.update.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.UpdateMessageType;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 07/08/2017.
 */
public class SendToQueueTaskTest {
    @Test
    public void testService() {
        Note note = new SimpleNoteBuilder("some content").buildNote();
        Message message = new ConcreteNoteUpdateMessage("fone", note, UpdateMessageType.CREATION);
        new SendToQueueTask().execute(message);
    }
}