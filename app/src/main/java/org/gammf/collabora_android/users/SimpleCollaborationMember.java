package org.gammf.collabora_android.users;

import org.gammf.collabora_android.utils.AccessRight;
import org.gammf.collabora_android.collaborations.general.Collaboration;

/**
 * Simple class that represents a member of a {@link Collaboration} with a certain {@link AccessRight}.
 */
public class SimpleCollaborationMember implements CollaborationMember {

    private final String username;
    private final AccessRight accessRight;

    /**
     * Class constructor.
     * @param username the identifier of the member.
     * @param accessRight the access right of the member.
     */
    public SimpleCollaborationMember(final String username, final AccessRight accessRight) {
        this.username = username;
        this.accessRight = accessRight;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public AccessRight getAccessRight() {
        return accessRight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleCollaborationMember that = (SimpleCollaborationMember) o;

        return username != null ? username.equals(that.username) : that.username == null && accessRight == that.accessRight;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (accessRight != null ? accessRight.hashCode() : 0);
        return result;
    }
}
