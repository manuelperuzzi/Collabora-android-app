package org.gammf.collabora_android.app;

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
        Note note = new SimpleNoteBuilder("userID").setTitle("ciao").setContent("Un po' di contenuto").buildNote();
        new SendToQueueTask().execute(note);
    }
}