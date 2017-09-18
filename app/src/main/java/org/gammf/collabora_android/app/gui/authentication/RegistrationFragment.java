package org.gammf.collabora_android.app.gui.authentication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.model.short_collaborations.ConcreteCollaborationManager;
import org.gammf.collabora_android.model.short_collaborations.ConcreteShortCollaboration;
import org.gammf.collabora_android.model.users.SimpleUser;
import org.gammf.collabora_android.model.users.User;
import org.gammf.collabora_android.utils.communication.AuthenticationUtils;
import org.gammf.collabora_android.utils.model.CollaborationUtils;
import org.gammf.collabora_android.utils.app.LocalStorageUtils;
import org.gammf.collabora_android.utils.model.MandatoryFieldMissingException;
import org.gammf.collabora_android.utils.model.UserUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private EditText userText;
    private EditText passText;
    private EditText emailText;
    private EditText nameText;
    private EditText surnameText;
    private TextView dateViewEdited;
    private Calendar calendarEdited;
    private int yearEdited, monthEdited, dayEdited;
    private DatePickerDialog.OnDateSetListener myDateListenerEdited;
    private Button registerButton;

    public RegistrationFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegistrationFragment.
     */
    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
        initializeGuiComponent(rootView);
        ImageButton btnSetDateExpiration = rootView.findViewById(R.id.btnSetDate);
        btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),
                        myDateListenerEdited, yearEdited, monthEdited, dayEdited).show();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptRegistration();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    private void initializeGuiComponent(View rootView){
        userText = rootView.findViewById(R.id.username);
        passText = rootView.findViewById(R.id.password);
        emailText= rootView.findViewById(R.id.email);
        nameText= rootView.findViewById(R.id.name);
        surnameText= rootView.findViewById(R.id.surname);
        registerButton = rootView.findViewById(R.id.register_button);
        dateViewEdited = rootView.findViewById(R.id.txtNewDateSelected);
        myDateListenerEdited = this;
        calendarEdited = Calendar.getInstance();
        yearEdited = calendarEdited.get(Calendar.YEAR);
        monthEdited = calendarEdited.get(Calendar.MONTH);
        dayEdited = calendarEdited.get(Calendar.DAY_OF_MONTH);
        showDate(yearEdited, monthEdited+1, dayEdited);
    }

     private boolean isEmailValid(String email) {
         return email.contains("@");
     }

     private boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,10}$");
    }

    private void showDate(int year, int month, int day) {
        dateViewEdited.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        showDate(year, month+1, day);
    }

    private void attemptRegistration() throws UnsupportedEncodingException, JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        if(!isPasswordValid(passText.getText().toString())){
            Toast toast = Toast.makeText(getContext(), "The password is too easy! Remember that a password should have at least upper case, one lower case letter and one digit, and 6 - 10 character long.", Toast.LENGTH_SHORT);
            toast.show();
        }else if (!isEmailValid(emailText.getText().toString())){
            Toast toast = Toast.makeText(getContext(), "Email is not valid!", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            try {
                final User user = new SimpleUser.Builder()
                        .name(nameText.getText().toString())
                        .surname(surnameText.getText().toString())
                        .username(userText.getText().toString())
                        .birthday(new DateTime(calendarEdited.getTime()))
                        .email(emailText.getText().toString())
                        .build();
                final JSONObject jsonUser = UserUtils.userToJson(user);
                jsonUser.put("hashedPassword", HashingUtils.hashString(passText.getText().toString()));
                StringEntity entity = new StringEntity(jsonUser.toString());
                client.post(getContext(), AuthenticationUtils.POST, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                        intent.putExtra(AuthenticationActivity.INTENT_TAG, "show-progress-bar");
                        LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            LocalStorageUtils.writeUserToFile(getContext(), user);
                            writeRegistrationInfoToFile(new JSONObject(new String(responseBody)));

                            final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                            intent.putExtra(AuthenticationActivity.INTENT_TAG, "authentication-ok");
                            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void writeRegistrationInfoToFile(final JSONObject json) throws JSONException {
                        final Collaboration collaboration = CollaborationUtils.jsonToCollaboration(json);
                        final CollaborationsManager manager = new ConcreteCollaborationManager();
                        manager.addCollaboration(new ConcreteShortCollaboration(collaboration));
                        LocalStorageUtils.writeCollaborationToFile(getContext(), collaboration);
                        LocalStorageUtils.writeShortCollaborationsToFile(getContext(), manager);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        final Intent intent = new Intent(AuthenticationActivity.INTENT_TAG);
                        intent.putExtra(AuthenticationActivity.INTENT_TAG, "hide-progress-bar");
                        LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
                        Toast.makeText(getContext(), statusCode + "Error: username is not available! Change it and retry.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (final MandatoryFieldMissingException e) {
                Toast.makeText(getContext(), "Please refill the form with valid data", Toast.LENGTH_SHORT).show();
            }

        }

    }
}

