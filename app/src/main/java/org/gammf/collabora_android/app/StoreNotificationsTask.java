package org.gammf.collabora_android.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;

import org.gammf.collabora_android.app.alarm.Alarm;
import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.collaboration.CollaborationMessage;
import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.short_collaborations.ConcreteShortCollaboration;
import org.gammf.collabora_android.communication.update.collaborations.CollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.members.MemberUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.notes.NoteUpdateMessage;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;

/**
 * @author Alfredo Maffi, Manuel Peruzzi
 * This is an asynchronous task that parses an update message, storing in the local application
 * memory the new collaborations data deriving from the server notification.
 */
public class StoreNotificationsTask extends AsyncTask<Message, Void, Boolean> {

    private final Context context;
    private GeofenceManager geoManager;
    private Alarm alarm;


    /**
     * Async task constructor.
     * @param context the application context, used to access the application local files.
     */
    public StoreNotificationsTask(final Context context) {
        this.context = context;
        this.alarm = new Alarm();
    }

    @Override
    protected Boolean doInBackground(Message... messages) {
        final Message message = messages[0];
        this.geoManager = new GeofenceManager(context);

        if(message.getMessageType().equals(MessageType.UPDATE)) {
            return handleUpdateMessage((UpdateMessage)message);
        } else if(message.getMessageType().equals(MessageType.COLLABORATION)) {
            return handleCollaborationMessage((CollaborationMessage)message);
        }

        this.geoManager.addGeofence("id1","contenuto prima posizione",new LatLng(44.261746, 12.338030));
        this.geoManager.addGeofence("id2","contenuto seconda posizione",new LatLng(44.159825, 12.430086));

        this.geoManager.removeGeofence("id2");

        return false;
    }

    private boolean handleCollaborationMessage(CollaborationMessage message) {
        try {
            final CollaborationsManager manager = LocalStorageUtils.readShortCollaborationsFromFile(context);
            manager.addCollaboration(new ConcreteShortCollaboration(message.getCollaboration()));
            LocalStorageUtils.writeCollaborationToFile(context, message.getCollaboration());
            LocalStorageUtils.writeShortCollaborationsToFile(context, manager);
            return true;
        } catch (final IOException | JSONException e) {
            return false;
        }
    }

    private boolean handleUpdateMessage(final UpdateMessage message) {
        try {
            final Collaboration storedCollaboration = LocalStorageUtils.readCollaborationFromFile(
                    context, message.getCollaborationId());
            switch (message.getTarget()) {
                case NOTE:
                    return storeUpdatedNote((NoteUpdateMessage) message, storedCollaboration);
                case MODULE:
                    return storedCollaboration instanceof Project &&
                            storeUpdatedModule((ModuleUpdateMessage) message, (Project) storedCollaboration);
                case COLLABORATION:
                    return storeUpdatedCollaboration((CollaborationUpdateMessage) message);
                case MEMBER:
                    return storedCollaboration instanceof SharedCollaboration &&
                            storeUpdatedMember((MemberUpdateMessage) message, (SharedCollaboration) storedCollaboration);
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
                if(message.getNote().getLocation()!=null) {
                    this.geoManager.addGeofence(message.getNote().getNoteID(),message.getNote().getContent(),
                            new LatLng(message.getNote().getLocation().getLatitude(), message.getNote().getLocation().getLongitude()));
                }
                if(message.getNote().getExpirationDate()!=null){
                    this.alarm.setAlarm(context,message.getNote().getContent(),message.getNote().getExpirationDate());
                }
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

        LocalStorageUtils.writeCollaborationToFile(context, storedCollaboration);
        return true;
    }

    private boolean storeUpdatedModule(final ModuleUpdateMessage message, final Project storedCollaboration)
            throws IOException, JSONException {
        switch (message.getUpdateType()) {
            case CREATION:
                storedCollaboration.addModule(message.getModule());
                break;
            case UPDATING:
                storedCollaboration.removeModule(message.getModule().getId());
                storedCollaboration.addModule(message.getModule());
                break;
            case DELETION:
                storedCollaboration.removeModule(message.getModule().getId());
                break;
            default:
                return false;
        }

        LocalStorageUtils.writeCollaborationToFile(context, storedCollaboration);
        return true;
    }

    private boolean storeUpdatedMember(final MemberUpdateMessage message, final SharedCollaboration storedCollaboration)
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

        LocalStorageUtils.writeCollaborationToFile(context, storedCollaboration);
        return true;
    }

    private boolean storeUpdatedCollaboration(final CollaborationUpdateMessage message)
            throws IOException, JSONException {
        final CollaborationsManager manager = LocalStorageUtils.readShortCollaborationsFromFile(context);

        switch (message.getUpdateType()) {
            case UPDATING:
                manager.removeCollaboration(message.getCollaborationId());
                manager.addCollaboration(new ConcreteShortCollaboration(message.getCollaboration()));
                LocalStorageUtils.writeCollaborationToFile(context, message.getCollaboration());
                break;
            case DELETION:
                manager.removeCollaboration(message.getCollaborationId());
                context.deleteFile(message.getCollaborationId());
                break;
            default:
                return false;
        }

        LocalStorageUtils.writeShortCollaborationsToFile(context, manager);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success) {
            final Intent intent = new Intent("update.collaborations.on.gui");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }
}
