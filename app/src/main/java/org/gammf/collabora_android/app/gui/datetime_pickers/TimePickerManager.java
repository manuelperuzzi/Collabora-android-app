package org.gammf.collabora_android.app.gui.datetime_pickers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import org.gammf.collabora_android.app.utils.AbstractObservableSource;
import org.joda.time.LocalTime;

/**
 * Created by gab on 12/09/17.
 */

public class TimePickerManager extends AbstractObservableSource<LocalTime> implements TimePickerDialog.OnTimeSetListener {

    public static final LocalTime NO_TIME = null;

    private final Context context;

    public TimePickerManager(final Context context) {
        this.context = context;
    }

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
