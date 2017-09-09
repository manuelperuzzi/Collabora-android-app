package org.gammf.collabora_android.app.gui.collaboration;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.support.v4.app.DialogFragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.members.ConcreteMemberUpdateMessage;
import org.gammf.collabora_android.communication.update.members.MemberUpdateMessage;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.AccessRight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mperuzzi on 07/09/17.
 */

public class MemberDialogFragment extends DialogFragment {

    private static final String ARG_COLLABORATION_ID = "collaborationId";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_MEMBER_USERNAME = "memberUsername";
    private static final String ARG_MEMBER_RIGHT = "right";

    private String username;
    private String collaborationId;
    private String previousMemberUsername;
    private String previousMemberRight;

    private InputMethodManager inputMethodManager;
    private Spinner spinnerRight;
    private EditText txtUsername;

    public static MemberDialogFragment addMemberInstance(final String collaborationId, final String username) {
        final MemberDialogFragment fragment = new MemberDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_COLLABORATION_ID, collaborationId);
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberDialogFragment updateMemberInstance(final String collaborationId, final String username,
                                                            final String memberUsername, final String memberRight) {
        final MemberDialogFragment fragment = new MemberDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_COLLABORATION_ID, collaborationId);
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_MEMBER_USERNAME, memberUsername);
        args.putString(ARG_MEMBER_RIGHT, memberRight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            this.username = getArguments().getString(ARG_USERNAME);
            this.previousMemberUsername = getArguments().getString(ARG_MEMBER_USERNAME);
            this.previousMemberRight = getArguments().getString(ARG_MEMBER_RIGHT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_dialog_member, container, false);
        initializeDialogGuiComponent(rootView);
        return rootView;
    }

    private void initializeDialogGuiComponent(final View rootView) {
        txtUsername = rootView.findViewById(R.id.txtInsertUsername);
        final Button addButton = rootView.findViewById(R.id.btnPositiveAddMember);
        if (previousMemberUsername != null) {
            txtUsername.setText(previousMemberUsername);
            txtUsername.setKeyListener(null);
            addButton.setText("Edit");
        } else {
            addButton.setText("Add");
        }
        initializeSpinnerComponent(rootView);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processMemberOperation();
            }
        });
        final Button cancelButton = rootView.findViewById(R.id.btnNegativeAddMember);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(txtUsername.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    private void initializeSpinnerComponent (final View rootView) {
        spinnerRight = rootView.findViewById(R.id.spinnerMemberRight);
        final List<AccessRight> stateList = new ArrayList<>();
        stateList.addAll(Arrays.asList(AccessRight.values()));
        final ArrayAdapter<AccessRight> dataAdapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_spinner_item, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRight.setAdapter(dataAdapter);
        if (previousMemberRight == null) {
            spinnerRight.setSelection(2);
        } else {
            int stateIndex = 0;
            for (int i = 0; i < stateList.size(); i++) {
                if (stateList.get(i).toString().equals(previousMemberRight)) {
                    stateIndex = i;
                }
            }
            spinnerRight.setSelection(stateIndex);
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void processMemberOperation() {
        final String newUsername = txtUsername.getText().toString();
        if (newUsername.equals("")) {
            txtUsername.setError("Field required");
        } else {
            final AccessRight newRight = (AccessRight) spinnerRight.getSelectedItem();
            this.inputMethodManager.hideSoftInputFromWindow(txtUsername.getWindowToken(), 0);
            if (previousMemberUsername == null) {
                handleMemberOperation(newUsername, newRight, UpdateMessageType.CREATION);
            } else if (! previousMemberRight.equals(newRight)) {
                handleMemberOperation(newUsername, newRight, UpdateMessageType.UPDATING);
            }
            dismiss();
        }
    }

    private void handleMemberOperation(final String memberUsername, final AccessRight memberRight, final UpdateMessageType type) {
        final CollaborationMember member = new SimpleCollaborationMember(memberUsername, memberRight);
        final MemberUpdateMessage message = new ConcreteMemberUpdateMessage(username, member, type, collaborationId);
        new SendMessageToServerTask(getContext()).execute(message);
    }
}
