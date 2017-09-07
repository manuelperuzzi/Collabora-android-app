package org.gammf.collabora_android.app.gui.authentication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.MainActivity;
import org.gammf.collabora_android.app.gui.RegistrationFragment;
import org.gammf.collabora_android.utils.AuthenticationUtils;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.gammf.collabora_android.utils.MandatoryFieldMissingException;
import org.gammf.collabora_android.utils.UserUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import cz.msebera.android.httpclient.Header;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String INTENT_TAG = "collabora-login";

    // UI references.
    private View mProgressView;
    private View mLoginFormView;
    private final LoginActivityReceiver receiver = new LoginActivityReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        // Set up the login form.
        final EditText userTextField = (EditText) findViewById(R.id.email);
        final EditText passwordTextField = (EditText) findViewById(R.id.password);

        Button loginButton = (Button) findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLoginFields()) {
                    attemptLogin(userTextField.getText().toString(), passwordTextField.getText().toString());
                }
            }

            private boolean checkLoginFields() {
                if (userTextField.getText().toString().equals("")) {
                    userTextField.setError("Insert a valid username");
                    return false;
                }
                if (passwordTextField.getText().toString().equals("")) {
                    passwordTextField.setError("Insert a password");
                    return false;
                }
                return true;
            }
        });

        final TextView passToRegister = (TextView) findViewById(R.id.text_registerL);
        passToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.signon_frame, RegistrationFragment.newInstance());
                fragmentTransaction.addToBackStack("sign-up");
                fragmentTransaction.commit();
                findViewById(R.id.signon_frame).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, new IntentFilter(INTENT_TAG));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
            findViewById(R.id.signon_frame).setVisibility(View.GONE);
        }
    }

    public void authenticationDone() {
        final Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(final String username, final String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        showProgress(true);
        String hash = BCrypt.hashpw(password, "$2a$10$2wymx/003xT1XIndPwFgPe");
        client.setBasicAuth(username,hash);
        client.get(AuthenticationUtils.GET, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

                /*loginButton.setClickable(false);
                passToRegister.setClickable(false);*/
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    final JSONObject user = new JSONObject(new String(responseBody));
                    LocalStorageUtils.writeUserToFile(getApplicationContext(), UserUtils.jsonToUser(user));
                } catch (JSONException | IOException | MandatoryFieldMissingException e) {
                    e.printStackTrace();
                }
                showProgress(false);
                authenticationDone();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Username or Password wrong! Retry", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                /*loginButton.setClickable(true);
                passToRegister.setClickable(true);*/
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private class LoginActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getStringExtra(INTENT_TAG)) {
                case "show-progress-bar":
                    showProgress(true);
                    break;
                case "hide-progress-bar":
                    showProgress(false);
                    break;
                case "authentication-ok":
                    showProgress(false);
                    authenticationDone();
                    break;
                default:
            }
        }
    }
}