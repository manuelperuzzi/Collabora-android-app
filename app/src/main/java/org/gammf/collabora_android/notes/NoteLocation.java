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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteLocation that = (NoteLocation) o;

        return latitude != null ? latitude.equals(that.latitude) : that.latitude == null &&
                (longitude != null ? longitude.equals(that.longitude) : that.longitude == null);

    }

    @Override
    public int hashCode() {
        int result = latitude != null ? latitude.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
