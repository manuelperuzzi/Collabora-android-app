package org.gammf.collabora_android.notes;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 05/08/2017.
 */
public class SimpleNoteBuilderTest {

    @Test
    public void buildANote() {
        Note n = new SimpleNoteBuilder("username").setContent("content")
                                                  .setLocation(new NoteLocation(42.22, 55.23))
                                                  .buildNote();
        assertEquals(n.getUsername(),"username");
        assertEquals(n.getContent(),"content");
        assertEquals(n.getLocation().getLatitude(), 42.22, 0.000001);
        assertEquals(n.getLocation().getLongitude(), 55.23, 0.000001);
    }
}