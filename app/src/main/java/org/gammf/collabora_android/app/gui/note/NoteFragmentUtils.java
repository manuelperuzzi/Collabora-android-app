package org.gammf.collabora_android.app.gui.note;

import android.content.Context;
import android.util.Pair;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.app.utils.NoteProjectState;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.utils.LocalStorageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contain common method shared by note Fragments in the application
 */
public class NoteFragmentUtils {

    /**
     * Method used for prepare the listView to contain all the previousNotes
     * @param context the context of the application
     * @param note the note who has previousNotes
     * @param collaborationId the collaboration id that contain the note
     * @return return a list of CollaborationComponentInfo that contains all the previous notes
     */
    public static ArrayList<CollaborationComponentInfo> fillListView(Context context, Note note, String collaborationId){
        final List<Note> allNotes = new ArrayList<>();
        final ArrayList<CollaborationComponentInfo> noteItems = new ArrayList<>();
        allNotes.addAll(LocalStorageUtils.readCollaborationFromFile(context, collaborationId).getAllNotes());
        for (String pNoteID: note.getPreviousNotes()) {
            for (Note singleNote: allNotes) {
                if(singleNote.getNoteID().equals(pNoteID)){
                    noteItems.add(new CollaborationComponentInfo(singleNote.getNoteID(), singleNote.getContent(), CollaborationComponentType.NOTE,singleNote.getState().getCurrentDefinition()));
                }
            }
        }
        return noteItems;
    }

    /**
     * Method used to check that all the previous notes and the stat of the note don't broke the application logic
     * @param context the context of the application
     * @param actualState contains the State of the current note
     * @param previousNotesList contains the list of all the previous notes
     * @param collaboration contains the collaboration who contain the note
     * @return return a Pair where the first element is the successful or not of the method, and the second element id the error message to show
     */
    public static Pair<Boolean,String> checkPreviousNotesState(Context context,String actualState, List<String> previousNotesList, Collaboration collaboration){
        for (String noteId: previousNotesList ) {
            Note note = collaboration.getNote(noteId);
            if (note.getState().getCurrentDefinition().equals(NoteProjectState.TO_DO.toString())) {
                if (!actualState.equals(NoteProjectState.TO_DO.toString())) {
                    return new Pair<>(false,context.getString(R.string.error_todo));
                }
            }
            if (!note.getState().getCurrentDefinition().equals(NoteProjectState.DONE.toString())) {
                if (actualState.equals(NoteProjectState.DONE.toString())) {
                    return new Pair<>(false,context.getString(R.string.error_done));
                }
            }
        }
        return new Pair<>(true,"");
    }
}
