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

    /**
     * @return the instance of the Exception Manager
     */
    public static ExceptionManager getInstance() {
        return EXCEPTION_MANAGER;
    }

    /**
     * Initialize the ExceptionManager of the application. This method have to be called on application startup
     * @param mainActivity the main activity of the application.
     */
    public void init(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * Handle and exception occurrend in the application.
     * @param e the exception
     */
    public void handle(final Exception e) {
        this.mainActivity.onLocalStorageCorrupted();
    }
}
