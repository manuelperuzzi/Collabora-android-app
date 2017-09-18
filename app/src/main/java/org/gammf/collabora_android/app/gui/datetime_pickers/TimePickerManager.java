package org.gammf.collabora_android.app.gui.datetime_pickers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import org.gammf.collabora_android.app.utils.AbstractObservableSource;
import org.joda.time.LocalTime;

/**
 *
 * Represent a Time Picker Manager used to set a time expiration.
 * Handle time representation and picker showing.
 * Implements a {@link org.gammf.collabora_android.app.utils.ObservableSource} of {@link org.joda.time.DateTime}
 *
 */
public class TimePickerManager extends AbstractObservableSource<LocalTime> implements TimePickerDialog.OnTimeSetListener {

    /**
     * A LocalTime that have to be used when the picker don't have to shown any particular time at start up.
     */
    public static final LocalTime NO_TIME = null;

    private final Context context;

    /**
     * Build a new TimePickerManager
     *
     * @param context the application context.
     */
    public TimePickerManager(final Context context) {
        this.context = context;
    }

    /**
     * Shows a time picker, initially setted on the given {@link LocalTime}
     * @param timeToShow the time initially setted in the time picker. If no particular time
     *                   have to be showed, use NO_TIME.
     */
    public void showTimePicker(final LocalTime timeToShow) {
        LocalTime setTime = LocalTime.now();
        if (timeToShow != NO_TIME) {
            setTime = timeToShow;
        }
        new TimePickerDialog(this.context, this,
                setTime.getHourOfDay(),
                setTime.getMinuteOfHour(), true).show();
    }

    @Override
    public void onTimeSet(final TimePicker timePicker, final int hour, final int minute) {
        notifyObservers(new LocalTime(hour, minute));
    }
}
