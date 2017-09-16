package org.gammf.collabora_android.communication.error;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.utils.communication.CollaborationError;

/**
 * Interface representing a message containing information about a server error.
 */

public interface ErrorMessage extends Message {

    /**
     * Returns the server error.
     */
    CollaborationError getError();
}
