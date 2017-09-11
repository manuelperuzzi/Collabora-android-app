package org.gammf.collabora_android.app.gui.note;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.utils.LocalStorageUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChoosePreviousNotesDialogFragment extends android.support.v4.app.DialogFragment{

    private String collaborationId;
    private String moduleId, noteId;
    private static final String ARG_COLLABORATION_ID = "collaborationId";
    private static final String ARG_MODULEID = "moduleid";
    private static final String ARG_NOTEID = "noteId";
    private static final String NOMODULE = "nomodule";
    private static final String ARG_PREVNOTE = "PREVNOTESELECTED";
    private static final String ARG_NOTEITEMS = "NOTEITEMS";
    private static final String NONOTEID = "nonote";
    private static final int REQUEST_CODE = 0;
    private ArrayList<CollaborationComponentInfo> noteItems;
    private ArrayList<String> previousNotesSelected ;


    public static ChoosePreviousNotesDialogFragment newInstance(String collaborationId,String moduleId,ArrayList<CollaborationComponentInfo> noteItems,ArrayList<String> previousNotesSelected,String noteId ){
        final ChoosePreviousNotesDialogFragment fragment = new ChoosePreviousNotesDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_COLLABORATION_ID, collaborationId);
        args.putString(ARG_MODULEID, moduleId);
        args.putSerializable(ARG_NOTEITEMS, noteItems);
        args.putSerializable(ARG_PREVNOTE, previousNotesSelected);
        args.putString(ARG_NOTEID,noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.moduleId = getArguments().getString(ARG_MODULEID);
            this.collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            this.noteItems = (ArrayList<CollaborationComponentInfo>) getArguments().getSerializable(ARG_NOTEITEMS);
            this.previousNotesSelected = (ArrayList<String>) getArguments().getSerializable(ARG_PREVNOTE);
            this.noteId = getArguments().getString(ARG_NOTEID);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List<Note> allNotes = new ArrayList<>();
        final List<Integer> mSelectedItems = new ArrayList<>();
        Collaboration collaboration = LocalStorageUtils.readCollaborationFromFile(getActivity().getApplicationContext(), collaborationId);
        if (moduleId.equals(NOMODULE)) {
            allNotes.addAll(collaboration.getAllNotes());
        } else {
            for (Module module : ((ConcreteProject) collaboration).getAllModules()) {
                if (module.getId().equals(moduleId))
                    allNotes.addAll(module.getAllNotes());
            }
        }
        final CharSequence[] charSequenceItems = fillDialogList(allNotes).toArray(new CharSequence[fillDialogList(allNotes).size()]);
        return new AlertDialog.Builder(getActivity())
                .setTitle("Select previous notes")
                .setMultiChoiceItems(charSequenceItems, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        noteItems.clear();
                        previousNotesSelected.clear();
                        for (int position: mSelectedItems) {
                            previousNotesSelected.add(allNotes.get(position).getNoteID());
                            noteItems.add(new CollaborationComponentInfo(allNotes.get(position).getNoteID(), allNotes.get(position).getContent(), CollaborationComponentType.NOTE));
                        }
                        Intent intent = new Intent();
                        intent.putExtra(ARG_NOTEITEMS,noteItems);
                        intent.putExtra(ARG_PREVNOTE,previousNotesSelected);
                        getTargetFragment().onActivityResult(
                                getTargetRequestCode(), REQUEST_CODE, intent);
                        dismiss();

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                })
       .create();
    }

    private List<String> fillDialogList(List<Note> allNotes){
        final List<String> listItems = new ArrayList<>();
        if (noteId.equals(NONOTEID)) {
            for (Note note : allNotes) {
                listItems.add(note.getContent());
            }
        } else {
            for (Iterator<Note> iterator = allNotes.iterator(); iterator.hasNext(); ) {
                Note tmpNote = iterator.next();
                if (tmpNote.getNoteID().equals(noteId))
                    iterator.remove();
                else
                    listItems.add(tmpNote.getContent());
            }
        }
        return listItems;
    }
}
