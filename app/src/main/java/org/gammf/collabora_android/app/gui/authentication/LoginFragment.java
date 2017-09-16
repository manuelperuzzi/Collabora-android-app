package org.gammf.collabora_android.app.gui.authentication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.model.short_collaborations.ConcreteCollaborationManager;
import org.gammf.collabora_android.model.short_collaborations.ConcreteShortCollaboration;
import org.gammf.collabora_android.utils.communication.AuthenticationUtils;
import org.gammf.collabora_android.utils.model.CollaborationUtils;
import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.utils.app.LocalStorageUtils;
import org.gammf.collabora_android.utils.model.UserUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
 /*
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText userText;
    private EditText passText;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        userText = rootView.findViewById(R.id.username);
        passText = rootView.findViewById(R.id.password);
        final Button loginButton = rootView.findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLoginFields()) {
                    attemptLogin(userText.getText().toString(), passText.getText().toString());
                }
            }

            private boolean checkLoginFields() {
                if (userText.getText().toString().equals("")) {
                    userText.setError("Insert a valid username");
                    return false;
                }
                if (passText.getText().toString().equals("")) {
                    passText.setError("Insert a password");
                    return false;
                }
                return true;
            }
        });
        final TextView passToRegister = rootView.findViewById(R.id.text_registerL);
        passToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                intent.putExtra(AuthenticationActivity.INTENT_TAG, "pass-to-register");
                LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
            }
        });
        return rootView;
    }


    private void attemptLogin(final String username, final String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(username, HashingUtils.hashString(password));
        client.get(AuthenticationUtils.GET, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                intent.putExtra(AuthenticationActivity.INTENT_TAG, "show-progress-bar");
                LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    writeLoginInfoToFile(new JSONObject(new String(responseBody)));
                    final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                    intent.putExtra(AuthenticationActivity.INTENT_TAG, "authentication-ok");
                    LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
                } catch (final JSONException e) {
                    ExceptionManager.getInstance().handle(e);
                }
            }

            private void writeLoginInfoToFile(final JSONObject json) {
                try {
                    LocalStorageUtils.writeUserToFile(getContext(), UserUtils.jsonToUser(json.getJSONObject("user")));

                    final CollaborationsManager manager = new ConcreteCollaborationManager();
                    final JSONArray jCollaborations = json.getJSONArray("collaborations");
                    for (int i = 0; i < jCollaborations.length(); i++) {
                        final Collaboration collaboration = CollaborationUtils.jsonToCollaboration(jCollaborations.getJSONObject(i));
                        manager.addCollaboration(new ConcreteShortCollaboration(collaboration));
                        LocalStorageUtils.writeCollaborationToFile(getContext(), collaboration);
                    }
                    LocalStorageUtils.writeShortCollaborationsToFile(getContext(), manager);
                } catch (final JSONException e) {
                    ExceptionManager.getInstance().handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                intent.putExtra(AuthenticationActivity.INTENT_TAG, "hide-progress-bar");
                LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getContext(), "Username or Password wrong! Retry", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
