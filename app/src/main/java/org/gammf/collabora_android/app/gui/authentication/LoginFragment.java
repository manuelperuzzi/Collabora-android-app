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
import org.gammf.collabora_android.utils.AuthenticationUtils;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.gammf.collabora_android.utils.MandatoryFieldMissingException;
import org.gammf.collabora_android.utils.UserUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

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
        String hash = BCrypt.hashpw(password, "$2a$10$2wymx/003xT1XIndPwFgPe");
        client.setBasicAuth(username,hash);
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
                    final JSONObject user = new JSONObject(new String(responseBody));
                    LocalStorageUtils.writeUserToFile(getContext(), UserUtils.jsonToUser(user));
                    final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                    intent.putExtra(AuthenticationActivity.INTENT_TAG, "authentication-ok");
                    LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
                } catch (JSONException | IOException | MandatoryFieldMissingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                intent.putExtra(AuthenticationActivity.INTENT_TAG, "hide-progress-bar");
                LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getContext(), "Username or Password wrong! Retry", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                /*loginButton.setClickable(true);
                passToRegister.setClickable(true);*/
            }
        });
    }

}
