package org.gammf.collabora_android.utils;

import android.content.Context;

import org.gammf.collabora_android.app.alarm.Alarm;
import org.gammf.collabora_android.notes.Note;

/**
 * Created by Alfredo on 15/09/2017.
 */

public class AlarmUtils {
    public static final String PREFS_NAME = "CollaboraPrefs";

    public static void setAlarm(final Context context, final Note note, final Alarm alarm){
        if(note.getExpirationDate()!=null)
            alarm.setAlarm(context,note.getContent(),note.getExpirationDate());
    }

    public static void updateAlarm(final Context context, final Note note, final Alarm alarm){
        if(note.getExpirationDate()!=null){
            deleteAlarm(context, note, alarm);
            setAlarm(context, note, alarm);
        }
    }

    public static void deleteAlarm(final Context context, final Note note, final Alarm alarm){
        if(note.getExpirationDate()!=null)
            alarm.deleteAlarm(context,note.getExpirationDate());
    }
}
