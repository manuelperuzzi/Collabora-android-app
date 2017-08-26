package org.gammf.collabora_android.notes;

/**
 * @author Alfredo Maffi
 * Concrete location representing a note's location.
 */

public class NoteLocation implements Location {
    private final Double latitude;
    private final Double longitude;

    /**
     * Class constructor.
     * @param latitude the note's latitude.
     * @param longitude the note's longitude.
     */
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
