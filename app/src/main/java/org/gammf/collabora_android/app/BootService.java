package org.gammf.collabora_android.app;

import android.app.IntentService;
import android.content.Intent;

import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.short_collaborations.ShortCollaboration;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;

/**
 * Service that manage rebooting procedure
 * Created by Federico on 04/08/2017.
 */

public class BootService extends IntentService {

    private GeofenceManager geoManager;

    public BootService() {
        super("BootService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            CollaborationsManager manager = LocalStorageUtils.readShortCollaborationsFromFile(getApplicationContext());
            for (ShortCollaboration collab : manager.getAllCollaborations()) {
                Collaboration tmpcollab = LocalStorageUtils.readCollaborationFromFile(getApplicationContext(),collab.getId())
                for (Note collabnote: tmpcollab.getAllNotes()) {

                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

}
