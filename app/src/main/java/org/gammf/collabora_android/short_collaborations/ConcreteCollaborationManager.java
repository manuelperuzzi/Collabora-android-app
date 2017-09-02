package org.gammf.collabora_android.short_collaborations;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * An implementation of a collaboration manager that handles all the application collaborations in
 * their short format.
 */
public class ConcreteCollaborationManager implements CollaborationsManager {

    private final Set<ShortCollaboration> collaborations;

    public ConcreteCollaborationManager() {
        collaborations = new HashSet<>();
    }

    @Override
    public Set<ShortCollaboration> getAllCollaborations() {
        return Collections.unmodifiableSet(collaborations);
    }

    @Override
    public boolean containsCollaboration(String collaborationId) {
        for (final ShortCollaboration c: collaborations) {
            if (c.getId().equals(collaborationId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ShortCollaboration getCollaboration(String collaborationId) throws NoSuchElementException {
        for (final ShortCollaboration c: collaborations) {
            if (c.getId().equals(collaborationId)) {
                return c;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean addCollaboration(ShortCollaboration collaboration) {
        return collaborations.add(collaboration);
    }

    @Override
    public boolean removeCollaboration(String collaborationId) {
        for (final ShortCollaboration c: collaborations) {
            if (c.getId().equals(collaborationId)) {
                collaborations.remove(c);
                return true;
            }
        }
        return false;
    }
}
