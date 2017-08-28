package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.complete_collaborations.SharedCollaboration;
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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Alfredo Maffi, Manuel Peruzzi
 * Utily class providing methods to convert from UpdateMessage class to json message and vice versa.
 */

public class MessageUtils {

    /**
     * Provides a json with all the update message information.
     * @param message the update message.
     * @return a json message with all the update message information.
     * @throws JSONException if the conversion went wrong.
     */
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
                jsn.put("member", CollaborationMemberUtils.memberToJson(((MemberUpdateMessage)message).getMember()));
                break;
        }
        return jsn;
    }

    /**
     * Creates an update message from a json message.
     * @param json the input json message.
     * @return an update message built from the json message.
     * @throws JSONException if the conversion went wrong.
     */
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
                final SharedCollaboration collaboration = CollaborationUtils.jsonToCollaboration(
                        json.getJSONObject("collaboration"));
                return new ConcreteCollaborationUpdateMessage(username, collaboration, updateType);
            case MODULE:
                final Module module = ModulesUtils.jsonToModule(json.getJSONObject("module"));
                return new ConcreteModuleUpdateMessage(username, module, updateType, collaborationId);
            case MEMBER:
                final CollaborationMember member = CollaborationMemberUtils.jsonToMember(json.getJSONObject("member"));
                return new ConcreteMemberUpdateMessage(username, member, updateType, collaborationId);
            default:
                throw new JSONException("JSON message not parsable! Target field is incorrect.");
        }
    }

}
