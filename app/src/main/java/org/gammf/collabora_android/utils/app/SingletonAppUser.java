package org.gammf.collabora_android.utils.app;

import android.content.Context;

import org.gammf.collabora_android.model.users.User;

import java.io.FileNotFoundException;

/**
 * Singleton class which represents the application {@link User}, accessible from anywhere.
 */

public class SingletonAppUser {

    private static final SingletonAppUser SINGLETON_APP_USER = new SingletonAppUser();
    private User user;

    private SingletonAppUser() { }

    public static SingletonAppUser getInstance() {
        return SINGLETON_APP_USER;
    }

    /**
     * Read all the {@link User}'s related information from local storage, if present.
     * @param context the application context, needed to read from local storage.
     * @throws FileNotFoundException thrown if no {@link User} was found.
     */
    public void loadUser(final Context context) throws FileNotFoundException {
        user = LocalStorageUtils.readUserFromFile(context);
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }
}
