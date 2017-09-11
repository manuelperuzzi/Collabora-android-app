package org.gammf.collabora_android.app.gui.note;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.MapView;
import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.gui.map.MapManager;
import org.gammf.collabora_android.app.gui.spinner.ResponsibleSpinnerManager;
import org.gammf.collabora_android.app.gui.spinner.StateSpinnerManager;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.utils.Observer;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.notes.Location;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.utils.AccessRight;
import org.gammf.collabora_android.utils.CollaborationType;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditNoteFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_NOTEID = "noteId";
    private static final String ARG_MODULEID = "moduleName";
    private static final String ARG_PREVNOTE = "PREVNOTESELECTED";
    private static final String ARG_NOTEITEMS = "NOTEITEMS";
    private static final int REQUEST_CODE = 0;
    private static final String CHOOSE_PREVIOUS_NOTE_DIALOG_TAG = "ChoosePreviousNotesDialogTag";
    private EditNoteFragment fragment = this;

    private String username;
    private String noteStateEdited = "";
    private TextView dateViewEdited, timeViewEdited;
    private EditText txtContentNoteEdited;
    private DatePickerDialog.OnDateSetListener myDateListenerEdited;
    private TimePickerDialog.OnTimeSetListener myTimeListenerEdited;
    private ListView previousNotesList;
    private List<String> previousNotesSelected ;
    private ArrayList<CollaborationComponentInfo> noteItems;

    private String collaborationId, noteId,moduleId;

    private MapManager mapManager;

    private Note note;
    private Collaboration collaboration;
    private int year, month, day, hour, minute ;
    private String responsible;
    private boolean dateSet = false;
    private boolean timeSet = false;

    public EditNoteFragment() {
        setHasOptionsMenu(true);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditNoteFragment newInstance(String username, String collabId, String noteId,String moduleId) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_COLLABID, collabId);
        args.putString(ARG_NOTEID, noteId);
        args.putString(ARG_MODULEID, moduleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.noteId = getArguments().getString(ARG_NOTEID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }

        try {
            this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
            note = collaboration.getNote(noteId);
            if (note.getLocation() != null) {
                this.mapManager = new MapManager(note.getLocation(), getContext());
            } else {
                this.mapManager = new MapManager(MapManager.NO_LOCATION, getContext());
            }
            this.mapManager.addObserver(new Observer<Location>() {
                @Override
                public void notify(final Location location) {
                    note.modifyLocation(location);
                }
            });
            this.responsible = note.getState().getCurrentResponsible();
        } catch (final IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.editdone_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_editdone) {
            if (checkUserNoteUpdate()) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);
        noteItems = new ArrayList<>();
        previousNotesSelected = new ArrayList<>();
        initializeGuiComponent(rootView);

        return rootView;
    }

    private void initializeGuiComponent(final View rootView){
        txtContentNoteEdited = rootView.findViewById(R.id.txtNoteContentEdit);
        txtContentNoteEdited.setText(note.getContent());
        txtContentNoteEdited.requestFocus();
        SupportPlaceAutocompleteFragment autocompleteFragmentEdited = new SupportPlaceAutocompleteFragment();
        getFragmentManager().beginTransaction().replace(R.id.place_autocomplete_fragment_edit, autocompleteFragmentEdited).commit();
        autocompleteFragmentEdited.setOnPlaceSelectedListener(this.mapManager);

        final StateSpinnerManager stateSpinnerManager = new StateSpinnerManager(this.note.getState().getCurrentState(), rootView, R.id.spinnerEditNoteState,
                LocalStorageUtils.readShortCollaborationsFromFile(getContext().getApplicationContext()).getCollaboration(this.collaborationId).getCollaborationType());
        stateSpinnerManager.addObserver(new Observer<String>() {
            @Override
            public void notify(final String newState) {
                noteStateEdited = newState;
            }
        });

        this.createResponsibleSpinnerManager(rootView);

        dateViewEdited = rootView.findViewById(R.id.txtEditDateSelected);
        timeViewEdited = rootView.findViewById(R.id.txtEditTimeSelected);
        ImageButton btnSetDateExpiration = rootView.findViewById(R.id.btnEditDateExpiration);
        ImageButton btnSetTimeExpiration = rootView.findViewById(R.id.btnEditTimeExpiration);
        if (note.getExpirationDate() != null) {
            this.dateSet = true;
            this.timeSet = true;

            year = note.getExpirationDate().getYear();
            month = note.getExpirationDate().getMonthOfYear();
            day = note.getExpirationDate().getDayOfMonth();
            hour = note.getExpirationDate().getHourOfDay();
            minute = note.getExpirationDate().getMinuteOfHour();

            dateViewEdited.setText(note.getExpirationDate().toLocalDate().toString());
            timeViewEdited.setText(note.getExpirationDate().toLocalTime().toString());
            btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getActivity(), myDateListenerEdited, year, month, day).show();
                }
            });
            btnSetTimeExpiration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(getActivity(), myTimeListenerEdited, hour, minute, true).show();
                }
            });
        } else {
            final Calendar calendar = Calendar.getInstance();
            btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getActivity(), myDateListenerEdited,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            btnSetTimeExpiration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(getActivity(), myTimeListenerEdited,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE), true).show();
                }
            });
        }

        previousNotesList = rootView.findViewById(R.id.listViewPNote);
        Button btnAddPNote = rootView.findViewById(R.id.btnAddPNote);
        if(note.getPreviousNotes()!= null){
            final DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, NoteFragmentUtils.fillListView(getContext(), note, collaborationId));
            previousNotesList.setAdapter(noteListAdapter);
        }
        previousNotesList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(previousNotesList);

        btnAddPNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final ChoosePreviousNotesDialogFragment dialog = ChoosePreviousNotesDialogFragment.newInstance(
                        collaborationId, moduleId,noteItems , (ArrayList<String>) previousNotesSelected, noteId);
                dialog.setTargetFragment(fragment, REQUEST_CODE);
                dialog.show(getActivity().getSupportFragmentManager(), CHOOSE_PREVIOUS_NOTE_DIALOG_TAG);
            }
        });
        myDateListenerEdited = this;
        myTimeListenerEdited = this;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @SuppressWarnings("unchecked")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            this.noteItems = (ArrayList<CollaborationComponentInfo>) data.getSerializableExtra(ARG_NOTEITEMS);
            this.previousNotesSelected = (ArrayList<String>) data.getSerializableExtra(ARG_PREVNOTE);
            final DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, noteItems);
            previousNotesList.setAdapter(noteListAdapter);
            setListViewHeightBasedOnChildren(previousNotesList);
        }
    }

    private boolean checkUserNoteUpdate(){
        String insertedNoteName = txtContentNoteEdited.getText().toString();
        if(insertedNoteName.equals("")){
            txtContentNoteEdited.setError(getResources().getString(R.string.fieldempty));
            return false;
        }else{
            note.modifyContent(insertedNoteName);
            if (this.dateSet && this.timeSet && isDateTimeValid()) {
                note.modifyExpirationDate(new DateTime(year, month, day, hour, minute));
            } else if (this.dateSet || this.timeSet) {
                Toast.makeText(getContext().getApplicationContext(), "Choose a valid expiration date", Toast.LENGTH_SHORT).show();
                return false;
            }
            note.modifyState(new NoteState(noteStateEdited, responsible));
            if(!previousNotesSelected.isEmpty())
                note.modifyPreviousNotes(previousNotesSelected);
            else
                note.modifyPreviousNotes(null);
            new SendMessageToServerTask(getContext()).execute(new ConcreteNoteUpdateMessage(
                username, note, UpdateMessageType.UPDATING, collaborationId));
            return true;
        }
    }

    private boolean isDateTimeValid() {
        final DateTime now = new DateTime();
        final DateTime expiration = new DateTime(year, month, day, hour, minute);
        return expiration.compareTo(now) > 0;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        this.mapManager.createMap((MapView) view.findViewById(R.id.mapViewLocationEdit), savedInstanceState);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        this.year = year;
        this.month = month + 1;
        this.day = day;
        this.dateSet = true;
        showDate();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        this.timeSet = true;
        showTime();
    }

    private void showDate() {
        dateViewEdited.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    private void showTime() {
        timeViewEdited.setText(new StringBuilder().append(hour).append(":").append(minute));
    }

    private void createResponsibleSpinnerManager(final View rootView) {
        if (collaboration.getCollaborationType() != CollaborationType.PRIVATE &&
                ((SharedCollaboration)collaboration).getMember(username).getAccessRight() == AccessRight.ADMIN) {
            final ResponsibleSpinnerManager responsibleSpinnerManager = new ResponsibleSpinnerManager(
                    note.getState().getCurrentResponsible() == null ? ResponsibleSpinnerManager.NO_RESPONSIBLE : note.getState().getCurrentResponsible(),
                    rootView, R.id.spinnerEditResponsible, this.collaborationId
            );

            responsibleSpinnerManager.addObserver(new Observer<String>() {
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
        rootView.findViewById(R.id.imgEditResponsible).setVisibility(View.GONE);
        rootView.findViewById(R.id.textViewEditResponsible).setVisibility(View.GONE);
        rootView.findViewById(R.id.spinnerEditResponsible).setVisibility(View.GONE);

    }
}
