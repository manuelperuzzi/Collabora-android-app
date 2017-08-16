package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.Collaboration;
import org.gammf.collabora_android.communication.update.collaborations.CollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.members.ConcreteMemberUpdateMessage;
import org.gammf.collabora_android.communication.update.members.MemberUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.communication.update.notes.NoteUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;

import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.users.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alfredo on 08/08/2017.
 */

public class MessageUtils {
    public static JSONObject updateMessageToJSON(final UpdateMessage message) throws JSONException {
        final JSONObject jsn = new JSONObject();
        jsn.put("user", message.getUsername())
                .put("target", message.getTarget().name())
                .put("messageType", message.getUpdateType().name())
                .put("collaborationId", message.getCollaborationId());
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
                jsn.put("member", UserUtils.userToJson(((MemberUpdateMessage)message).getMember()));
                break;
        }
        return jsn;
    }

    public static UpdateMessage jsonToUpdateMessage(final JSONObject json) throws JSONException{
        final String username = json.getString("user");
        final UpdateMessageType updateType = UpdateMessageType.valueOf(json.getString("messageType"));
        final String collaborationId = json.getString("collaborationId");

        final UpdateMessageTarget target = UpdateMessageTarget.valueOf(json.getString("target"));
        switch (target) {
            case NOTE:
                final Note note = NoteUtils.jsonToNote(json.getJSONObject("note"));
                return new ConcreteNoteUpdateMessage(username, note, updateType, collaborationId);
            case COLLABORATION:
                try {
                    final Collaboration collaboration = CollaborationUtils.jsonToCollaboration(
                            json.getJSONObject("collaboration"));
                    return new ConcreteCollaborationUpdateMessage(username, collaboration, updateType);
                } catch (final MandatoryFieldMissingException e) {
                    throw new JSONException("JSON message not parsable! Possibly one or more mandatory fields may have not be filled.");
                }
            case MODULE:
                final Module module = ModulesUtils.jsonToModule(json.getJSONObject("module"));
                return new ConcreteModuleUpdateMessage(username, module, updateType, collaborationId);
            case MEMBER:
                try {
                    final User user = UserUtils.jsonToUser(json.getJSONObject("member"));
                    if (user instanceof CollaborationMember) {
                        return new ConcreteMemberUpdateMessage(username, (CollaborationMember) user, updateType, collaborationId);
                    }
                    throw new JSONException("JSON message not parsable! Possibly one or more mandatory fields may have not be filled.");
                } catch (final MandatoryFieldMissingException e) {
                    throw new JSONException("JSON message not parsable! Possibly one or more mandatory fields may have not be filled.");
                }
            default:
                throw new JSONException("JSON message not parsable! Target field is incorrect.");
        }
    }

}
