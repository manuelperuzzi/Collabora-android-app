package org.gammf.collabora_android.app.gui.datetime_pickers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import org.gammf.collabora_android.app.utils.AbstractObservableSource;
import org.joda.time.LocalDate;

/**
 * A manager for DatePicker. handle date representation and picker showing.
 * Implements a {@link org.gammf.collabora_android.app.utils.ObservableSource} of {@link org.joda.time.DateTime}
 */
public class DatePickerManager extends AbstractObservableSource<LocalDate> implements DatePickerDialog.OnDateSetListener  {

    /**
     * A Date that have to be used when the picker dont have to shown any particular date at start up.
     */
    public static final LocalDate NO_DATE = null;

    private final Context context;

    /**
     * Build the date picker manager.
     * @param context the context of the application.
     */
    public DatePickerManager(final Context context) {
        this.context = context;
    }

    /**
     * Shows a date picker, initially setted on the given {@link LocalDate}
     * @param settedDate the date initially setted in the date picker. If no particular date
     *                   have to be showed, use NO_DATE.
     */
    public void showDatePicker(final LocalDate settedDate) {
        LocalDate toSet = LocalDate.now();
        if (settedDate != NO_DATE) {
            toSet = settedDate;
        }
        new DatePickerDialog(this.context, this,
                toSet.getYear(),
                toSet.getMonthOfYear() - 1,
                toSet.getDayOfMonth()).show();
    }

    @Override
    public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
        notifyObservers(new LocalDate(year, month + 1, day));
    }
}


