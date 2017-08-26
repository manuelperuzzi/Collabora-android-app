package org.gammf.collabora_android.notes;

/**
 * @author Alfredo Maffi
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
