package org.gammf.collabora_android.app.gui.note;

import android.content.Context;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.app.utils.NoteProjectState;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoteFragmentUtil {

    public static ArrayList<CollaborationComponentInfo> fillListView(Context context, Note note, String collaborationId){
        final List<Note> allNotes = new ArrayList<>();
        final ArrayList<CollaborationComponentInfo> noteItems = new ArrayList<>();
        try {
            allNotes.addAll(LocalStorageUtils.readCollaborationFromFile(context, collaborationId).getAllNotes());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        for (String pNoteID: note.getPreviousNotes()) {
            for (Note noteon: allNotes) {
                if(noteon.getNoteID().equals(pNoteID)){
                    noteItems.add(new CollaborationComponentInfo(noteon.getNoteID(), noteon.getContent(), CollaborationComponentType.NOTE));
                }
            }
        }
        return noteItems;
    }

    public static boolean checkPreviousNotesState(String actualState, ArrayList<String> previousNotesList, Collaboration collaboration){
        for (String noteId: previousNotesList ) {
            Note note = collaboration.getNote(noteId);
            if (note.getState().getCurrentState().equals(NoteProjectState.TO_DO.toString())) {
                if (!actualState.equals(NoteProjectState.TO_DO.toString())) {
                    return false;
                }
            }
        }
        if (actualState.equals(NoteProjectState.DONE.toString())){
            for (String noteId: previousNotesList ) {
                Note note = collaboration.getNote(noteId);
                if(!note.getState().getCurrentState().equals(NoteProjectState.DONE.toString()))
                    return false;
            }
        }
        return true;
    }
}
