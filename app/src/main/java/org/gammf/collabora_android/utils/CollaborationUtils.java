package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.private_collaborations.ConcretePrivateCollaboration;
import org.gammf.collabora_android.collaborations.private_collaborations.PrivateCollaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteGroup;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.collaborations.shared_collaborations.Group;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.users.CollaborationMember;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Manuel Peruzzi
 * Utily class providing methods to convert from collaboration class to json message and vice versa.
 */
public class CollaborationUtils {

    /**
     * Provides a json with all the collaboration information.
     * @param collaboration the collaboration.
     * @return a json message with all the collaboration information.
     * @throws JSONException if the conversion went wrong.
     */
    public static JSONObject collaborationToJson(final Collaboration collaboration) throws JSONException {
        final JSONObject json = new JSONObject();

        if (collaboration.getId() != null) {
            json.put("id", collaboration.getId());
        }

        json.put("name", collaboration.getName());

        final Set<Note> notes = collaboration.getAllNotes();
        if (!notes.isEmpty()) {
            final JSONArray jNotes = new JSONArray();
            for (final Note n: notes) {
                jNotes.put(NoteUtils.noteToJSON(n));
            }
            json.put("notes", jNotes);
        }

        final Set<CollaborationMember> members = new HashSet<>();
        if (collaboration instanceof PrivateCollaboration) {
            members.add(((PrivateCollaboration) collaboration).getUser());
        } else if (collaboration instanceof SharedCollaboration) {
            members.addAll(((SharedCollaboration) collaboration).getAllMembers());
        }
        if (!members.isEmpty()) {
            final JSONArray jMembers = new JSONArray();
            for (final CollaborationMember m: members) {
                jMembers.put(CollaborationMemberUtils.memberToJson(m));
            }
            json.put("users", jMembers);
        }

        if (collaboration instanceof PrivateCollaboration) {
            json.put("collaborationType", CollaborationType.PRIVATE.name());
        } else if (collaboration instanceof Group) {
            json.put("collaborationType", CollaborationType.GROUP.name());
        } else if (collaboration instanceof Project) {
            json.put("collaborationType", CollaborationType.PROJECT.name());
            final Set<Module> modules = ((Project) collaboration).getAllModules();
            if (!modules.isEmpty()) {
                final JSONArray jModules = new JSONArray();
                for (final Module m: modules) {
                    jModules.put(ModulesUtils.moduleToJson(m));
                }
                json.put("modules", jModules);
            }
        }

        return json;
    }

    /**
     * Create a collaboration class from a json message.
     * @param json the input json message.
     * @return a collaboration built from the json message.
     * @throws JSONException if the conversion went wrong.
     */
    public static Collaboration jsonToCollaboration(final JSONObject json) throws JSONException {
        final String id = json.getString("id");
        final String name = json.getString("name");
        final CollaborationType type = CollaborationType.valueOf(json.getString("collaborationType"));

        final JSONArray jMembers = json.getJSONArray("users");
        if (jMembers == null) {
            throw new JSONException("Json not correctly formatted! At least one member is required.");
        }

        final Collaboration collaboration;
        switch (type) {
            case PRIVATE:
                final CollaborationMember member = CollaborationMemberUtils.jsonToMember(jMembers.getJSONObject(0));
                collaboration = new ConcretePrivateCollaboration(id, name, member.getUsername());
                break;
            case GROUP:
                collaboration = new ConcreteGroup(id, name);
                break;
            case PROJECT:
                collaboration = new ConcreteProject(id, name);
                if (json.has("modules")) {
                    final JSONArray jModules = json.getJSONArray("modules");
                    for (int i = 0; i < jModules.length(); i++) {
                        ((Project)collaboration).addModule(ModulesUtils.jsonToModule(jModules.getJSONObject(i)));
                    }
                }
                break;
            default:
                throw new JSONException("Json not correctly formatted! Collaboration type not recognized.");
        }

        if (json.has("notes")) {
            final JSONArray jNotes = json.getJSONArray("notes");
            for (int i = 0; i < jNotes.length(); i++) {
                final Note note = NoteUtils.jsonToNote(jNotes.getJSONObject(i));
                if (note instanceof ModuleNote) {
                    ((Project)collaboration).addNote(note, ((ModuleNote) note).getModuleId());
                } else {
                    collaboration.addNote(NoteUtils.jsonToNote(jNotes.getJSONObject(i)));
                }
            }
        }

        if (collaboration instanceof SharedCollaboration) {
            final SharedCollaboration sharedCollaboration = (SharedCollaboration) collaboration;
            for (int i = 0; i < jMembers.length(); i++) {
                final CollaborationMember member = CollaborationMemberUtils.jsonToMember(jMembers.getJSONObject(i));
                sharedCollaboration.addMember(member);
            }
        }

        return collaboration;
    }

}
