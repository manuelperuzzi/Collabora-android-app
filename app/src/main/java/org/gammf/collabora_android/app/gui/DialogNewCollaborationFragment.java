package org.gammf.collabora_android.app.gui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

    private static final String TOAST_ERR_NAMEREQUIRED = "Creation failed: name not inserted!";
    private static final String TOAST_ERR_CANCELCREATION = "Creation discarded";

    private static final String TYPE_PROJECT = "Project";
    private static final String TYPE_GROUP = "Group";

    // Use this instance of the interface to deliver action events
    private DialogCollabListener mListener;

    //ui elements
    private EditText txtCollabName;
    private RadioGroup radioGroupCollabType;
    private RadioButton radioButtonGroup;
    private RadioButton radioButtonProject;
    private Button btnPositiveClick, btnNegativeClick;

    //manager for keyboard
    private InputMethodManager inputMethodManager;

    public DialogNewCollaborationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
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
        txtCollabName = rootView.findViewById(R.id.txtInsertCollabNameD);
        radioGroupCollabType = rootView.findViewById(R.id.radioGroupCollabType);
        radioButtonGroup = rootView.findViewById(R.id.radioButtonGroup);
        radioButtonProject = rootView.findViewById(R.id.radioButtonProject);
        radioButtonGroup.setChecked(true);
        inputMethodManager =
                (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        btnPositiveClick = rootView.findViewById(R.id.btnPositiveAddCollab);

        //CLICK ON CREATE BUTTON
        btnPositiveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPositiveClick();
            }
        });

        btnNegativeClick = rootView.findViewById(R.id.btnNegativePositiveAddCollab);
        //CANCEL CLICK EVENT
        btnNegativeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User cancelled the dialog
                Toast toast =
                        Toast.makeText(getActivity().getApplicationContext(), TOAST_ERR_CANCELCREATION, Toast.LENGTH_SHORT);
                toast.show();
                inputMethodManager.hideSoftInputFromWindow(txtCollabName.getWindowToken(), 0);
                mListener.onDialogCancelClick(DialogNewCollaborationFragment.this);
            }
        });

        return rootView;
    }

    private void processPositiveClick(){
        //getting the collabName
        String insertedNoteName = txtCollabName.getText().toString();
        String collabType = "";
        //check if collabName is empty
        if(insertedNoteName.equals("")){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), TOAST_ERR_NAMEREQUIRED, Toast.LENGTH_LONG);
            toast.show();
            inputMethodManager.hideSoftInputFromWindow(txtCollabName.getWindowToken(), 0);
            mListener.onDialogCancelClick(DialogNewCollaborationFragment.this);

        }else {
            //if it's not empty, get the collabtype
            int selectedId = radioGroupCollabType.getCheckedRadioButtonId();
            if (selectedId == radioButtonProject.getId()) {
                collabType = TYPE_PROJECT;
            } else if (selectedId == radioButtonGroup.getId()) {
                collabType = TYPE_GROUP;
            }
            inputMethodManager.hideSoftInputFromWindow(txtCollabName.getWindowToken(), 0);
            //and send the info to MainActivity
            mListener.onDialogCreateClick(DialogNewCollaborationFragment.this, insertedNoteName, collabType);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
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
