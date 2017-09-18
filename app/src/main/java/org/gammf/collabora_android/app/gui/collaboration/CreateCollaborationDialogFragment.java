package org.gammf.collabora_android.app.gui.collaboration;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.ConcreteGroup;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.model.users.SimpleCollaborationMember;
import org.gammf.collabora_android.model.users.User;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.model.CollaborationType;
import org.gammf.collabora_android.utils.app.SingletonAppUser;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link CreateCollaborationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCollaborationDialogFragment extends DialogFragment {

    private EditText editTextCollaborationName;
    private RadioGroup radioGroupCollaborationType;

    private InputMethodManager inputMethodManager;

    public CreateCollaborationDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return a new istance of CreateCollaborationDialogFragment.
     */
    public static CreateCollaborationDialogFragment newInstance() {
        return new CreateCollaborationDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_dialog_new_collaboration, container, false);
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

    private void createCollaboration(final String collaborationName, final CollaborationType type) {
        final SharedCollaboration collaboration = type.equals(CollaborationType.GROUP) ? new ConcreteGroup(null, collaborationName) : new ConcreteProject(null, collaborationName);
        final User user = SingletonAppUser.getInstance().getUser();
        collaboration.addMember(new SimpleCollaborationMember(user.getUsername(), AccessRight.ADMIN));
        final UpdateMessage message = new ConcreteCollaborationUpdateMessage(user.getUsername(),
                collaboration, UpdateMessageType.CREATION);
        new SendMessageToServerTask(getContext().getApplicationContext()).execute(message);
        this.dismiss();
    }

    private void initializeDialogGuiComponent(View rootView) {
        editTextCollaborationName = rootView.findViewById(R.id.txtInsertCollabNameD);
        radioGroupCollaborationType = rootView.findViewById(R.id.radioGroupCollabType);
        this.radioGroupCollaborationType.check(R.id.radioButtonGroup);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        final Button createButton = rootView.findViewById(R.id.btnPositiveAddCollab);
        final Button cancelButton = rootView.findViewById(R.id.btnNegativePositiveAddCollab);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPositiveClick();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(editTextCollaborationName.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    private void processPositiveClick(){
        final String collaborationName = this.editTextCollaborationName.getText().toString();
        if(collaborationName.equals("")) {
            this.editTextCollaborationName.setError("Field required");
        } else {
            final CollaborationType type = this.radioGroupCollaborationType.getCheckedRadioButtonId() == R.id.radioButtonGroup ? CollaborationType.GROUP : CollaborationType.PROJECT;
            this.inputMethodManager.hideSoftInputFromWindow(this.editTextCollaborationName.getWindowToken(), 0);
            this.createCollaboration(collaborationName, type);
        }
    }
}
