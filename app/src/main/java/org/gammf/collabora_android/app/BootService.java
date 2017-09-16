package org.gammf.collabora_android.app;

import android.app.IntentService;
import android.content.Intent;

import org.gammf.collabora_android.app.alarm.AlarmController;
import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.model.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.model.short_collaborations.ShortCollaboration;
import org.gammf.collabora_android.utils.app.AlarmUtils;
import org.gammf.collabora_android.utils.app.GeofenceUtils;
import org.gammf.collabora_android.utils.app.LocalStorageUtils;

/**
 * Service that manage rebooting procedure
 * Created by Federico on 04/08/2017.
 */

public class BootService extends IntentService {

    public BootService() {
        super("BootService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        CollaborationsManager manager = LocalStorageUtils.readShortCollaborationsFromFile(getApplicationContext());
        if(manager!=null){
            for (ShortCollaboration collab : manager.getAllCollaborations()) {
                Collaboration tmpcollab = LocalStorageUtils.readCollaborationFromFile(getApplicationContext(),collab.getId());
                for (Note collabnote: tmpcollab.getAllNotes()) {
                    AlarmUtils.setAlarm(getApplicationContext(), collabnote, new AlarmController());
                    GeofenceUtils.setGeofence(collabnote, new GeofenceManager(getApplicationContext()));
                }
            }
        }
    }

}
