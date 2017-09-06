package org.gammf.collabora_android.app.gui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.map.MapManager;
import org.gammf.collabora_android.app.gui.spinner.StateSpinnerManager;
import org.gammf.collabora_android.app.utils.NoteProjectState;
import org.gammf.collabora_android.app.utils.Observer;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.notes.Location;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleModuleNote;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Fragment for note creation user interface
 */
public class CreateNoteFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String ARG_USERNAME = "USERNAME";
    private static final String ARG_COLLABORATION_ID = "COLLABORATION_ID";
    private static final String ARG_MODULEID = "moduleName";
    private static final String NOMODULE = "nomodule";

    private String noteState = "";
    private TextView dateView, timeView;
    private EditText txtContentNote;
    private StateSpinnerManager spinnerManager;

    private DatePickerDialog.OnDateSetListener myDateListener;
    private TimePickerDialog.OnTimeSetListener myTimeListener;

    private MapManager mapManager;

    private String collaborationId, moduleId;

    private String username;
    private Location location;
    private int year, month, day, hour, minute;

    public CreateNoteFragment() {
        setHasOptionsMenu(false);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param collaborationId collaboration id
     * @param moduleId the id of the module if the notes is contained in a module.
     *
     * @return A new instance of fragment CreateNoteFragment.
     */
    public static CreateNoteFragment newInstance(final String username, final String collaborationId,
                                                 final String moduleId) {
        final Bundle arg = new Bundle();
        arg.putString(ARG_USERNAME, username);
        arg.putString(ARG_COLLABORATION_ID, collaborationId);
        arg.putString(ARG_MODULEID, moduleId);
        final CreateNoteFragment fragment = new CreateNoteFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if(getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }
        this.mapManager = new MapManager(MapManager.NO_LOCATION, this.getContext());
        this.mapManager.addObserver(new Observer<Location>() {
            @Override
            public void notify(final Location newlocation) {
                location = newlocation;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_note, container, false);

        initializeGuiComponent(rootView);

        return rootView;
    }

    private void initializeGuiComponent(View rootView){
        txtContentNote = rootView.findViewById(R.id.txtNoteContent);
        txtContentNote.requestFocus();
        final SupportPlaceAutocompleteFragment autocompleteFragment = new SupportPlaceAutocompleteFragment();
        getFragmentManager().beginTransaction().replace(R.id.place_autocomplete_fragment, autocompleteFragment).commit();
        autocompleteFragment.setOnPlaceSelectedListener(this.mapManager);

        this.spinnerManager = new StateSpinnerManager(rootView,
                LocalStorageUtils.readShortCollaborationsFromFile(getContext().getApplicationContext()).getCollaboration(this.collaborationId).getCollaborationType());
        this.spinnerManager.addObserver(new Observer<String>() {
            @Override
            public void notify(final String state) {
                noteState = state;
            }
        });

        final FloatingActionButton btnAddNote = rootView.findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processInput();
            }
        });
        dateView = rootView.findViewById(R.id.txtNewDateSelected);
        timeView = rootView.findViewById(R.id.txtNewTimeSelected);

        myDateListener = this;
        myTimeListener = this;

        final Calendar calendar = Calendar.getInstance();
        ImageButton btnSetDateExpiration = rootView.findViewById(R.id.btnSetDateExpiration);
        btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), myDateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ImageButton btnSetTimeExpiration = rootView.findViewById(R.id.btnSetTimeExpiration);
        btnSetTimeExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), myTimeListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });
    }

    private void processInput(){
        final String insertedNoteName = txtContentNote.getText().toString();
        if (insertedNoteName.equals("")) {
            txtContentNote.setError(getResources().getString(R.string.fieldempty));
        } else {
            if (isDateTimeValid()) {
                addNote(insertedNoteName, location, new NoteState(noteState, null),
                        new DateTime(year, month, day, hour, minute));
            } else {
                addNote(insertedNoteName, location, new NoteState(noteState, null), null);
            }
        }
    }

    private boolean isDateTimeValid() {
        return year > 0 && month > 0 && day > 0 && hour >=0 && minute >= 0;
    }

    private void addNote(final String content, final Location location, final NoteState state, final DateTime expiration){
        final Note simpleNote = new SimpleNoteBuilder(content, state)
                .setLocation(location)
                .setExpirationDate(expiration)
                .buildNote();
        if (moduleId.equals(NOMODULE)) {
            new SendMessageToServerTask(getContext()).execute(new ConcreteNoteUpdateMessage(
                    username, simpleNote, UpdateMessageType.CREATION, collaborationId));
        } else {
            new SendMessageToServerTask(getContext()).execute(new ConcreteNoteUpdateMessage(
                    username, new SimpleModuleNote(simpleNote, moduleId), UpdateMessageType.CREATION, collaborationId));
        }

        ((MainActivity)getActivity()).showLoadingSpinner();
        new TimeoutSender(getContext(), 5000);
    }

    @Override
    public void onDateSet(final DatePicker arg0, final int year, final int month, final int day) {
        this.year = year;
        this.month = month + 1;
        this.day = day;
        showDate();
    }

    @Override
    public void onTimeSet(final TimePicker timePicker, final int hour, final int minute) {
        this.hour = hour;
        this.minute = minute;
        showTime();
    }

    private void showDate() {
        dateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    private void showTime(){
        timeView.setText(new StringBuilder().append(hour).append(":").append(minute));
    }
}
