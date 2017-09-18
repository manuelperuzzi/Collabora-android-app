package org.gammf.collabora_android.utils.communication;

import org.gammf.collabora_android.app.R;

/**
 * Simple enumeration representing all the possible server error codes.
 */

public enum CollaborationError {
    SERVER_ERROR(R.string.server_error),
    MEMBER_NOT_FOUND(R.string.member_not_found);

    private final int errorRes;

    CollaborationError(final int errorRes) {
        this.errorRes = errorRes;
    }

    /**
     * Returns the id of the string resource that describes the error.
     */
    public int getErrorResource() {
        return errorRes;
    }
}
