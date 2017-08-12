package org.gammf.collabora_android.collaborations;

import org.joda.time.DateTime;

/**
 * @author Manuel Peruzzi
 */

/**
 * Simple class representing a standard user.
 */
public class SimpleUser implements User {

    private final String username;
    private final String email;
    private final String name;
    private final String surname;
    private final DateTime birthday;

    protected SimpleUser(final String username, final String email, final String name,
                         final String surname, final DateTime birthday) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getFirstName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public DateTime getBirthday() {
        return birthday;
    }
    
}
