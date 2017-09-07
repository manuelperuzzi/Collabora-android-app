package org.gammf.collabora_android.app.utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import org.gammf.collabora_android.app.BuildConfig;
import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.MainActivity;

/**
 * This class manage permissions. It has methods to ask and check permissions.
 * Created by gab on 04/09/17.
 */
public class PermissionManager {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String TAG = PermissionManager.class.getSimpleName();

    private final MainActivity mainActivity;

    /**
     * Builds the manager.
     * @param mainActivity the application main activity.
     */
    public PermissionManager(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    /**
     * Check if permissions are already granted.
     * @return true if permissions are already grated, false otherwise.
     */
    public boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(mainActivity.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests at the user to grant permissions to the application.
     */
    public void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this.mainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(this.mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * This method has to be called when the user makes a choice about permissions.
     * @param requestCode the request code.
     * @param grantResults the results granted.
     */
    public void processPermissionsRequestResult(final int requestCode,
                                                final int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
            } else { // if user deny permissions
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                final Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mainActivity.startActivity(intent);
                            }
                        });
            }
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              final View.OnClickListener listener) {
        Snackbar.make(
                this.mainActivity.findViewById(android.R.id.content),
                this.mainActivity.getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(this.mainActivity.getString(actionStringId), listener).show();
    }

}
