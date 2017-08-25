package org.gammf.collabora_android.utils;

import android.content.Context;

import org.gammf.collabora_android.collaborations.Collaboration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Manuel Peruzzi
 * Utility class providing methods to read and write collaborations data in the application local storage.
 */
public class LocalStorageUtils {

    /**
     * Retrieve a collaboration from the application local storage.
     * @param context the application context used to access the application local files.
     * @param collaborationId the identifier of the collaboration.
     * @return the collaboration built from the local storage.
     * @throws IOException if the file reading from file went wrong.
     * @throws JSONException if the json conversion went wrong.
     */
    public static Collaboration readCollaborationFromFile(final Context context, final String collaborationId)
            throws IOException, JSONException {
        final JSONObject storedJson = readStoredFile(context, collaborationId);
        return CollaborationUtils.jsonToCollaboration(storedJson);
    }

    /**
     * Writes a collaboration in a single file in the application local storage.
     * @param context the application context used to access the application local files.
     * @param collaboration the collaboration to be written.
     * @throws IOException if the file writing went wrong.
     * @throws JSONException if the json conversion went wrong.
     */
    public static void writeCollaborationToFile(final Context context, final Collaboration collaboration)
            throws IOException, JSONException{
        final JSONObject json = CollaborationUtils.collaborationToJson(collaboration);
        writeStoredFile(context, collaboration.getId(), json);
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

}
