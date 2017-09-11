package org.gammf.collabora_android.utils;

import android.content.Context;

import org.gammf.collabora_android.users.User;

import java.io.FileNotFoundException;

/**
 * Created by mperuzzi on 11/09/17.
 */

public class SingletonAppUser {

    private static final SingletonAppUser SINGLETON_APP_USER = new SingletonAppUser();
    private User user;

    private SingletonAppUser() { }

    public static SingletonAppUser getInstance() {
        return SINGLETON_APP_USER;
    }

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
