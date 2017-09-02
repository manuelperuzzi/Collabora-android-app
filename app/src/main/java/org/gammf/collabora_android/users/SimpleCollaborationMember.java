package org.gammf.collabora_android.users;

import org.gammf.collabora_android.utils.AccessRight;

/**
 * @author Manuel Peruzzi
 * Simple class that represents a user member of a collaboration with a certain access right.
 */
public class SimpleCollaborationMember implements CollaborationMember {

    private final String username;
    private final AccessRight accessRight;

    /**
     * Class constructor.
     * @param username the identifier of the user.
     * @param accessRight the access right of the user.
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

        if (username != null ? !username.equals(that.username) : that.username != null)
            return false;
        return accessRight == that.accessRight;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (accessRight != null ? accessRight.hashCode() : 0);
        return result;
    }
}
