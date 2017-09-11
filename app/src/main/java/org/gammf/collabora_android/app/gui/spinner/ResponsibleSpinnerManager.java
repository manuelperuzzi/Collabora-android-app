package org.gammf.collabora_android.app.gui.spinner;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.gammf.collabora_android.app.utils.AbstractObservableSource;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.utils.LocalStorageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A manager for the spinner used to select the responsible. It is a {@link org.gammf.collabora_android.app.utils.ObservableSource<String>},
 * and sent the note state to observers when the user change the spinner selection.
 */
public class ResponsibleSpinnerManager extends AbstractObservableSource<String> implements AdapterView.OnItemSelectedListener {

    public static final String NO_RESPONSIBLE = "";

    private static final String ERROR_RESPONSIBLE_NOT_SELECTED = "Please select a responsible";

    private View rootView;

    /**
     * Build a new spinner manager.
     * @param responsible the responsible, if any. If the note has no responsible setted, use NO_RESPONSIBLE
     * @param rootView the {@link View} where this spinner is.
     * @param spinnerId the ID of the spinner, in the Android res file.
     * @param collaborationId the id of the collaboration, used for retrieve users.
     */
    public ResponsibleSpinnerManager(final String responsible,
                                     final View rootView,
                                     final int spinnerId,
                                     final String collaborationId) {
        this.rootView = rootView;
        final Spinner spinnerEditResponsible = rootView.findViewById(spinnerId);



        final List<String> responsibleList = new ArrayList<>();
        for(CollaborationMember member : ((SharedCollaboration)LocalStorageUtils.readCollaborationFromFile(this.rootView.getContext().getApplicationContext(),
                collaborationId)).getAllMembers()) {
            responsibleList.add(member.getUsername());
        }
        responsibleList.add(0, NO_RESPONSIBLE);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.rootView.getContext(),
                android.R.layout.simple_spinner_item, responsibleList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditResponsible.setAdapter(dataAdapter);
        if (responsible.equals(NO_RESPONSIBLE)) {
            spinnerEditResponsible.setSelection(0);
        } else {
            int responsibleIndex = 0;
            for (int i = 0; i < responsibleList.size(); i++) {
                if (responsibleList.get(i).equals(responsible)) {
                    responsibleIndex = i;
                }
            }
            spinnerEditResponsible.setSelection(responsibleIndex);
        }
        spinnerEditResponsible.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.notifyObservers((String)adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this.rootView.getContext().getApplicationContext(), ERROR_RESPONSIBLE_NOT_SELECTED, Toast.LENGTH_LONG).show();
    }
}
