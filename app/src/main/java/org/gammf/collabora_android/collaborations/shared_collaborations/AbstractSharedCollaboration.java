package org.gammf.collabora_android.collaborations.shared_collaborations;

import org.gammf.collabora_android.collaborations.general.AbstractCollaboration;
import org.gammf.collabora_android.users.CollaborationMember;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * This is an abstract class that defines the basic operations of a generic collaboration shared
 * between users.
 */
public abstract class AbstractSharedCollaboration extends AbstractCollaboration implements SharedCollaboration {

    private final Set<CollaborationMember> members;

    protected AbstractSharedCollaboration(final String id, final String name) {
        super(id, name);
        this.members = new HashSet<>();
    }

    @Override
    public Set<CollaborationMember> getAllMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public boolean containsMember(String username) {
        for (final CollaborationMember m: members) {
            if (m.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CollaborationMember getMember(String username) throws NoSuchElementException {
        for (final CollaborationMember m: members) {
            if (m.getUsername().equals(username)) {
                return m;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean addMember(CollaborationMember member) {
        return members.add(member);
    }

    @Override
    public boolean removeMember(String username) {
        for (final CollaborationMember m: members) {
            if (m.getUsername().equals(username)) {
                members.remove(m);
                return true;
            }
        }
        return false;
    }
}
