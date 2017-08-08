package org.gammf.collabora_android.collabora_android_app.location_geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.gammf.collabora_android.collabora_android_app.Constants;
import org.gammf.collabora_android.collabora_android_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Federico on 08/08/2017.
 */

public class GeofenceManager implements OnCompleteListener<Void> {

    private static final String TAG = "GeofenceManagerDEBUG";

    private Context context;
    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;

    public GeofenceManager(Context context){

        this.context = context;

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        // Initially set the PendingIntent used in addGeofence() and removeGeofence() to null.
        mGeofencePendingIntent = null;

        mGeofencingClient = LocationServices.getGeofencingClient(context);

    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        Log.d(TAG, String.valueOf(geofence));
        builder.addGeofence(geofence);

        // Return a GeofencingRequest.
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

            int messageId = getGeofencesAdded() ? R.string.geofences_added :
                    R.string.geofences_removed;
            Toast.makeText(this.context, this.context.getString(messageId), Toast.LENGTH_SHORT).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
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
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this.context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofence().
        return PendingIntent.getService(this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /*public void addGeofenceToList(String noteID, LatLng coordinates ) {
        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. USE NOTE ID
                .setRequestId(noteID)

                // Set the circular region of this geofence.
                .setCircularRegion(
                        coordinates.latitude,
                        coordinates.longitude,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )

                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)

                // Create the geofence.
                .build());

    }*/

    /**
     * Adds geofences. This method should be called after the user has granted the location
     * permission.
     */
    @SuppressWarnings("MissingPermission")
    public void addGeofence(String noteID, LatLng coordinates) {

        Geofence geofence = new Geofence.Builder()
                // Set the request ID of the geofence. USE NOTE ID
                .setRequestId(noteID)

                // Set the circular region of this geofence.
                .setCircularRegion(
                        coordinates.latitude,
                        coordinates.longitude,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )

                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)

                // Create the geofence.
                .build();

        mGeofencingClient.addGeofences(getGeofencingRequest(geofence), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }

    /**
     * Returns true if geofences were added, otherwise false.
     */
    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(this.context).getBoolean(
                Constants.GEOFENCES_ADDED_KEY, false);
    }

    /**
     * Stores whether geofences were added ore removed in {@link SharedPreferences};
     *
     * @param added Whether geofences were added or removed.
     */
    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this.context)
                .edit()
                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

    /**
     * Removes geofences. This method should be called after the user has granted the location
     * permission.
     */
    public void removeGeofence(String idNote) {
        List<String> tmp = new ArrayList<>();
        tmp.add(idNote);
        mGeofencingClient.removeGeofences(tmp).addOnCompleteListener(this);
    }

}
