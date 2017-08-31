package org.gammf.collabora_android.app.gui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.utils.AuthenticationUtils;
import org.mindrot.jbcrypt.BCrypt;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.mime.MIME;

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
        Button loginButton = rootView.findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(userText.getText().toString(),passText.getText().toString());
            }
        });
        TextView passToRegister = rootView.findViewById(R.id.text_registerL);
        passToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment registrationFragment = RegistrationFragment.newInstance();
                changeFragment(registrationFragment);
            }
        });
        return rootView;
    }

    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();
            Log.d("loginfragment","passo a register");
        } else {
            Log.d("loginfragment","errore nel cambio");
        }
    }

    private void attemptLogin(String username, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        client.setBasicAuth(username,hash);
        client.get(AuthenticationUtils.GET, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //tornare a homepage!!
                //Toast in caso si successo solo per ora che non c'è ancora una homepage
                Toast toast = Toast.makeText(getContext(), "Logged correctly!",  Toast.LENGTH_SHORT);
                toast.show();
                ((MainActivity)getActivity()).riputMenu();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast toast = Toast.makeText(getContext(), "Username or Password wrong! Retry", Toast.LENGTH_SHORT);
                toast.show();
            }
        });



    }
}
