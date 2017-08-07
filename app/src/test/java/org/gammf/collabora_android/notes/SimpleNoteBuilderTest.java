package org.gammf.collabora_android.notes;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alfredo on 05/08/2017.
 */
public class SimpleNoteBuilderTest {

    @Test
    public void buildANote() {
        Note n = new SimpleNoteBuilder("username").setTitle("title")
                                                  .setContent("content")
                                                  .buildNote();
        assertEquals(n.getUsername(),"username");
        assertEquals(n.getTitle(),"title");
        assertEquals(n.getContent(),"content");
    }
}