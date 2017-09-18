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
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.gui.map.MapManager;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.model.CollaborationType;
import org.gammf.collabora_android.utils.app.LocalStorageUtils;
import org.gammf.collabora_android.utils.app.SingletonAppUser;
import org.joda.time.format.DateTimeFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment implements AdapterView.OnItemClickListener{

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String SENDER = "notefrag";

    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_NOTEID = "noteId";
    private static final String ARG_MODULEID = "moduleName";

    private String collaborationId,moduleId;
    private String noteId;
    private Note note;
    private Collaboration collaboration;
    private ListView previousNotesList;
    private MapManager mapManager;
    private ProgressBar progressBarState;
    private TextView stateTextView;

    public NoteFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of this fragment
     *
     * @param collabId the collaboration ID that contain the note
     * @param noteId the note ID
     * @param moduleId the Module ID of the note
     *
     * @return A new instance of fragment NoteFragment.
     */
    public static NoteFragment newInstance(String collabId, String noteId,String moduleId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
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
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.noteId = getArguments().getString(ARG_NOTEID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }
        this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
        this.note = collaboration.getNote(noteId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (collaboration.getCollaborationType().equals(CollaborationType.PRIVATE) ||
                !((SharedCollaboration)collaboration).getMember(SingletonAppUser.getInstance().getUsername()).getAccessRight().equals(AccessRight.READ)) {
            inflater.inflate(R.menu.edit_note, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        Method for editcollaboration click on toolbar
        trigger the @CollaborationInfoFragment
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_editnote) {
            Fragment editNoteFragment = EditNoteFragment.newInstance(collaborationId, noteId,moduleId);
            changeFragment(editNoteFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        previousNotesList = rootView.findViewById(R.id.listViewPNote);
        initializeGuiComponent(rootView);
        setStateProgressBar(stateTextView.getText().toString());
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        this.mapManager.createMap((MapView) view.findViewById(R.id.mapViewLocation), savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final CollaborationComponentInfo listName = (CollaborationComponentInfo) adapterView.getItemAtPosition(position);
        selectItem(listName);
    }

    private void initializeGuiComponent(final View rootView) {
        ((TextView) rootView.findViewById(R.id.contentNote)).setText(note.getContent());
        progressBarState = rootView.findViewById(R.id.progressBarState);
        final TextView noPreviousNoteView = rootView.findViewById(R.id.noPNote);
        this.stateTextView = rootView.findViewById(R.id.lblState);
        this.stateTextView.setText(note.getState().getCurrentDefinition());

        final TextView responsibleTextView = rootView.findViewById(R.id.lblResponsible);
        if (note.getState().getCurrentResponsible() != null) {
            responsibleTextView.setText(note.getState().getCurrentResponsible());
        }
        final TextView expiration = rootView.findViewById(R.id.expiration);
        if (note.getExpirationDate() != null) {
            expiration.setText(note.getExpirationDate().toString(DateTimeFormat.mediumDateTime()));
        }
        if (note.getPreviousNotes() != null) {
            final DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, NoteFragmentUtils.fillListView(getContext(), note, collaborationId));
            previousNotesList.setAdapter(noteListAdapter);
            previousNotesList.setOnItemClickListener(this);
        } else {
            noPreviousNoteView.setText(R.string.nonoteinserted);
        }
        this.mapManager = new MapManager(note.getLocation(), this.getContext());
    }

    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(BACKSTACK_FRAG);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        } else {
            Log.e(SENDER, CREATIONERROR_FRAG);
        }
    }

    private void selectItem(CollaborationComponentInfo itemSelected) {
        Fragment openFragment = NoteFragment.newInstance(collaborationId, itemSelected.getId(),moduleId);
        changeFragment(openFragment);
    }

    /***
     * Set the bar over the note title in different colors, based on the note state.
     * @param state the note state.
     */
    private void setStateProgressBar(String state){
        switch(state) {
            case "To Do" :{
                progressBarState.setProgress(10);
                progressBarState.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "Doing" :{
                progressBarState.setProgress(50);
                progressBarState.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "Done" :{
                progressBarState.setProgress(100);
                progressBarState.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "Review" :{
                progressBarState.setProgress(100);
                progressBarState.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "Testing" :{
                progressBarState.setProgress(100);
                progressBarState.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "Blocked" :{
                progressBarState.setProgress(100);
                progressBarState.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                break;
            }
            default:{
                progressBarState.setProgress(0);
            }
        }
    }

}
