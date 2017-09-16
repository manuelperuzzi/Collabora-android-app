package org.gammf.collabora_android.utils.app;

import android.content.Context;

import org.gammf.collabora_android.app.alarm.AlarmController;
import org.gammf.collabora_android.model.notes.Note;

/**
 * Simple util class which is meant to provide useful methods in order to manage users alarms.
 */

public class AlarmUtils {
    public static final String PREFS_NAME = "CollaboraPrefs";

    /**
     * Sets an alarm to a certain {@link Note}.
     * @param context the application context.
     * @param note the {@link Note} of interest.
     * @param alarmController the {@link AlarmController} used to set the alarm.
     */
    public static void setAlarm(final Context context, final Note note, final AlarmController alarmController) {
        if (note.getExpirationDate() != null) {
            alarmController.setAlarm(context, note.getContent(), note.getExpirationDate());
        }
    }

    /**
     * Updates a previously-set alarm of a certain {@link Note}.
     * @param context the application context.
     * @param note the {@link Note} of interest.
     * @param alarmController the {@link AlarmController} used to update the alarm.
     */
    public static void updateAlarm(final Context context, final Note note, final AlarmController alarmController){
        if(note.getExpirationDate()!=null){
            deleteAlarm(context, note, alarmController);
            setAlarm(context, note, alarmController);
        }
    }

    /**
     * Deletes a previously-set alarm of a certain {@link Note}.
     * @param context the application context.
     * @param note the {@link Note} of interest.
     * @param alarmController the {@link AlarmController} used to update the alarm.
     */
    public static void deleteAlarm(final Context context, final Note note, final AlarmController alarmController){
        if(note.getExpirationDate()!=null) {
            alarmController.deleteAlarm(context, note.getExpirationDate());
        }
    }
}
