package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.Collaboration;
import org.gammf.collabora_android.collaborations.CollaborationType;
import org.gammf.collabora_android.collaborations.ConcreteGroup;
import org.gammf.collabora_android.collaborations.ConcreteProject;
import org.gammf.collabora_android.collaborations.Group;
import org.gammf.collabora_android.collaborations.Project;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.users.SimpleCollaborationMember;
import org.gammf.collabora_android.users.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        final Set<CollaborationMember> members = collaboration.getAllMembers();
        if (!members.isEmpty()) {
            final JSONArray jMembers = new JSONArray();
            for (CollaborationMember m: members) {
                jMembers.put(UserUtils.userToJson(m));
            }
            json.put("users", jMembers);
        }

        final Set<Note> notes = collaboration.getAllNotes();
        if (!notes.isEmpty()) {
            final JSONArray jNotes = new JSONArray();
            for (final Note n: notes) {
                jNotes.put(NoteUtils.noteToJSON(n));
            }
            json.put("notes", jNotes);
        }

        if (collaboration instanceof Group) {
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

    public static Collaboration jsonToCollaboration(final JSONObject json)
            throws JSONException, MandatoryFieldMissingException {
        final String id = json.getString("id");
        final String name = json.getString("name");
        final CollaborationType type = CollaborationType.valueOf(json.getString("collaborationType"));

        final Collaboration collaboration;
        switch (type) {
            case PROJECT:
                collaboration = new ConcreteProject(id, name);
                if (json.has("modules")) {
                    final JSONArray jModules = json.getJSONArray("modules");
                    for (int i = 0; i < jModules.length(); i++) {
                        ((Project)collaboration).addModule(ModulesUtils.jsonToModule(jModules.getJSONObject(i)));
                    }
                }
                break;
            case GROUP:
            default:
                collaboration = new ConcreteGroup(id, name);
                break;
        }

        if (json.has("users")) {
            final JSONArray jMembers = json.getJSONArray("users");
            for (int i = 0; i < jMembers.length(); i++) {
                final User user = UserUtils.jsonToUser(jMembers.getJSONObject(i));
                if (user instanceof CollaborationMember) {
                    collaboration.addMember((CollaborationMember) user);
                } else {
                    collaboration.addMember(new SimpleCollaborationMember(user, AccessRight.READ));
                }
            }
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

        return collaboration;
    }

}
