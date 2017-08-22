package org.gammf.collabora_android.app.gui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.gammf.collabora_android.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogNewCollaborationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogNewCollaborationFragment extends DialogFragment {


    // Use this instance of the interface to deliver action events
    private DialogCollabListener mListener;
    //ui elements
    private EditText txtCollabName;
    private RadioGroup radioGroupCollabType;
    private RadioButton radioButtonGroup;
    private RadioButton radioButtonProject;
    //manager for keyboard
    private InputMethodManager inputMethodManager;

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

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_new_collaboration, null);
        txtCollabName = view.findViewById(R.id.txtInsertCollabNameD);
        radioGroupCollabType = view.findViewById(R.id.radioGroupCollabType);
        radioButtonGroup = view.findViewById(R.id.radioButtonGroup);
        radioButtonProject = view.findViewById(R.id.radioButtonProject);
        radioButtonGroup.setChecked(true);

        inputMethodManager =
                (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


        builder.setView(view)
                .setMessage(R.string.collabfrag_title)
                .setPositiveButton(R.string.dialog_createcollab, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String insertedNoteName = txtCollabName.getText().toString();
                        String collabType = "";
                        if(insertedNoteName.equals("")){
                            Context context = getActivity().getApplicationContext();
                            CharSequence text = "Creation failed: name not inserted!";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            inputMethodManager.hideSoftInputFromWindow(txtCollabName.getWindowToken(), 0);
                        }else {
                            int selectedId = radioGroupCollabType.getCheckedRadioButtonId();
                            if (selectedId == radioButtonProject.getId()) {
                                collabType = "Project";
                                Log.e("", collabType);
                            } else if (selectedId == radioButtonGroup.getId()) {
                                collabType = "Group";
                                Log.e("", collabType);
                            }
                            inputMethodManager.hideSoftInputFromWindow(txtCollabName.getWindowToken(), 0);
                            mListener.onDialogPositiveClick(DialogNewCollaborationFragment.this, insertedNoteName, collabType);
                        }

                    }
                })
                .setNegativeButton(R.string.dialog_cancelcollab, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        inputMethodManager.hideSoftInputFromWindow(txtCollabName.getWindowToken(), 0);
                        mListener.onDialogNegativeClick(DialogNewCollaborationFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

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
