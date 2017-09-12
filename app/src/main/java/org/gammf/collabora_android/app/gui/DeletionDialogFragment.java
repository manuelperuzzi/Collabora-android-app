package org.gammf.collabora_android.app.gui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.gammf.collabora_android.utils.SingletonAppUser;

public class DeletionDialogFragment  extends android.support.v4.app.DialogFragment{

    private String collaborationId,componentId,componentContent;
    private Collaboration collaboration;
    private CollaborationComponentType componentType;
    private static final String ARG_COLLABORATION_ID = "collaborationId";
    private static final String ARG_COMPONENTID = "componentId";
    private static final String ARG_COMPONENTTYPE = "componentType";
    private static final String ARG_COMPONENTCONTENT = "componentContent";

    public static DeletionDialogFragment newInstance(String collaborationId,String componentId, String componentContent, CollaborationComponentType componentType){
        final DeletionDialogFragment fragment = new DeletionDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_COLLABORATION_ID, collaborationId);
        args.putString(ARG_COMPONENTID,componentId);
        args.putString(ARG_COMPONENTCONTENT,componentContent);
        args.putSerializable(ARG_COMPONENTTYPE,componentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            this.componentId = getArguments().getString(ARG_COMPONENTID);
            this.componentContent = getArguments().getString(ARG_COMPONENTCONTENT);
            this.componentType = (CollaborationComponentType) getArguments().getSerializable(ARG_COMPONENTTYPE);
            this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Warning - deleting "+componentType+"!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to delete the "+componentType+": "+ componentContent+" ? "+"(this operation cannot be undone)")
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (componentType.equals(CollaborationComponentType.MODULE)) {
                            deleteModule(((Project)collaboration).getModule(componentId));
                        }else{
                            deleteNote(collaboration.getNote(componentId));
                        }
                        dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                })
        .create();
    }

    private void deleteNote(Note noteToDelete) {
        new SendMessageToServerTask(getContext()).execute(new ConcreteNoteUpdateMessage(
                SingletonAppUser.getInstance().getUsername(), noteToDelete, UpdateMessageType.DELETION, collaborationId));
    }

    private void deleteModule(Module moduleToDelete) {
        new SendMessageToServerTask(getContext()).execute(new ConcreteModuleUpdateMessage(
                SingletonAppUser.getInstance().getUsername(), moduleToDelete, UpdateMessageType.DELETION, collaborationId));
    }
}
