package org.gammf.collabora_android.app.gui.spinner;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.gammf.collabora_android.app.utils.AbstractObservableSource;
import org.gammf.collabora_android.app.utils.NoteGroupState;
import org.gammf.collabora_android.app.utils.NoteProjectState;
import org.gammf.collabora_android.utils.CollaborationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A state spinner manager, that manages the behaviour of a spinner used for selecting the note/module state.
 */
public class StateSpinnerManager extends AbstractObservableSource<String> implements AdapterView.OnItemSelectedListener {

    /**
     * Represents the unknown state of a note.
     */
    public static final String NO_STATE = "";

    private static final String ERROR_STATE_NOT_SELECTED = "Please select state";

    private final View rootView;

    /**
     * Builds the spinner manager.
     * @param state the state of the module/note. If no state is selected use NO_STATE
     * @param rootView the {@link View} where this spinner is.
     * @param spinnerId the ID of the spinner, in the Android res file.
     * @param collaborationType the type of the collaboration.
     */
    public StateSpinnerManager(final String state, final View rootView, final int spinnerId, final CollaborationType collaborationType) {
        this.rootView = rootView;
        final Spinner spinnerEditState = rootView.findViewById(spinnerId);
        final List<Enum> stateList = new ArrayList<>();
        if (collaborationType == CollaborationType.PROJECT) {
            stateList.addAll(Arrays.asList(NoteProjectState.values()));
        } else {
            stateList.addAll(Arrays.asList(NoteGroupState.values()));
        }
        final ArrayAdapter<Enum> dataAdapter = new ArrayAdapter<>(this.rootView.getContext(),
                android.R.layout.simple_spinner_item, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditState.setAdapter(dataAdapter);
        if (state.equals(NO_STATE)) {
            spinnerEditState.setSelection(0);
        } else {
            int stateIndex = 0;
            for (int i = 0; i < stateList.size(); i++) {
                if (stateList.get(i).toString().equals(state)) {
                    stateIndex = i;
                }
            }
            spinnerEditState.setSelection(stateIndex);
        }
        spinnerEditState.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        final Enum state = (Enum) adapterView.getItemAtPosition(i);
        this.notifyObservers(state.toString());
    }

    @Override
    public void onNothingSelected(final AdapterView<?> adapterView) {
        Toast.makeText(this.rootView.getContext().getApplicationContext(), ERROR_STATE_NOT_SELECTED, Toast.LENGTH_LONG).show();
    }
}
