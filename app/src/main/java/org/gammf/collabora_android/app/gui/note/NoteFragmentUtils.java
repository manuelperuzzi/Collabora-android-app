package org.gammf.collabora_android.app.gui.note;

import android.content.Context;
import android.util.Pair;

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

public class NoteFragmentUtils {

    public static ArrayList<CollaborationComponentInfo> fillListView(Context context, Note note, String collaborationId){
        final List<Note> allNotes = new ArrayList<>();
        final ArrayList<CollaborationComponentInfo> noteItems = new ArrayList<>();
        try {
            allNotes.addAll(LocalStorageUtils.readCollaborationFromFile(context, collaborationId).getAllNotes());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        for (String pNoteID: note.getPreviousNotes()) {
            for (Note singleNote: allNotes) {
                if(singleNote.getNoteID().equals(pNoteID)){
                    noteItems.add(new CollaborationComponentInfo(singleNote.getNoteID(), singleNote.getContent(), CollaborationComponentType.NOTE));
                }
            }
        }
        return noteItems;
    }

    public static Pair<Boolean,String> checkPreviousNotesState(String actualState, ArrayList<String> previousNotesList, Collaboration collaboration){
        for (String noteId: previousNotesList ) {
            Note note = collaboration.getNote(noteId);
            if (note.getState().getCurrentState().equals(NoteProjectState.TO_DO.toString())) {
                if (!actualState.equals(NoteProjectState.TO_DO.toString())) {
                    return new Pair<>(false,"Error: in the previous notes list there are notes with To Do state, modify current state in To Do or edit list!");
                }
            }
        }
        if (actualState.equals(NoteProjectState.DONE.toString())){
            for (String noteId: previousNotesList ) {
                Note note = collaboration.getNote(noteId);
                if(!note.getState().getCurrentState().equals(NoteProjectState.DONE.toString()))
                    return new Pair<>(false,"Error: selected state Done not compliant with previous notes list (with Done state, all list's note should has Done in state!");
            }
        }
        return new Pair<>(true,"");
    }
}
