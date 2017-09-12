package org.gammf.collabora_android.app.location_geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.gammf.collabora_android.utils.AlarmAndGeofenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager of geofences logic in the application
 */
public class GeofenceManager implements OnCompleteListener<Void> {

    private static final String TAG = "GeofenceManagerDEBUG";

    private Context context;
    private GeofencingClient mGeofencingClient;
    private PendingIntent mGeofencePendingIntent;

    public GeofenceManager(Context context){
        this.context = context;
        mGeofencePendingIntent = null;
        mGeofencingClient = LocationServices.getGeofencingClient(context);
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        Log.d(TAG, String.valueOf(geofence));
        builder.addGeofence(geofence);
        return builder.build();
    }

    /**
     * Runs when the result of calling of addGeofence
     * is available.
     * @param task the resulting Task, containing either a result or error.
     */
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            updateGeofencesAdded(!getGeofencesAdded());
        } else {
            String errorMessage = GeofenceErrorMessages.getErrorString(this.context, task.getException());
            Log.w(TAG, errorMessage);
        }
    }


    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent(String noteID, String contentToDisplay) {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this.context, GeofenceTransitionsIntentService.class);
        intent.putExtra(noteID ,contentToDisplay);
        final int _id = (int) System.currentTimeMillis();
        Log.d("INTENT PRIMA", noteID + " - "+ contentToDisplay);
        return PendingIntent.getService(this.context, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Adds geofences. This method should be called after the user has granted the location
     * permission.
     */
    @SuppressWarnings("MissingPermission")
    public void addGeofence(String noteID, String contentToDisplay, LatLng coordinates) {
        Geofence geofence = new Geofence.Builder()
                .setRequestId(noteID)
                .setCircularRegion(
                        coordinates.latitude,
                        coordinates.longitude,
                        AlarmAndGeofenceUtils.GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(AlarmAndGeofenceUtils.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
        mGeofencingClient.addGeofences(getGeofencingRequest(geofence), getGeofencePendingIntent(noteID,contentToDisplay))
                .addOnCompleteListener(this);
    }

    /**
     * Removes geofence
     */
    public void removeGeofence(String idNote) {
        List<String> tmp = new ArrayList<>();
        tmp.add(idNote);
        mGeofencingClient.removeGeofences(tmp).addOnCompleteListener(this);
    }

    /**
     * Returns true if geofences were added, otherwise false.
     */
    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(this.context).getBoolean(
                AlarmAndGeofenceUtils.GEOFENCES_ADDED_KEY, false);
    }

    /**
     * Stores whether geofences were added ore removed in SharedPreferences;
     *
     * @param added Whether geofences were added or removed.
     */
    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this.context)
                .edit()
                .putBoolean(AlarmAndGeofenceUtils.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

}
