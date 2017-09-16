package org.gammf.collabora_android.app.utils;

import org.gammf.collabora_android.app.gui.MainActivity;

/**
 * Exception manger which can be accessed from anywhere.
 * More in detail, this class is meant to handle fatal exceptions that shouldn't occur while the application
 * is running (e.g. json parse error).
 */
public class ExceptionManager {

    private static final ExceptionManager EXCEPTION_MANAGER = new ExceptionManager();

    private MainActivity mainActivity;

    private ExceptionManager() { }

    public static ExceptionManager getInstance() {
        return EXCEPTION_MANAGER;
    }

    public void init(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void handle(final Exception e) {
        this.mainActivity.onLocalStorageCorrupted();
    }
}
