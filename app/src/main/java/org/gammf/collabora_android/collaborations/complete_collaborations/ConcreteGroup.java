package org.gammf.collabora_android.collaborations.complete_collaborations;

/**
 * @author Manuel Peruzzi
 * A simple implementation of a collaboration of type group.
 */
public class ConcreteGroup extends AbstractSharedCollaboration implements Group {

    /**
     * Class constructor.
     * @param id the identifier of the group.
     * @param name the name of the group.
     */
    public ConcreteGroup(final String id, final String name) {
        super(id, name);
    }

}
