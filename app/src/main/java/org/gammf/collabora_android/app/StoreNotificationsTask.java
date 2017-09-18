package org.gammf.collabora_android.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.gammf.collabora_android.app.alarm.AlarmController;
import org.gammf.collabora_android.app.gui.MainActivity;
import org.gammf.collabora_android.app.utils.IntentConstants;
import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.all_collaborations.AllCollaborationsMessage;
import org.gammf.collabora_android.communication.collaboration.CollaborationMessage;
import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.error.ErrorMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.model.modules.Module;
import org.gammf.collabora_android.model.notes.ModuleNote;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.model.short_collaborations.ConcreteCollaborationManager;
import org.gammf.collabora_android.model.short_collaborations.ConcreteShortCollaboration;
import org.gammf.collabora_android.communication.update.collaborations.CollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.members.MemberUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.notes.NoteUpdateMessage;
import org.gammf.collabora_android.utils.app.AlarmUtils;
import org.gammf.collabora_android.utils.model.CollaborationType;
import org.gammf.collabora_android.utils.app.GeofenceUtils;
import org.gammf.collabora_android.utils.app.LocalStorageUtils;
import org.gammf.collabora_android.utils.app.SingletonAppUser;

import java.util.Set;

/**
 * This is an asynchronous task that parses an {@link UpdateMessage}, storing in the local application
 * memory new/updated data deriving from the server notification.
 */
public class StoreNotificationsTask extends AsyncTask<Message, Void, Boolean> {

    private final Context context;
    private GeofenceManager geoManager;
    private AlarmController alarmController;
    private String collaborationId;
    private UpdateMessageType updateType = null;
    private String senderUsername;

    /**
     * Async task constructor.
     * @param context the application context, used to access the application local files.
     */
    public StoreNotificationsTask(final Context context) {
        this.context = context;
        this.alarmController = new AlarmController();
    }

