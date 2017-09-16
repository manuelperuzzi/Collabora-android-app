package org.gammf.collabora_android.model.notes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mattia on 16/09/2017.
 */

public class NoteStateTest {

    private static final String STATE = "doing";
    private static final String DEFINITION = "Andrew Black";
    private static final String NEW_STATE = "done";
    private static final String NEW_DEFINITION = "Marcus Rashford";

    NoteState noteState;


    @Before
    public void initState() {
        noteState = new NoteState(STATE, DEFINITION);
    }

    @Test
    public void checkStateInfo() {
        assertEquals(noteState.getCurrentDefinition(), STATE);
        assertEquals(noteState.getCurrentResponsible(), DEFINITION);

    }
    
    @Test
    public void checkEquality() {
        assertTrue(noteState.equals(new NoteState(STATE, DEFINITION)));
    }

    @Test
    public void modifyAndCheckState() {
        noteState.setDefinition(NEW_STATE);
        noteState.setResponsible(NEW_DEFINITION);

        assertEquals(noteState.getCurrentDefinition(), NEW_STATE);
        assertEquals(noteState.getCurrentResponsible(), NEW_DEFINITION);
    }
}
