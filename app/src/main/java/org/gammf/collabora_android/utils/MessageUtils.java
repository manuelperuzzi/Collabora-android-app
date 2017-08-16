package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.notification.ConcreteNotificationMessage;
import org.gammf.collabora_android.communication.notification.NotificationMessageType;
import org.gammf.collabora_android.communication.update.notes.NoteUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class MessageUtils {
    public static JSONObject updateMessageToJSON(final UpdateMessage message) throws JSONException {
        final JSONObject jsn = new JSONObject();
        jsn.put("user", message.getUsername()).put("target", message.getTarget()).put("messageType", message.getUpdateType().toString());
        switch (message.getTarget()) {
            case NOTE:
                jsn.put("note", NoteUtils.noteToJSON(((NoteUpdateMessage)message).getNote()));
                break;
            case MODULE:
                //TODO
                break;
            case COLLABORATION:
                //TODO
                break;
            case MEMBER:
                //TODO
                break;
        }
        return jsn;
    }

    public static Message jsonToMessage(final JSONObject jsn) throws JSONException {
        //if messageType is present, jsn is a notification message
        if(jsn.has("messageType")) {
            return  new ConcreteNotificationMessage(jsn.getString("user"),
                                                    NoteUtils.jsonToNote((JSONObject)jsn.get("note")),
                                                    NotificationMessageType.valueOf(jsn.getString("messageType")));
        } else {
            //TODO
            //the message is a collaboration message
            return null;
        }
    }
}
