package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.update.NoteUpdateMessage;
import org.gammf.collabora_android.communication.update.UpdateMessage;
import org.gammf.collabora_android.communication.update.UpdateMessageTarget;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class MessageUtils {
    public static JSONObject messageToJSON(final Message message) throws JSONException {
        switch (message.getMessageType()) {
            case UPDATE:
                return updateMessageToJSON((UpdateMessage)message);
            case NOTIFICATION:
                //TO-DO
                return null;
            case COLLABORATION:
                //TO-DO
                return null;
        }
        return new JSONObject();
    }

    public static Message jsonToMessage(final JSONObject jsn) throws JSONException {
        //TO-DO
        return null;
    }

    private static JSONObject updateMessageToJSON(final UpdateMessage message) throws JSONException {
        final JSONObject jsn = new JSONObject();
        jsn.put("user", message.getUsername()).put("target", message.getTarget()).put("messageType", message.getUpdateType());
        if(message.getTarget() == UpdateMessageTarget.NOTE) {
            jsn.put("note", NoteUtils.noteToJSON(((NoteUpdateMessage)message).getNote()));
        }
        return jsn;
    }

}
