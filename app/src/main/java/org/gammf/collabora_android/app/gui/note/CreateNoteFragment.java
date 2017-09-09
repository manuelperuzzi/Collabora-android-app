package org.gammf.collabora_android.app.gui.note;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.gui.map.MapManager;
import org.gammf.collabora_android.app.gui.spinner.ResponsibleSpinnerManager;
import org.gammf.collabora_android.app.gui.spinner.StateSpinnerManager;
import org.gammf.collabora_android.app.utils.Observer;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.notes.Location;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleModuleNote;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.utils.AccessRight;
import org.gammf.collabora_android.utils.CollaborationType;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

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
    private ListView previousNotesList;
    private ArrayList<CollaborationComponentInfo> noteItems;
    private ArrayList<String> previousNotesSelected ;
    private DatePickerDialog.OnDateSetListener myDateListener;
    private TimePickerDialog.OnTimeSetListener myTimeListener;

    private MapManager mapManager;

    private String collaborationId, moduleId;
    private Collaboration collaboration;

    private String username;
    private Location location;
    private String responsible;
    private int year, month, day, hour, minute;

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
        try {
            this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext().getApplicationContext(), collaborationId);
            this.mapManager = new MapManager(MapManager.NO_LOCATION, this.getContext());
            this.mapManager.addObserver(new Observer<Location>() {
                @Override
                public void notify(final Location newlocation) {
                    location = newlocation;
                }
            });
        } catch (IOException | JSONException e) {
            e.printStackTrace(); // TODO this exception will be deleted.
        }
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

        btnAddPNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final List<String> listItems = new ArrayList<>();
                final List<Note> allNotes = new ArrayList<>();
                final List<Integer> mSelectedItems = new ArrayList<>();
                try {
                    Collaboration collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
                    if (moduleId.equals(NOMODULE)) {
                        allNotes.addAll(collaboration.getAllNotes());
                    }else{
                        for (Module module: ((ConcreteProject)collaboration).getAllModules()){
                            if(module.getId().equals(moduleId))
                                allNotes.addAll(module.getAllNotes());
                        }
                    }
                    for (Note note : allNotes) {
                        listItems.add(note.getContent());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                final CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select previous notes")
                        .setMultiChoiceItems(charSequenceItems, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            mSelectedItems.add(which);
                                        } else if (mSelectedItems.contains(which)) {
                                            mSelectedItems.remove(Integer.valueOf(which));
                                        }
                                    }
                                })
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                noteItems.clear();
                                previousNotesSelected.clear();
                                for (int position: mSelectedItems) {
                                    previousNotesSelected.add(allNotes.get(position).getNoteID());
                                    noteItems.add(new CollaborationComponentInfo(allNotes.get(position).getNoteID(), allNotes.get(position).getContent(), CollaborationComponentType.NOTE));
                                }
                                final DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, noteItems);
                                previousNotesList.setAdapter(noteListAdapter);

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void processInput(){
        final String insertedNoteName = txtContentNote.getText().toString();
        if (insertedNoteName.equals("")) {
            txtContentNote.setError(getResources().getString(R.string.fieldempty));
        } else {
            if (this.dateSet && this.timeSet && isDateTimeValid()) {
                if (previousNotesSelected.isEmpty())
                    addNote(insertedNoteName, location, new NoteState(noteState, null),
                            new DateTime(year, month, day, hour, minute), null);
                else
                    addNote(insertedNoteName, location, new NoteState(noteState, null),
                            new DateTime(year, month, day, hour, minute), previousNotesSelected);
            } else if (!this.dateSet && !this.dateSet) {
                if (previousNotesSelected.isEmpty())
                    addNote(insertedNoteName, location, new NoteState(noteState, null), null, null);
                else
                    addNote(insertedNoteName, location, new NoteState(noteState, null), null, previousNotesSelected);
            } else {
                Toast.makeText(getContext().getApplicationContext(), "Choose a valid expiration date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isDateTimeValid() {
        final DateTime now = new DateTime();
        final DateTime expiration = new DateTime(year, month, day, hour, minute);
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

    @Override
    public void onDateSet(final DatePicker arg0, final int year, final int month, final int day) {
        this.year = year;
        this.month = month + 1;
        this.day = day;
        this.dateSet = true;
        showDate();
    }

    @Override
    public void onTimeSet(final TimePicker timePicker, final int hour, final int minute) {
        this.hour = hour;
        this.minute = minute;
        this.timeSet = true;
        showTime();
    }

    private void showDate() {
        dateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    private void showTime(){
        timeView.setText(new StringBuilder().append(hour).append(":").append(minute));
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
