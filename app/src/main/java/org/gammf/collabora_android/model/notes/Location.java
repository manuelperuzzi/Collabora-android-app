package org.gammf.collabora_android.model.notes;

/**
 * Interface representing a well-defined position on planet earth.
 */

public interface Location {

    /**
     * @return the location's latitude.
     */
    Double getLatitude();

    /**
     * @return the location's longitude.
     */
    Double getLongitude();
}
