package org.gammf.collabora_android.app.gui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.gammf.collabora_android.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogNewCollaborationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogNewCollaborationFragment extends DialogFragment {

    private EditText txtCollabName;
    private RadioGroup radioGroupCollabType;
    private RadioButton radioButtonGroup;
    private RadioButton radioButtonProject;

    public DialogNewCollaborationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static DialogNewCollaborationFragment newInstance() {
        DialogNewCollaborationFragment fragment = new DialogNewCollaborationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dialog_new_collaboration, container, false);

        txtCollabName = rootView.findViewById(R.id.txtInsertCollabName);
        radioGroupCollabType = rootView.findViewById(R.id.radioGroupCollabType);
        radioButtonGroup = rootView.findViewById(R.id.radioButtonGroup);
        radioButtonProject = rootView.findViewById(R.id.radioButtonProject);
        radioButtonGroup.setChecked(true);

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_dialog_new_collaboration, null))
                .setMessage(R.string.collabfrag_title)
                .setPositiveButton(R.string.dialog_createcollab, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String insertedNoteName = txtCollabName.getText().toString();
                        String collabType = "";
                        if(insertedNoteName.equals("")){
                            Resources res = getResources();
                            txtCollabName.setError(res.getString(R.string.fieldempty));
                        }else {
                            int selectedId = radioGroupCollabType.getCheckedRadioButtonId();
                            if (selectedId == radioButtonProject.getId()) {
                                collabType = "To-do";
                                Log.e("", collabType);
                            } else if (selectedId == radioButtonGroup.getId()) {
                                collabType = "Doing";
                                Log.e("", collabType);
                            }
                            mListener.onDialogPositiveClick(DialogNewCollaborationFragment.this, insertedNoteName, collabType);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancelcollab, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.onDialogNegativeClick(DialogNewCollaborationFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    // Use this instance of the interface to deliver action events
    DialogCollabListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogCollabListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
