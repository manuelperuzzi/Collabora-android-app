package org.gammf.collabora_android.notes;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class NoteLocation implements Location {
    private final Double latitude;
    private final Double longitude;

    public NoteLocation(final Double latitude, final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Double getLatitude() {
        return this.latitude;
    }

    @Override
    public Double getLongitude() {
        return this.longitude;
    }
}
