package org.gammf.collabora_android.app.gui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.users.SimpleUser;
import org.gammf.collabora_android.users.User;
import org.gammf.collabora_android.utils.AuthenticationUtils;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.gammf.collabora_android.utils.MandatoryFieldMissingException;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText userText;
    private EditText passText;
    private EditText emailText;
    private EditText nameText;
    private EditText surnameText;
    private TextView dateViewEdited;
    private Calendar calendarEdited;
    private int yearEdited, monthEdited, dayEdited;
    private DatePickerDialog.OnDateSetListener myDateListenerEdited;
    private ProgressBar bar;
    private Button registerButton;
    private TextView passToLogin;

    public RegistrationFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
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
        ImageButton btnSetDateExpiration = rootView.findViewById(R.id.btnSetDateExpiration);
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
        passToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment returntoLogin = LoginFragment.newInstance();
                changeFragment(returntoLogin);
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
        bar = rootView.findViewById(R.id.register_progress);
        passToLogin = rootView.findViewById(R.id.text_backToLogin);
        dateViewEdited = rootView.findViewById(R.id.txtNewDateSelected);
        myDateListenerEdited = this;
        calendarEdited = Calendar.getInstance();
        yearEdited = calendarEdited.get(Calendar.YEAR);
        monthEdited = calendarEdited.get(Calendar.MONTH);
        dayEdited = calendarEdited.get(Calendar.DAY_OF_MONTH);
        showDate(yearEdited, monthEdited+1, dayEdited);
    }


    private void changeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();
        }
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
        }else{
            String hash = BCrypt.hashpw(passText.getText().toString(), "$2a$10$2wymx/003xT1XIndPwFgPe");
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("username", userText.getText().toString());
            jsonParams.put("email", emailText.getText().toString());
            jsonParams.put("name", nameText.getText().toString());
            jsonParams.put("surname", surnameText.getText().toString() );
            jsonParams.put("birthday", new DateTime(calendarEdited.getTime()));
            jsonParams.put("hashedPassword", hash);
            User temporaryUser = null;
            try {
                temporaryUser = new SimpleUser.Builder()
                        .name(nameText.getText().toString())
                        .surname(surnameText.getText().toString() )
                        .username(userText.getText().toString())
                        .birthday(new DateTime(calendarEdited.getTime()))
                        .email(emailText.getText().toString())
                        .build();
            } catch (MandatoryFieldMissingException e) {
                e.printStackTrace();
            }

            StringEntity entity = new StringEntity(jsonParams.toString());
            final User finalTemporaryUser = temporaryUser;
            client.post(getContext(),AuthenticationUtils.POST, entity,"application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    bar.setVisibility(View.VISIBLE);
                    registerButton.setClickable(false);
                    passToLogin.setClickable(false);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        LocalStorageUtils.writeUserToFile(getContext(), finalTemporaryUser);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    ((MainActivity)getActivity()).insertLateralMenu();
                    ((MainActivity)getActivity()).updateUserInfo();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    try {
                        Toast.makeText(getContext(), statusCode +new String(responseBody, "UTF-8")+"Error: username is not available! Change it and retry.", Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    bar.setVisibility(View.GONE);
                    registerButton.setClickable(true);
                    passToLogin.setClickable(true);
                }
            });
        }

    }
}

