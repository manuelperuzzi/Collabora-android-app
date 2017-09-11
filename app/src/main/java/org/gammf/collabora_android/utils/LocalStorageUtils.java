package org.gammf.collabora_android.utils;

import android.content.Context;

import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.short_collaborations.ConcreteCollaborationManager;
import org.gammf.collabora_android.short_collaborations.ShortCollaboration;
import org.gammf.collabora_android.users.User;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.short_collaborations.CollaborationsManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Manuel Peruzzi
 * Utility class providing methods to read and write collaborations data in the application local storage.
 */
public class LocalStorageUtils {

    private static final String USER_FILENAME = "user";
    private static final String COLLABORATIONS_FILENAME = "collaborations";
    private static final ExceptionManager exceptionManager = ExceptionManager.getInstance();

    /**
     * Retrieves a user from the application local storage.
     * @param context the application context used to access the application local files.
     * @return the user built from the local storage.
     */
    public static User readUserFromFile(final Context context) throws FileNotFoundException {
        User user = null;
        try {
            final JSONObject storedJson = readStoredFile(context, USER_FILENAME);
            user = UserUtils.jsonToUser(storedJson);
        } catch (final FileNotFoundException e) {
            throw e;
        } catch (final Exception e) {
            exceptionManager.handle(e);
        }
        return  user;
    }

    /**
     * Writes a user on a single file in the application local storage.
     * @param context the application context used to access the application local files.
     * @param user the user to be written.
     */
    public static void writeUserToFile(final Context context, final User user) {
        try {
            writeStoredFile(context, USER_FILENAME, UserUtils.userToJson(user));
        } catch (final Exception e) {
            exceptionManager.handle(e);
        }
    }

    public static void deleteUserFromFile(final Context context) {
        deleteStoredFile(context, USER_FILENAME);
    }

    public static void deleteAllCollaborations(final Context context) {
        final CollaborationsManager manager = LocalStorageUtils.readShortCollaborationsFromFile(context);
        if (manager != null){
            for (final ShortCollaboration collaboration : manager.getAllCollaborations()) {
                deleteStoredFile(context, collaboration.getId());
            }
            deleteStoredFile(context,COLLABORATIONS_FILENAME);
        }
    }

    /**
     * Retrieves a collaboration from the application local storage.
     * @param context the application context used to access the application local files.
     * @param collaborationId the identifier of the collaboration.
     * @return the collaboration built from the local storage.
     */
    public static Collaboration readCollaborationFromFile(final Context context, final String collaborationId) {
        Collaboration collaboration = null;
        try {
            final JSONObject storedJson = readStoredFile(context, collaborationId);
             collaboration = CollaborationUtils.jsonToCollaboration(storedJson);
        } catch (final Exception e) {
            exceptionManager.handle(e);
        }
        return collaboration;
    }

    /**
     * Writes a collaboration on a single file in the application local storage.
     * @param context the application context used to access the application local files.
     * @param collaboration the collaboration to be written.
     */
    public static void writeCollaborationToFile(final Context context, final Collaboration collaboration) {
        try {
            final JSONObject json = CollaborationUtils.collaborationToJson(collaboration);
            writeStoredFile(context, collaboration.getId(), json);
        } catch (final Exception e) {
            exceptionManager.handle(e);
        }
    }

    /**
     * Retrieves a collaborations manager with all the collaborations from the application local storage.
     * @param context the application context used to access the application local files.
     * @return a collaborations manager containing all the collaborations retrieved from file.
     */
    public static CollaborationsManager readShortCollaborationsFromFile(final Context context) {
        try {
            final JSONObject storedJson = readStoredFile(context, COLLABORATIONS_FILENAME);
            return CollaborationsManagerUtils.jsonToCollaborationManager(storedJson);
        } catch (final IOException | JSONException e) {
            return new ConcreteCollaborationManager();
        }
    }

    /**
     * Writes all the short collaborations contained in a manager on a single file in the application
     * local storage.
     * @param context the application context used to access the application local files.
     * @param manager the manager containing the collaborations to be written.
     */
    public static void writeShortCollaborationsToFile(final Context context, final CollaborationsManager manager) {
        try {
            final JSONObject json = CollaborationsManagerUtils.collaborationsManagerToJson(manager);
            writeStoredFile(context, COLLABORATIONS_FILENAME, json);
        } catch (final Exception e) {
            exceptionManager.handle(e);
        }
    }

    private static JSONObject readStoredFile(final Context context, final String filename)
            throws IOException, JSONException {
        final FileInputStream fis = context.openFileInput(filename);
        final byte[] content = new byte[fis.available()];
        if (fis.read(content, 0, content.length) <= 0) {
            throw new IOException();
        }
        return new JSONObject(new String(content));
    }

    private static void writeStoredFile(final Context context, final String filename, final JSONObject json)
            throws IOException {
        final FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        fos.write(json.toString().getBytes());
        fos.close();
    }

    private static void deleteStoredFile(final Context context, final String filename){
        context.deleteFile(filename);
    }

}
