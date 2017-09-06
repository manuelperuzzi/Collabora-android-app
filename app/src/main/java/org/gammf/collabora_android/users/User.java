package org.gammf.collabora_android.users;

import org.joda.time.DateTime;

/**
 * Simple interface that represents a user of the application.
 */
public interface User {

    /**
     * @return the username
     */
    String getUsername();

    /**
     * @return the e-mail address
     */
    String getEmail();

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the surname
     */
    String getSurname();

    /**
     * @return the date of birth
     */
    DateTime getBirthday();
}