package org.gammf.collabora_android.app;

import android.content.Context;
import android.os.AsyncTask;

import org.gammf.collabora_android.collaborations.Collaboration;
import org.gammf.collabora_android.collaborations.Project;
import org.gammf.collabora_android.communication.update.collaborations.CollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.members.MemberUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.notes.NoteUpdateMessage;
import org.gammf.collabora_android.utils.CollaborationUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Alfredo Maffi, Manuel Peruzzi
 * This is an asynchronous task that parses an update message, storing in the local application
 * memory the new collaborations data deriving from the server notification.
 */
public class StoreNotificationsTask extends AsyncTask<UpdateMessage, Void, Boolean> {

    private final Context context;

    /**
     * Async task constructor.
     * @param context the application context used to access the application local files.
     */
    public StoreNotificationsTask(final Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(UpdateMessage... updateMessages) {
        final UpdateMessage message = updateMessages[0];

        try {
            final Collaboration storedCollaboration = readCollaborationFromFile(message.getCollaborationId());
            switch (message.getTarget()) {
                case NOTE:
                    return storeUpdatedNote((NoteUpdateMessage) message, storedCollaboration);
                case MODULE:
                    return storeUpdatedModule((ModuleUpdateMessage) message, storedCollaboration);
                case COLLABORATION:
                    return storeUpdatedCollaboration((CollaborationUpdateMessage) message);
                case MEMBER:
                    return storeUpdatedMember((MemberUpdateMessage) message, storedCollaboration);
                default:
                    return false;
            }
        } catch (final IOException | JSONException e) {
            return false;
        }
    }

    private boolean storeUpdatedNote(final NoteUpdateMessage message, final Collaboration storedCollaboration)
            throws IOException, JSONException {
        switch (message.getUpdateType()) {
            case CREATION:
                storedCollaboration.addNote(message.getNote());
                break;
            case UPDATING:
                storedCollaboration.removeNote(message.getNote().getNoteID());
                storedCollaboration.addNote(message.getNote());
                break;
            case DELETION:
                storedCollaboration.removeNote(message.getNote().getNoteID());
                break;
            default:
                return false;
        }

        writeCollaborationToFile(storedCollaboration);
        return true;
    }

    private boolean storeUpdatedModule(final ModuleUpdateMessage message, final Collaboration storedCollaboration)
            throws IOException, JSONException {
        if (storedCollaboration instanceof Project) {
            final Project project = (Project) storedCollaboration;

            switch (message.getUpdateType()) {
                case CREATION:
                    project.addModule(message.getModule());
                    break;
                case UPDATING:
                    project.removeModule(message.getModule().getId());
                    project.addModule(message.getModule());
                    break;
                case DELETION:
                    project.removeModule(message.getModule().getId());
                    break;
                default:
                    return false;
            }

            writeCollaborationToFile(storedCollaboration);
            return true;
        }
        return false;
    }

    private boolean storeUpdatedMember(final MemberUpdateMessage message, final Collaboration storedCollaboration)
            throws IOException, JSONException {
        switch (message.getUpdateType()) {
            case CREATION:
                storedCollaboration.addMember(message.getMember());
                break;
            case UPDATING:
                storedCollaboration.removeMember(message.getMember().getUsername());
                storedCollaboration.addMember(message.getMember());
                break;
            case DELETION:
                storedCollaboration.removeMember(message.getMember().getUsername());
                break;
            default:
                return false;
        }

        writeCollaborationToFile(storedCollaboration);
        return true;
    }

    private boolean storeUpdatedCollaboration(final CollaborationUpdateMessage message)
            throws IOException, JSONException {
        switch (message.getUpdateType()) {
            case UPDATING:
                final JSONObject json = CollaborationUtils.collaborationToJson(message.getCollaboration());
                // TODO update collaboration in the general file
                writeStoredFile(message.getCollaborationId(), json);
                return true;
            case DELETION:
                context.deleteFile(message.getCollaborationId());
                // TODO remove collaboration from general file
                return true;
            default:
                return false;
        }
    }

    private Collaboration readCollaborationFromFile(final String collaborationId) throws IOException, JSONException {
        final JSONObject storedJson = readStoredFile(collaborationId);
        return CollaborationUtils.jsonToCollaboration(storedJson);
    }

    private JSONObject readStoredFile(final String filename) throws IOException, JSONException {
        final FileInputStream fis = context.openFileInput(filename);
        final byte[] content = new byte[fis.available()];
        if (fis.read(content, 0, content.length) <= 0) {
            throw new IOException();
        }
        return new JSONObject(new String(content));
    }

    private void writeCollaborationToFile(final Collaboration collaboration) throws IOException, JSONException{
        final JSONObject json = CollaborationUtils.collaborationToJson(collaboration);
        writeStoredFile(collaboration.getId(), json);
    }

    private void writeStoredFile(final String filename, final JSONObject json) throws IOException {
        final FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        fos.write(json.toString().getBytes());
        fos.close();
    }

}
