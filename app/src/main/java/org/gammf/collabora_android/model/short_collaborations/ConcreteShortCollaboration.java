package org.gammf.collabora_android.model.short_collaborations;

import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.utils.model.CollaborationType;

/**
 * An implementation of a {@link ShortCollaboration} defining the basic operations allowed.
 */
public class ConcreteShortCollaboration implements ShortCollaboration {

    private final String id;
    private final String name;
    private final CollaborationType collaborationType;

    /**
     * Class constructor.
     * @param id the identifier of the {@link Collaboration}.
     * @param name the name of the {@link Collaboration}.
     * @param collaborationType the type of the {@link Collaboration}.
     */
    public ConcreteShortCollaboration(final String id, final String name, final CollaborationType collaborationType) {
        this.id = id;
        this.name = name;
        this.collaborationType = collaborationType;
    }

    /**
     * Builds a class object from an existing {@link Collaboration}.
     * @param collaboration the {@link Collaboration}.
     */
    public ConcreteShortCollaboration(final Collaboration collaboration) {
        this (collaboration.getId(), collaboration.getName(), collaboration.getCollaborationType());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CollaborationType getCollaborationType() {
        return collaborationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConcreteShortCollaboration that = (ConcreteShortCollaboration) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
