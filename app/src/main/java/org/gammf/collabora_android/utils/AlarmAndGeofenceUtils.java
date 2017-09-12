package org.gammf.collabora_android.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.gammf.collabora_android.app.alarm.Alarm;
import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.notes.Note;

import java.util.HashMap;

/**
 * AlarmAndGeofenceUtils used in this project.
 * Created by Federico on 07/08/2017.
 */

public class AlarmAndGeofenceUtils {

    public AlarmAndGeofenceUtils() {
    }
    
    public static final String PREFS_NAME = "CollaboraPrefs";

    private static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    public static final float GEOFENCE_RADIUS_IN_METERS = 200; //200m

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

    public static void addAlarmAndGeofences(Context context, Note note, Alarm alarm, GeofenceManager geoManager){
        if(note.getLocation()!=null)
            geoManager.addGeofence(note.getNoteID(),note.getContent(),
                    new LatLng(note.getLocation().getLatitude(),note.getLocation().getLongitude()));
        if(note.getExpirationDate()!=null)
            alarm.setAlarm(context,note.getContent(),note.getExpirationDate());
    }

    public static void updateAlarmAndGeofences(Context context, Note note, Alarm alarm, GeofenceManager geoManager){
        if(note.getLocation()!=null) {
            geoManager.removeGeofence(note.getNoteID());
            geoManager.addGeofence(note.getNoteID(),note.getContent(),
                    new LatLng(note.getLocation().getLatitude(), note.getLocation().getLongitude()));
        }
        if(note.getExpirationDate()!=null){
            alarm.deleteAlarm(context,note.getExpirationDate());
            alarm.setAlarm(context,note.getContent(),note.getExpirationDate());
        }
    }

    public static void deleteAlarmAndGeofences(Context context, Note note, Alarm alarm, GeofenceManager geoManager){
        if(note.getLocation()!=null)
            geoManager.removeGeofence(note.getNoteID());
        if(note.getExpirationDate()!=null)
            alarm.deleteAlarm(context,note.getExpirationDate());
    }

}
