package org.gammf.collabora_android.utils;

/**
 * @author Manuel Peruzzi
 * Defines the level of access right of a user in a collaboration.
 */
public enum AccessRight {

    /**
     * Includes write privilege, can alter the whole collaboration and its members.
     */
    ADMIN,

    /**
     * Can read and modify the notes in the collaboration.
     */
    WRITE,

    /**
     * Can only read the notes in the collaboration.
     */
    READ

}
