package org.gammf.collabora_android.communication.error;

import org.gammf.collabora_android.communication.common.AbstractMessage;
import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.utils.communication.CollaborationError;

/**
 * Concrete implementation of a server error message.
 */

public class ConcreteErrorMessage extends AbstractMessage implements ErrorMessage {

    final CollaborationError error;

    public ConcreteErrorMessage(final String username, final CollaborationError error) {
        super(username, MessageType.ERROR);
        this.error = error;
    }

    @Override
    public CollaborationError getError() {
        return error;
    }
}
