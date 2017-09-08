package org.gammf.collabora_android.app.gui.note;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.gui.map.MapManager;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.Group;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.utils.AccessRightUtils;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class NoteFragment extends Fragment implements AdapterView.OnItemClickListener{

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String SENDER = "notefrag";

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_NOTEID = "noteId";
    private static final String ARG_MODULEID = "moduleName";
    private static final String NOMODULE = "nomodule";

    private String username;
    private String collaborationId,moduleId;
    private String noteId;
    private Note note;
    private Collaboration collaboration;
    private ListView previousNotesList;
    private MapManager mapManager;
    private ProgressBar progressBarState;
    private TextView stateTextView;
    private TextView noPreviousNoteView;
    private ArrayList<CollaborationComponentInfo> noteItems;
    private CollaborationMember member;

    public NoteFragment() {
        setHasOptionsMenu(true);
    }

    public static NoteFragment newInstance(String username, String collabId, String noteId,String moduleId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_COLLABID, collabId);
        args.putString(ARG_NOTEID, noteId);
        args.putString(ARG_MODULEID, moduleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.noteId = getArguments().getString(ARG_NOTEID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }
        try {
            this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
            this.note = collaboration.getNote(noteId);
            if(collaboration instanceof Project || collaboration instanceof Group){
                this.member = AccessRightUtils.checkMemebrAccess(collaboration,username);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if(collaboration instanceof Project || collaboration instanceof Group) {
            if (AccessRightUtils.checkIfUserHasAccessRight(member)) {
                inflater.inflate(R.menu.edit_note, menu);
            }
        }
        else
            inflater.inflate(R.menu.edit_collaboration, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        Method for editcollaboration click on toolbar
        trigger the @EditCollaborationFragment
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_editnote) {
            Fragment editNoteFragment = EditNoteFragment.newInstance(username, collaborationId, noteId,moduleId);
            changeFragment(editNoteFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        previousNotesList = rootView.findViewById(R.id.listViewPNote);
        noteItems = new ArrayList<>();
        initializeGuiComponent(rootView);
        setStateProgressBar(stateTextView.getText().toString());
        return rootView;
    }

    private void initializeGuiComponent(final View rootView) {
        try {
            ((TextView) rootView.findViewById(R.id.contentNote)).setText(note.getContent());
            progressBarState = rootView.findViewById(R.id.progressBarState);
            this.noPreviousNoteView = rootView.findViewById(R.id.noPNote);
            this.stateTextView = rootView.findViewById(R.id.lblState);
            this.stateTextView.setText(note.getState().getCurrentState());

            final TextView responsibleTextView = rootView.findViewById(R.id.lblResponsible);
            if (note.getState().getCurrentResponsible() != null) {
                responsibleTextView.setText(note.getState().getCurrentResponsible());
            }
            final TextView expiration = rootView.findViewById(R.id.expiration);
            if (note.getExpirationDate() != null) {
                expiration.setText(note.getExpirationDate().toString());
            }
            if(note.getPreviousNotes()!= null){
                final List<Note> allNotes = new ArrayList<>();
                allNotes.addAll(LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId).getAllNotes());
                for (String pNoteID: note.getPreviousNotes()) {
                    for (Note noteon: allNotes) {
                        if(noteon.getNoteID().equals(pNoteID)){
                            noteItems.add(new CollaborationComponentInfo(noteon.getNoteID(), noteon.getContent(), CollaborationComponentType.NOTE));
                        }
                    }
                }
                final DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, noteItems);
                previousNotesList.setAdapter(noteListAdapter);
                previousNotesList.setOnItemClickListener(this);
            }else
                noPreviousNoteView.setText(R.string.nonoteinserted);
            this.mapManager = new MapManager(note.getLocation(), this.getContext());
        } catch (final IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        this.mapManager.createMap((MapView) view.findViewById(R.id.mapViewLocation), savedInstanceState);
    }

    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.addToBackStack(BACKSTACK_FRAG);
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();
        } else {
            Log.e(SENDER, CREATIONERROR_FRAG);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final CollaborationComponentInfo listName = (CollaborationComponentInfo) adapterView.getItemAtPosition(position);
        selectItem(listName);
    }

    private void selectItem(CollaborationComponentInfo itemSelected) {
        Fragment openFragment = NoteFragment.newInstance(username, collaborationId, itemSelected.getId(),moduleId);
        changeFragment(openFragment);
    }

    /***
     * DA SISTEMARE SIA LA CHIAMATA AL METODO CHE IL METODO STESSO
     *
     * La progress bar è da decidere come settare i vari stati in base al valore
     *
     * @param state stato della nota contenuto in @NoteProjectState
     */
    private void setStateProgressBar(String state){
        switch(state){
            case "Doing" :{
                progressBarState.setProgress(50);
                progressBarState.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "To-do" :{
                progressBarState.setProgress(20);
                progressBarState.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "Done" :{
                progressBarState.setProgress(100);
                progressBarState.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                break;
            }
            default:{
                progressBarState.setProgress(80);
                progressBarState.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                break;
            }
        }
    }

}
