package org.gammf.collabora_android.utils;

import com.google.android.gms.maps.model.LatLng;

import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.notes.Note;

/**
 * Simple util class which is meant to provide useful methods in order to set, in the {@link GeofenceManager}, a location of
 * interest for a certain {@link Note}.
 */

public class GeofenceUtils {
    private static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    public static final float GEOFENCE_RADIUS_IN_METERS = 200; //200 meters

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 8760;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;

    /**
     * Sets a location in the {@link GeofenceManager}
     * @param note the {@link Note} of interest.
     * @param geoManager the manager.
     */
    public static void setGeofence(final Note note, final GeofenceManager geoManager){
        if(note.getLocation()!=null)
            geoManager.addGeofence(note.getNoteID(),note.getContent(),
                    new LatLng(note.getLocation().getLatitude(),note.getLocation().getLongitude()));
    }

    /**
     * Updates a previously-set location in the {@link GeofenceManager}
     * @param note the {@link Note} of interest.
     * @param geoManager the manager.
     */
    public static void updateGeofence(final Note note, final GeofenceManager geoManager){
        if(note.getLocation()!=null) {
            deleteGeofence(note, geoManager);
            setGeofence(note, geoManager);
        }
    }

    /**
     * Deletes a previously-set location in the {@link GeofenceManager}
     * @param note the {@link Note} of interest.
     * @param geoManager the manager.
     */
    public static void deleteGeofence(final Note note, final GeofenceManager geoManager){
        if(note.getLocation()!=null)
            geoManager.removeGeofence(note.getNoteID());
    }
}
