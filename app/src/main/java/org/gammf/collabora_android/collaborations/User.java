package org.gammf.collabora_android.collaborations;

import org.joda.time.DateTime;

/**
 * Simple interface that represents a user of the application.
 */
public interface User {
    String getUsername();
    String getEmail();
    String getFirstName();
    String getSurname();
    DateTime getBirthday();
}