package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;

/**
 * @author Alfredo Maffi
 * Simple class created to supply handy tricks for the rest of the app
 */

public class CollaboraAppUtils {
    /**
     * Creates a pretty readable message used in the notification shown to the user
     * @param updateMessage used to extract information about the incoming update from the server
     * @return a readable string to be shown to the user
     */
    public static String getReadableNotificationMessage(final UpdateMessage updateMessage) {
        return "The user " + updateMessage.getUsername()
                + getReadableUpdateType(updateMessage)
                + getReadableUpdateTarget(updateMessage)
                + getReadableNotificationEnd(updateMessage);
    }

    private static String getReadableUpdateType(final UpdateMessage message) {
        switch(message.getUpdateType()) {
            case CREATION: if (message.getTarget() == UpdateMessageTarget.MEMBER) return " added "; else return " created ";
            case UPDATING: if (message.getTarget() == UpdateMessageTarget.MEMBER) return " modified the rights of "; else return " modified ";
            case DELETION: if (message.getTarget() == UpdateMessageTarget.MEMBER) return " removed "; else return " deleted ";
        }
        return "";
    }

    private static String getReadableUpdateTarget(final UpdateMessage message) {
        switch(message.getTarget()) {
            case NOTE: return "a note ";
            case MODULE: return "a module ";
            case COLLABORATION: return "the collaboration ";
            case MEMBER: return "a member";
        }
        return "";
    }

    private static String getReadableNotificationEnd(final UpdateMessage message) {
        switch(message.getTarget()) {
            case COLLABORATION: return "called " + message.getCollaborationId();
            default: return "in the collaboration called" + message.getCollaborationId();
        }
    }
}
