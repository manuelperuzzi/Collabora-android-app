package org.gammf.collabora_android.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * GeofenceUtils used in this project.
 * Created by Federico on 07/08/2017.
 */

public class GeofenceUtils {

    private GeofenceUtils() {
    }
    
    public static final String PREFS_NAME = "CollaboraPrefs";

    private static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    public static final float GEOFENCE_RADIUS_IN_METERS = 1000; //1km

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

}
