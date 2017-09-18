package org.gammf.collabora_android.app.gui.collaboration;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.utils.app.LocalStorageUtils;
import org.gammf.collabora_android.utils.app.SingletonAppUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCollaborationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCollaborationDialogFragment extends DialogFragment {

    private static final String ARG_COLLABORATION_ID = "collaborationId";

    private Collaboration collaboration;

    private InputMethodManager inputMethodManager;
    private EditText txtCollaborationName;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param collaborationId the id of the collaboration to be edited.
     *
     * @return a new istance of EditCollaborationDialogFragment.
     */
    public static EditCollaborationDialogFragment newInstance(final String collaborationId) {
        final EditCollaborationDialogFragment fragment = new EditCollaborationDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_COLLABORATION_ID, collaborationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final String collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_dialog_editcollaboration, container, false);
        initializeDialogGuiComponent(rootView);
        return rootView;
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
                final UpdateMessage message = new ConcreteCollaborationUpdateMessage(SingletonAppUser.getInstance().getUsername(), collaboration, UpdateMessageType.UPDATING);
                new SendMessageToServerTask(getContext()).execute(message);
            }
            dismiss();
        }
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
}
