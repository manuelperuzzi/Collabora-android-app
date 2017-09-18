package org.gammf.collabora_android.app;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Firebase default service that handles the token refresh.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Refreshed token: " + FirebaseInstanceId.getInstance().getToken());
    }
}

