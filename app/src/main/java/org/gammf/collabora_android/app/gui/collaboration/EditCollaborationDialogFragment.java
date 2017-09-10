package org.gammf.collabora_android.app.gui.collaboration;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by mperuzzi on 07/09/17.
 */

public class EditCollaborationDialogFragment extends DialogFragment {

    private static final String ARG_COLLABORATION_ID = "collaborationId";
    private static final String ARG_USERNAME = "username";

    private String username;
    private Collaboration collaboration;

    private InputMethodManager inputMethodManager;
    private EditText txtCollaborationName;

    public static EditCollaborationDialogFragment newInstance(final String collaborationId, final String username) {
        final EditCollaborationDialogFragment fragment = new EditCollaborationDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_COLLABORATION_ID, collaborationId);
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            final String collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            try {
                this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
            } catch (final IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_dialog_editcollaboration, container, false);
        initializeDialogGuiComponent(rootView);
        return rootView;
    }

    private void initializeDialogGuiComponent(final View rootView) {
        txtCollaborationName = rootView.findViewById(R.id.txtInsertCollaborationName);
        txtCollaborationName.setText(collaboration.getName());

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        final Button addButton = rootView.findViewById(R.id.btnPositiveAddCollaboration);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processCollaborationOperation();
            }
        });
        final Button cancelButton = rootView.findViewById(R.id.btnNegativeAddCollaboration);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(txtCollaborationName.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void processCollaborationOperation() {
        final String newCollaborationName = txtCollaborationName.getText().toString();
        if (newCollaborationName.equals("")) {
            txtCollaborationName.setError("Field required");
        } else {
            this.inputMethodManager.hideSoftInputFromWindow(txtCollaborationName.getWindowToken(), 0);
            if (! collaboration.getName().equals(newCollaborationName)) {
                collaboration.setName(newCollaborationName);
                final UpdateMessage message = new ConcreteCollaborationUpdateMessage(username, collaboration, UpdateMessageType.UPDATING);
                new SendMessageToServerTask(getContext()).execute(message);
            }
            dismiss();
        }
    }
}
