package org.gammf.collabora_android.app.gui.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.gui.datetime_pickers.DatePickerManager;
import org.gammf.collabora_android.app.gui.datetime_pickers.TimePickerManager;
import org.gammf.collabora_android.app.gui.map.MapManager;
import org.gammf.collabora_android.app.gui.spinner.ResponsibleSpinnerManager;
import org.gammf.collabora_android.app.gui.spinner.StateSpinnerManager;
import org.gammf.collabora_android.app.utils.Observer;
import org.gammf.collabora_android.model.collaborations.general.Collaboration;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.model.notes.Note;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.model.notes.Location;
import org.gammf.collabora_android.model.notes.NoteState;
import org.gammf.collabora_android.model.notes.SimpleModuleNote;
import org.gammf.collabora_android.model.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.utils.model.AccessRight;
import org.gammf.collabora_android.utils.model.CollaborationType;
import org.gammf.collabora_android.utils.app.LocalStorageUtils;
import org.gammf.collabora_android.utils.app.SingletonAppUser;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Fragment for note creation user interface
 *
 * Use the {@link CreateNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNoteFragment extends Fragment {

    private static final String ARG_COLLABORATION_ID = "COLLABORATION_ID";
    private static final String ARG_MODULEID = "moduleName";
    private static final String NOMODULE = "nomodule";
    private static final String ARG_PREVNOTE = "PREVNOTESELECTED";
    private static final String ARG_NOTEITEMS = "NOTEITEMS";
    private static final String NONOTEID = "nonote";
    private static final int REQUEST_CODE = 0;
    private static final String CHOOSE_PREVIOUS_NOTE_DIALOG_TAG = "ChoosePreviousNotesDialogTag";

    private String noteState = "";
    private TextView dateView, timeView;
    private EditText txtContentNote;
    private ListView previousNotesList;
    private ArrayList<CollaborationComponentInfo> noteItems;
    private ArrayList<String> previousNotesSelected ;
    private CreateNoteFragment fragment = this;
    private MapManager mapManager;
    private DatePickerManager datePickerManager;
    private TimePickerManager timePickerManager;

    private String collaborationId, moduleId;
    private Collaboration collaboration;

    private String username;
    private Location location;
    private String responsible;
    private LocalDate date;
    private LocalTime time;

    private boolean dateSet = false;
    private boolean timeSet = false;

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
    public static CreateNoteFragment newInstance(final String collaborationId, final String moduleId) {
        final Bundle arg = new Bundle();
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
            this.collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }
        this.username = SingletonAppUser.getInstance().getUsername();
        this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext().getApplicationContext(), collaborationId);
        this.mapManager = new MapManager(MapManager.NO_LOCATION, this.getContext());
        this.mapManager.addObserver(new Observer<Location>() {
            @Override
            public void notify(final Location newlocation) {
                location = newlocation;
            }
        });
        this.datePickerManager = new DatePickerManager(getContext());
        this.datePickerManager.addObserver(new Observer<LocalDate>() {
            @Override
            public void notify(final LocalDate newDate) {
                date = newDate;
                dateSet = true;
                showDate();
            }
        });
        this.timePickerManager = new TimePickerManager(getContext());
        this.timePickerManager.addObserver(new Observer<LocalTime>() {
            @Override
            public void notify(final LocalTime newTime) {
                time = newTime;
                timeSet = true;
                showTime();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_note, container, false);
        noteItems = new ArrayList<>();
        previousNotesSelected = new ArrayList<>();
        initializeGuiComponent(rootView);

        return rootView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            this.noteItems = (ArrayList<CollaborationComponentInfo>) data.getSerializableExtra(ARG_NOTEITEMS);
            this.previousNotesSelected = (ArrayList<String>) data.getSerializableExtra(ARG_PREVNOTE);
            final DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, noteItems);
            previousNotesList.setAdapter(noteListAdapter);
        }
    }

    private void initializeGuiComponent(View rootView){
        txtContentNote = rootView.findViewById(R.id.txtNoteContent);
        txtContentNote.requestFocus();
        previousNotesList = rootView.findViewById(R.id.listViewPNote);
        Button btnAddPNote = rootView.findViewById(R.id.btnAddPNote);
        final SupportPlaceAutocompleteFragment autocompleteFragment = new SupportPlaceAutocompleteFragment();
        getFragmentManager().beginTransaction().replace(R.id.place_autocomplete_fragment, autocompleteFragment).commit();
        autocompleteFragment.setOnPlaceSelectedListener(this.mapManager);

        final StateSpinnerManager stateSpinnerManager = new StateSpinnerManager(StateSpinnerManager.NO_STATE, rootView, R.id.spinnerNewNoteState,
                LocalStorageUtils.readShortCollaborationsFromFile(getContext().getApplicationContext()).getCollaboration(this.collaborationId).getCollaborationType());
        stateSpinnerManager.addObserver(new Observer<String>() {
            @Override
            public void notify(final String state) {
                noteState = state;
            }
        });

        this.createResponsibleSpinnerManager(rootView);

        final FloatingActionButton btnAddNote = rootView.findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processInput();
            }
        });
        dateView = rootView.findViewById(R.id.txtNewDateSelected);
        timeView = rootView.findViewById(R.id.txtNewTimeSelected);

        ImageButton btnSetDateExpiration = rootView.findViewById(R.id.btnSetDateExpiration);
        btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerManager.showDatePicker(DatePickerManager.NO_DATE);
            }
        });

        ImageButton btnSetTimeExpiration = rootView.findViewById(R.id.btnSetTimeExpiration);
        btnSetTimeExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerManager.showTimePicker(TimePickerManager.NO_TIME);
            }
        });

        previousNotesList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btnAddPNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final ChoosePreviousNotesDialogFragment dialog = ChoosePreviousNotesDialogFragment.newInstance(
                        collaborationId, moduleId,noteItems ,previousNotesSelected ,NONOTEID);
                dialog.setTargetFragment(fragment, REQUEST_CODE);
                dialog.show(getActivity().getSupportFragmentManager(), CHOOSE_PREVIOUS_NOTE_DIALOG_TAG);
            }
        });
    }

    private void processInput(){
        final String insertedNoteName = txtContentNote.getText().toString();
        if (insertedNoteName.equals("")) {
            txtContentNote.setError(getResources().getString(R.string.fieldempty));
        } else {
            if (this.dateSet && this.timeSet && isDateTimeValid()) {
                final  DateTime date = new DateTime(this.date.getYear(), this.date.getMonthOfYear(),
                        this.date.getDayOfMonth(), this.time.getHourOfDay(), this.time.getMinuteOfHour());
                checkPreviousNotes(insertedNoteName,date);
            } else if (!this.dateSet && !this.timeSet) {
                checkPreviousNotes(insertedNoteName, null);
            } else {
                Toast.makeText(getContext().getApplicationContext(), "Choose a valid expiration date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPreviousNotes(String insertedNoteName, DateTime date) {
        if (previousNotesSelected.isEmpty())
            addNote(insertedNoteName, location, new NoteState(noteState, responsible), date, null);
        else {
            Pair<Boolean, String> checkPrevNotes = NoteFragmentUtils.checkPreviousNotesState(getContext(),noteState, previousNotesSelected, collaboration);
            if (checkPrevNotes.first)
                addNote(insertedNoteName, location, new NoteState(noteState, responsible), date, previousNotesSelected);
            else
                Toast.makeText(getContext().getApplicationContext(),checkPrevNotes.second , Toast.LENGTH_LONG).show();
        }

    }

    private boolean isDateTimeValid() {
        final DateTime now = new DateTime();
        final DateTime expiration = new DateTime(this.date.getYear(), this.date.getMonthOfYear(),
                this.date.getDayOfMonth(), this.time.getHourOfDay(), this.time.getMinuteOfHour());
        return expiration.compareTo(now) > 0;
    }

    private void addNote(final String content, final Location location, final NoteState state, final DateTime expiration, final List<String> previousNotes){
        final Note simpleNote = new SimpleNoteBuilder(content, state)
                .setLocation(location)
                .setExpirationDate(expiration)
                .setPreviousNotes(previousNotes)
                .buildNote();
        if (moduleId.equals(NOMODULE)) {
            new SendMessageToServerTask(getContext()).execute(new ConcreteNoteUpdateMessage(
                    username, simpleNote, UpdateMessageType.CREATION, collaborationId));
        } else {
            new SendMessageToServerTask(getContext()).execute(new ConcreteNoteUpdateMessage(
                    username, new SimpleModuleNote(simpleNote, moduleId), UpdateMessageType.CREATION, collaborationId));
        }
    }

    private void showDate() {
        dateView.setText(new StringBuilder()
                .append(this.date.getDayOfMonth())
                .append("/")
                .append(this.date.getMonthOfYear())
                .append("/")
                .append(this.date.getYear()));
    }

    private void showTime() {
        timeView.setText(new StringBuilder()
                .append(this.time.getHourOfDay())
                .append(":")
                .append(this.time.getMinuteOfHour() < 10 ? "0" + this.time.getMinuteOfHour() : this.time.getMinuteOfHour()));
    }

    private void createResponsibleSpinnerManager(final View rootView) {
        if (collaboration.getCollaborationType() != CollaborationType.PRIVATE &&
                ((SharedCollaboration)collaboration).getMember(username).getAccessRight() == AccessRight.ADMIN) {
            new ResponsibleSpinnerManager(
                    ResponsibleSpinnerManager.NO_RESPONSIBLE,
                    rootView, R.id.spinnerResponsible, this.collaborationId
            ).addObserver(new Observer<String>() {
                @Override
                public void notify(String newResponsible) {
                    if (newResponsible.equals(ResponsibleSpinnerManager.NO_RESPONSIBLE)) {
                        responsible = null;
                    } else {
                        responsible = newResponsible;
                    }
                }
            });
        } else {
            this.setEditResponsibleGone(rootView);
        }
    }

    private void setEditResponsibleGone(final View rootView) {
        rootView.findViewById(R.id.imgResponsible).setVisibility(View.GONE);
        rootView.findViewById(R.id.textViewResponsible).setVisibility(View.GONE);
        rootView.findViewById(R.id.spinnerResponsible).setVisibility(View.GONE);

    }
}