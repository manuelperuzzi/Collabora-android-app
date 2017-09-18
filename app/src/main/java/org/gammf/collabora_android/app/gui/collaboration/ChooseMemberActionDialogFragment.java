package org.gammf.collabora_android.app.gui.collaboration;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.members.ConcreteMemberUpdateMessage;
import org.gammf.collabora_android.model.users.CollaborationMember;
import org.gammf.collabora_android.model.users.SimpleCollaborationMember;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.app.SingletonAppUser;

/**
 * Created by mperuzzi on 09/09/17.
 *
 * A simple {@link DialogFragment} subclass.
 * Use the {@link ChooseMemberActionDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ChooseMemberActionDialogFragment extends DialogFragment {

    private static final String EDIT_MEMBER_DIALOG_TAG = "EditMemberDialogTag";

    private static final String ARG_COLLABORATION_ID = "collaborationId";
    private static final String ARG_MEMBER_USERNAME = "memberUsername";
    private static final String ARG_MEMBER_RIGHT = "right";

    private String collaborationId;
    private String memberUsername;
    private String memberRight;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param memberRight the right of the member.
     * @param memberUsername the username of the member.
     * @param collaborationId collaboration id
     *
     * @return A new instance of fragment ChooseMemberActionDialogFragment.
     */
    public static ChooseMemberActionDialogFragment newInstance(final String collaborationId,
                                                               final String memberUsername,
                                                               final String memberRight) {
        final ChooseMemberActionDialogFragment fragment = new ChooseMemberActionDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_COLLABORATION_ID, collaborationId);
        args.putString(ARG_MEMBER_USERNAME, memberUsername);
        args.putString(ARG_MEMBER_RIGHT, memberRight);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            this.memberUsername = getArguments().getString(ARG_MEMBER_USERNAME);
            this.memberRight = getArguments().getString(ARG_MEMBER_RIGHT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_dialog_choose_member_action, container, false);
        initializeDialogGuiComponent(rootView);
        return rootView;
    }

    private void initializeDialogGuiComponent(final View rootView) {
        final Button btnEdit = rootView.findViewById(R.id.btnEditMember);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MemberDialogFragment dialog = MemberDialogFragment.updateMemberInstance(
                        collaborationId, memberUsername, memberRight);
                dialog.show(getActivity().getSupportFragmentManager(), EDIT_MEMBER_DIALOG_TAG);
                dismiss();
            }
        });
        final Button btnRemove = rootView.findViewById(R.id.btnRemoveMember);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CollaborationMember member = new SimpleCollaborationMember(memberUsername, AccessRight.valueOf(memberRight));
                final UpdateMessage message = new ConcreteMemberUpdateMessage(SingletonAppUser.getInstance().getUsername(), member, UpdateMessageType.DELETION, collaborationId);
                new SendMessageToServerTask(getContext()).execute(message);
                dismiss();
            }
        });
    }
}
