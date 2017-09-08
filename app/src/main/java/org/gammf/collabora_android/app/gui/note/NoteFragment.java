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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.map.MapManager;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class NoteFragment extends Fragment {

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String SENDER = "notefrag";

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_NOTEID = "noteId";

    private String username;
    private String collaborationId;
    private String noteId;

    private MapManager mapManager;

    private ProgressBar progressBarState;
    private TextView stateTextView;

    public NoteFragment() {
        setHasOptionsMenu(true);
    }

    public static NoteFragment newInstance(String username, String collabId, String noteId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_COLLABID, collabId);
        args.putString(ARG_NOTEID, noteId);
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
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_note, menu);
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
            Fragment editNoteFragment = EditNoteFragment.newInstance(username, collaborationId, noteId);
            changeFragment(editNoteFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);

        initializeGuiComponent(rootView);
        setStateProgressBar(stateTextView.getText().toString());

        return rootView;
    }

    private void initializeGuiComponent(final View rootView) {
        try {
            final Note note = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId).getNote(noteId);
            ((TextView) rootView.findViewById(R.id.contentNote)).setText(note.getContent());

            progressBarState = rootView.findViewById(R.id.progressBarState);

            this.stateTextView = rootView.findViewById(R.id.lblState);
            this.stateTextView.setText(note.getState().getCurrentState());

            final TextView responsibleTextView = rootView.findViewById(R.id.lblResponsible);
            if (note.getState().getCurrentResponsible() != null) {
                responsibleTextView.setText(note.getState().getCurrentResponsible());
            }

            final TextView expiration = rootView.findViewById(R.id.expiration);
            if (note.getExpirationDate() != null) {
                expiration.setText(note.getExpirationDate().toString(DateTimeFormat.mediumDateTime()));
            }

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
