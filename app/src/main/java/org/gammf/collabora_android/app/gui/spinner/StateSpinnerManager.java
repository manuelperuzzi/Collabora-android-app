package org.gammf.collabora_android.app.gui.spinner;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.utils.AbstractObservableSource;
import org.gammf.collabora_android.app.utils.NoteGroupState;
import org.gammf.collabora_android.app.utils.NoteProjectState;
import org.gammf.collabora_android.utils.CollaborationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by gab on 9/6/17.
 */

public class StateSpinnerManager extends AbstractObservableSource<String> implements AdapterView.OnItemSelectedListener {

    private static final String ERROR_STATE_NOT_SELECTED = "Please select state";

    private final View rootView;

    public StateSpinnerManager(final View rootView, final CollaborationType collaborationType) {
        this.rootView = rootView;
        final Spinner spinnerEditState = rootView.findViewById(R.id.spinnerNewNoteState);
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
