package org.gammf.collabora_android.collaborations;

import org.joda.time.DateTime;

/**
 * @author Manuel Peruzzi
 * Simple class that represents a user member of a collaboration with a certain access right.
 */
public class CollaborationMember implements User {

    private final User user;
    private final AccessRight accessRight;

    public CollaborationMember(final User user, final AccessRight accessRight) {
        this.user = user;
        this.accessRight = accessRight;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public String getSurname() {
        return user.getSurname();
    }

    @Override
    public DateTime getBirthday() {
        return user.getBirthday();
    }

    public AccessRight getAccessRight() {
        return accessRight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollaborationMember that = (CollaborationMember) o;

        if (!user.equals(that.user)) return false;
        return accessRight == that.accessRight;

    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + accessRight.hashCode();
        return result;
    }
}
