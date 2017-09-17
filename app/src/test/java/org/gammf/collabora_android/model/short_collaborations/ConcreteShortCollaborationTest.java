package org.gammf.collabora_android.model.short_collaborations;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.collaborations.private_collaborations.ConcretePrivateCollaboration;
import org.gammf.collabora_android.utils.model.CollaborationType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mattia on 16/09/2017.
 */

public class ConcreteShortCollaborationTest {

    private static final String SHORT_COLLAB_ID = "shortCollabId";
    private static final String SHORT_COLLAB_NAME = "shortcollab";

    private static final String PRIV_COLLAB_ID = "privateCollaborationID";
    private static final String PRIV_COLLAB_NAME = "myCollaboration";
    private static final String USERNAME = "mrashford";

    private ShortCollaboration shortNormalCollaboration;
    private ShortCollaboration shortPrivateCollaboration;
    private Collaboration privateCollaboration;

    @Before
    public void setUp() {
        shortNormalCollaboration = new ConcreteShortCollaboration(SHORT_COLLAB_ID, SHORT_COLLAB_NAME, CollaborationType.GROUP);

        privateCollaboration = new ConcretePrivateCollaboration(PRIV_COLLAB_ID, PRIV_COLLAB_NAME, USERNAME);
        shortPrivateCollaboration = new ConcreteShortCollaboration(privateCollaboration);

    }

    @Test
    public void checkShortNormalCollaboration() {
        assertEquals(shortNormalCollaboration.getId(), SHORT_COLLAB_ID);
        assertEquals(shortNormalCollaboration.getName(), SHORT_COLLAB_NAME);
        assertEquals(shortNormalCollaboration.getCollaborationType(), CollaborationType.GROUP);
    }

    @Test
    public void checkPrivateCollaboration() {
        assertEquals(shortPrivateCollaboration.getId(), PRIV_COLLAB_ID);
        assertEquals(shortPrivateCollaboration.getName(), PRIV_COLLAB_NAME);
        assertEquals(shortPrivateCollaboration.getCollaborationType(), privateCollaboration.getCollaborationType());
    }


}
