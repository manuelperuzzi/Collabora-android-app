package org.gammf.collabora_android.model.short_collaborations;

import org.gammf.collabora_android.utils.model.CollaborationType;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * An implementation of a {@link CollaborationsManager} that handles all the application {@link Collaboration}s in
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

    @Override
    public List<ShortCollaboration> filterByGroup(final CollaborationType collaborationType) {
        final List<ShortCollaboration> collaborations = new ArrayList<>();
        for (final ShortCollaboration collaboration : this.getAllCollaborations()) {
            if (collaboration.getCollaborationType().equals(collaborationType)) {
                collaborations.add(collaboration);
            }
        }
        return collaborations;
    }

    @Override
    public List<String> getCollaborationsId() {
        final List<String> collaborationIds = new ArrayList<>();
        for (final ShortCollaboration collaboration : this.getAllCollaborations()) {
            collaborationIds.add(collaboration.getId());
        }
        return collaborationIds;
    }
}