    @Override
    protected Boolean doInBackground(final Message... messages) {
        final Message message = messages[0];
        this.geoManager = new GeofenceManager(context);

        senderUsername = message.getUsername();
        switch (message.getMessageType()) {
            case UPDATE:
                final UpdateMessage msg = (UpdateMessage) message;
                updateType = msg.getUpdateType();
                handleUpdateMessage(msg);
                return true;
            case COLLABORATION:
                handleCollaborationMessage((CollaborationMessage)message);
                return true;
            case ERROR:
                handleErrorMessage((ErrorMessage)message);
                return false;
            case ALL_COLLABORATIONS:
                handleAllCollaborationsMessage((AllCollaborationsMessage)message);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success && SingletonAppUser.getInstance().getUsername().equals(senderUsername)) {
            final Intent intent = new Intent(MainActivity.getReceiverIntentFilter());
            if(collaborationId != null) {
                intent.putExtra(IntentConstants.NETWORK_MESSAGE_RECEIVED, collaborationId);
                if (updateType == UpdateMessageType.DELETION) {
                    intent.putExtra(IntentConstants.COLLABORATION_DELETION, "");
                }
            }
            intent.putExtra(IntentConstants.MAIN_ACTIVITY_TAG, IntentConstants.NETWORK_MESSAGE_RECEIVED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    private void handleErrorMessage(final ErrorMessage message) {
        final Intent intent = new Intent(MainActivity.getReceiverIntentFilter());
        intent.putExtra(IntentConstants.MAIN_ACTIVITY_TAG, IntentConstants.SERVER_ERROR);
        intent.putExtra(IntentConstants.SERVER_ERROR, message.getError().getErrorResource());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void handleAllCollaborationsMessage(final AllCollaborationsMessage message) {
        final CollaborationsManager manager = new ConcreteCollaborationManager();
        for (final Collaboration c: message.getCollaborationList()) {
            manager.addCollaboration(new ConcreteShortCollaboration(c));
            LocalStorageUtils.writeCollaborationToFile(context, c);
            for (Note note: c.getAllNotes()) {
                AlarmUtils.setAlarm(context, note, alarmController);
                GeofenceUtils.setGeofence(note, geoManager);
            }
        }
        LocalStorageUtils.writeShortCollaborationsToFile(context, manager);
    }

    private void handleCollaborationMessage(final CollaborationMessage message) {
        final CollaborationsManager manager = LocalStorageUtils.readShortCollaborationsFromFile(context);
        manager.addCollaboration(new ConcreteShortCollaboration(message.getCollaboration()));
        LocalStorageUtils.writeCollaborationToFile(context, message.getCollaboration());
        for (Note note: message.getCollaboration().getAllNotes()) {
            AlarmUtils.setAlarm(context, note, alarmController);
            GeofenceUtils.setGeofence(note, geoManager);
        }
        LocalStorageUtils.writeShortCollaborationsToFile(context, manager);
    }

    private boolean handleUpdateMessage(final UpdateMessage message) {
        collaborationId = message.getCollaborationId();
        final Collaboration storedCollaboration = LocalStorageUtils.readCollaborationFromFile(
                context, message.getCollaborationId());
        switch (message.getTarget()) {
            case NOTE:
                return storeUpdatedNote((NoteUpdateMessage) message, storedCollaboration);
            case MODULE:
                return storedCollaboration.getCollaborationType().equals(CollaborationType.PROJECT) &&
                        storeUpdatedModule((ModuleUpdateMessage) message, (Project) storedCollaboration);
            case COLLABORATION:
                return storeUpdatedCollaboration((CollaborationUpdateMessage) message);
            case MEMBER:
                return !(storedCollaboration.getCollaborationType().equals(CollaborationType.PRIVATE)) &&
                        storeUpdatedMember((MemberUpdateMessage) message, (SharedCollaboration) storedCollaboration);
            default:
                return false;
        }
    }

    private boolean storeUpdatedNote(final NoteUpdateMessage message, final Collaboration storedCollaboration) {
        switch (message.getUpdateType()) {
            case CREATION:
                storedCollaboration.addNote(message.getNote());
                AlarmUtils.setAlarm(context, message.getNote(), alarmController);
                GeofenceUtils.setGeofence(message.getNote(), geoManager);
                break;
            case UPDATING:
                storedCollaboration.removeNote(message.getNote().getNoteID());
                storedCollaboration.addNote(message.getNote());
                AlarmUtils.updateAlarm(context, message.getNote(), alarmController);
                GeofenceUtils.updateGeofence(message.getNote(), geoManager);
                break;
            case DELETION:
                storedCollaboration.removeNote(message.getNote().getNoteID());
                AlarmUtils.deleteAlarm(context, message.getNote(), alarmController);
                GeofenceUtils.deleteGeofence(message.getNote(), geoManager);
                break;
            default:
                return false;
        }

        LocalStorageUtils.writeCollaborationToFile(context, storedCollaboration);
        return true;
    }

    private boolean storeUpdatedModule(final ModuleUpdateMessage message, final Project storedCollaboration) {
        switch (message.getUpdateType()) {
            case CREATION:
                storedCollaboration.addModule(message.getModule());
                for (Note note: message.getModule().getAllNotes()) {
                    AlarmUtils.setAlarm(context, note, alarmController);
                    GeofenceUtils.setGeofence(note, geoManager);
                }
                break;
            case UPDATING:
                final Set<ModuleNote> moduleNotes = storedCollaboration.getModule(message.getModule().getId()).getAllNotes();
                storedCollaboration.removeModule(message.getModule().getId());
                final Module newModule = message.getModule();
                for (final Note note: moduleNotes) {
                    newModule.addNote(note);
                    AlarmUtils.updateAlarm(context, note, alarmController);
                    GeofenceUtils.updateGeofence(note, geoManager);
                }
                storedCollaboration.addModule(newModule);
                break;
            case DELETION:
                storedCollaboration.removeModule(message.getModule().getId());
                for (Note note: message.getModule().getAllNotes()) {
                    AlarmUtils.deleteAlarm(context, note, alarmController);
                    GeofenceUtils.deleteGeofence(note, geoManager);
                }
                break;
            default:
                return false;
        }

        LocalStorageUtils.writeCollaborationToFile(context, storedCollaboration);
        return true;
    }

    private boolean storeUpdatedMember(final MemberUpdateMessage message, final SharedCollaboration storedCollaboration) {
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

        LocalStorageUtils.writeCollaborationToFile(context, storedCollaboration);
        return true;
    }

    private boolean storeUpdatedCollaboration(final CollaborationUpdateMessage message) {
        final CollaborationsManager manager = LocalStorageUtils.readShortCollaborationsFromFile(context);

        switch (message.getUpdateType()) {
            case UPDATING:
                manager.removeCollaboration(message.getCollaborationId());
                manager.addCollaboration(new ConcreteShortCollaboration(message.getCollaboration()));
                LocalStorageUtils.writeCollaborationToFile(context, message.getCollaboration());
                for (Note note: message.getCollaboration().getAllNotes()) {
                    AlarmUtils.updateAlarm(context, note, alarmController);
                    GeofenceUtils.updateGeofence(note, geoManager);
                }
                break;
            case DELETION:
                manager.removeCollaboration(message.getCollaborationId());
                context.deleteFile(message.getCollaborationId());
                for (Note note: message.getCollaboration().getAllNotes()) {
                    AlarmUtils.deleteAlarm(context, note, alarmController);
                    GeofenceUtils.deleteGeofence(note, geoManager);
                }
                break;
            default:
                return false;
        }

        LocalStorageUtils.writeShortCollaborationsToFile(context, manager);
        return true;
    }
}
