package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.notification.ConcreteNotificationMessage;
import org.gammf.collabora_android.communication.notification.NotificationMessage;
import org.gammf.collabora_android.communication.notification.NotificationMessageType;
import org.gammf.collabora_android.communication.update.NoteUpdateMessage;
import org.gammf.collabora_android.communication.update.UpdateMessage;
import org.gammf.collabora_android.communication.update.UpdateMessageTarget;
import org.gammf.collabora_android.notes.Note;
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
                return notificationMessageToJSON((NotificationMessage)message);
            case COLLABORATION:
                //TO-DO
                return null;
        }
        return new JSONObject();
    }

    public static Message jsonToMessage(final JSONObject jsn) throws JSONException {
        //I suppose that jsn can't contain an updateMessage
        if(jsn.has("messageType")) {
            return  new ConcreteNotificationMessage(jsn.getString("user"),
                                                    NoteUtils.jsonToNote((JSONObject)jsn.get("note")),
                                                    (NotificationMessageType)jsn.get("messageType"));
        } else {
            //TO-DO
            //the message is a collaboration message
            return null;
        }
    }

    private static JSONObject updateMessageToJSON(final UpdateMessage message) throws JSONException {
        final JSONObject jsn = new JSONObject();
        jsn.put("user", message.getUsername()).put("target", message.getTarget()).put("messageType", message.getUpdateType());
        switch (message.getTarget()) {
            case NOTE:
                jsn.put("note", NoteUtils.noteToJSON(((NoteUpdateMessage)message).getNote()));
                break;
            case MODULE:
                //TO-DO
                break;
            case COLLABORATION:
                //TO-DO
                break;
            case MEMBER:
                //TO-DO
                break;
        }
        return jsn;
    }

    private static JSONObject notificationMessageToJSON(final NotificationMessage message) throws JSONException {
        final JSONObject jsn = new JSONObject();
        return jsn.put("user", message.getUsername()).put("messageType", message.getNotificationType()).put("note", NoteUtils.noteToJSON((message).getNote()));
    }

}
