package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.Collaboration;
import org.gammf.collabora_android.communication.common.Message;
import org.gammf.collabora_android.communication.notification.ConcreteNotificationMessage;
import org.gammf.collabora_android.communication.notification.NotificationMessageType;
import org.gammf.collabora_android.communication.update.collaborations.CollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.notes.NoteUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;

import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
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
                jsn.put("module", ModulesUtils.moduleToJson(((ModuleUpdateMessage)message).getModule()));
                break;
            case COLLABORATION:
                jsn.put("collaboration", CollaborationUtils.collaborationToJson(((CollaborationUpdateMessage)message).getCollaboration()));
                break;
            case MEMBER:
                //TODO
                break;
        }
        return jsn;
    }

    public static UpdateMessage jsonToUpdateMessage(final JSONObject json) throws JSONException{
        final String username = json.getString("user");
        final UpdateMessageType updateType = UpdateMessageType.valueOf(json.getString("messageType"));

        final UpdateMessageTarget target = UpdateMessageTarget.valueOf(json.getString("target"));
        switch (target) {
            case NOTE:
                final Note note = NoteUtils.jsonToNote(json.getJSONObject("note"));
                return new ConcreteNoteUpdateMessage(username, note, updateType);
            case COLLABORATION:
                try {
                    final Collaboration collaboration = CollaborationUtils.jsonToCollaboration(
                            json.getJSONObject("collaboration"));
                    return new ConcreteCollaborationUpdateMessage(username, collaboration, updateType);
                } catch (final MandatoryFieldMissingException e) {
                    throw new JSONException("JSON message not parsable! possibly one or more mandatory fields may have not be filled.");
                }
            case MODULE:
                final Module module = ModulesUtils.jsonToModule(json.getJSONObject("module"));
                return new ConcreteModuleUpdateMessage(username, module, updateType);
            case MEMBER:
                throw new JSONException("JSON message not parsable! Member case is not handled yet.");
            default:
                throw new JSONException("JSON message not parsable! Target field is incorrect.");
        }
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
