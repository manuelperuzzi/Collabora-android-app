package org.gammf.collabora_android.model.short_collaborations;

import org.gammf.collabora_android.utils.model.CollaborationType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mattia on 16/09/2017.
 */

public class ConcreteCollaborationsManagerTest {

    private CollaborationsManager collaborationsManager;
    private static final String GROUP_COLLAB_ID = "groupCollabId";
    private static final String GROUP_COLLAB_NAME = "myGroup";

    private static final String PROJECT_COLLAB_ID = "projectCollabId";
    private static final String PROJECT_COLLAB_NAME = "myProject";

    private static final String FAKE_COLLAB_ID = "fakeId";

    private ShortCollaboration shortProjectCollaboration;
    private ShortCollaboration shortGroupCollaboration;

    @Before
    public void setUp() {
        collaborationsManager = new ConcreteCollaborationManager();

        shortGroupCollaboration = new ConcreteShortCollaboration(GROUP_COLLAB_ID, GROUP_COLLAB_NAME, CollaborationType.GROUP);
        shortProjectCollaboration = new ConcreteShortCollaboration(PROJECT_COLLAB_ID, PROJECT_COLLAB_NAME, CollaborationType.PROJECT);

        collaborationsManager.addCollaboration(shortGroupCollaboration);
        collaborationsManager.addCollaboration(shortProjectCollaboration);

    }

    @Test
    public void checkCollabIds(){
        assertEquals(2, collaborationsManager.getCollaborationsId().size());
    }

    @Test
    public void getCollaboration() {
        assertEquals(collaborationsManager.getCollaboration(GROUP_COLLAB_ID), shortGroupCollaboration);
        assertEquals(collaborationsManager.getCollaboration(PROJECT_COLLAB_ID), shortProjectCollaboration);
    }

    @Test
    public void getAllCollaborations() {
        assertEquals(2, collaborationsManager.getAllCollaborations().size());
    }

    @Test
    public void checkIfCollaborationsIsContained() {
        assertTrue(collaborationsManager.containsCollaboration(GROUP_COLLAB_ID));
        assertTrue(collaborationsManager.containsCollaboration(PROJECT_COLLAB_ID));
        assertFalse(collaborationsManager.containsCollaboration(FAKE_COLLAB_ID));
    }

    @Test
    public void removeCollaboration() {
        assertTrue(collaborationsManager.containsCollaboration(GROUP_COLLAB_ID));
        assertTrue(collaborationsManager.removeCollaboration(GROUP_COLLAB_ID));
        assertFalse(collaborationsManager.containsCollaboration(GROUP_COLLAB_ID));
    }

    @Test
    public void filterCollabByType() {
        assertEquals(1, collaborationsManager.filterByType(CollaborationType.PROJECT).size());
    }

}
