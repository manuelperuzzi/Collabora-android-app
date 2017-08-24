package org.gammf.collabora_android.app.gui;

import android.support.v4.app.DialogFragment;

/**
 * Created by Mattia on 20/08/2017.
 */

public interface DialogCollabListener {
    void onDialogCreateClick(DialogFragment dialog, String collabName, String collabType);
    void onDialogCancelClick(DialogFragment dialog);
}
