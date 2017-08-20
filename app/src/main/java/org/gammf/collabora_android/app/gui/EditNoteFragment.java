package org.gammf.collabora_android.app.gui;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.gammf.collabora_android.app.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditNoteFragment extends Fragment implements PlaceSelectionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SupportPlaceAutocompleteFragment autocompleteFragmentEdited;
    private RadioGroup radioGroupNoteStateEdited;
    private RadioButton radioButtonToDoEdited, radioButtonDoingEdited, radioButtonDoneEdited;
    private String noteStateEdited = "";
    private Calendar calendarEdited;
    private TextView dateViewEdited, timeViewEdited;
    private int yearEdited, monthEdited, dayEdited, hourEdited, minuteEdited;
    private EditText txtContentNoteEdited;

    public EditNoteFragment() {
        setHasOptionsMenu(true);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditNoteFragment newInstance(String param1, String param2) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editdone) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);
        txtContentNoteEdited = rootView.findViewById(R.id.txtNoteContentEdit);
        txtContentNoteEdited.requestFocus();
        autocompleteFragmentEdited = new SupportPlaceAutocompleteFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.place_autocomplete_fragment_edit, autocompleteFragmentEdited);
        ft.commit();
        autocompleteFragmentEdited.setOnPlaceSelectedListener(this);
        radioGroupNoteStateEdited = rootView.findViewById(R.id.radioGroupNoteStateEdit);
        radioButtonToDoEdited = rootView.findViewById(R.id.radioButtonToDoEdit);
        radioButtonDoingEdited = rootView.findViewById(R.id.radioButtonDoingEdit);
        radioButtonDoneEdited = rootView.findViewById(R.id.radioButtonDoneEdit);
        radioButtonToDoEdited.setChecked(true);
        /*
        FloatingActionButton btnEditNoteDone = rootView.findViewById(R.id.btnEditNoteDone);
        btnEditNoteDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String insertedNoteName = txtContentNoteEdited.getText().toString();
                if(insertedNoteName.equals("")){
                    Resources res = getResources();
                    txtContentNoteEdited.setError(res.getString(R.string.fieldempty));
                }else {
                    int selectedId = radioGroupNoteStateEdited.getCheckedRadioButtonId();
                    if (selectedId == radioButtonToDoEdited.getId()) {
                        noteStateEdited = "To-do";
                        Log.e("", noteStateEdited);
                    } else if (selectedId == radioButtonDoingEdited.getId()) {
                        noteStateEdited = "Doing";
                        Log.e("", noteStateEdited);
                    } else if (selectedId == radioButtonDoneEdited.getId()) {
                        noteStateEdited = "Done";
                        Log.e("", noteStateEdited);
                    }

                }

            }
        });
        */
        dateViewEdited = rootView.findViewById(R.id.txtEditDateSelected);
        calendarEdited = Calendar.getInstance();
        yearEdited = calendarEdited.get(Calendar.YEAR);
        monthEdited = calendarEdited.get(Calendar.MONTH);
        dayEdited = calendarEdited.get(Calendar.DAY_OF_MONTH);
        showDate(yearEdited, monthEdited+1, dayEdited);

        timeViewEdited = rootView.findViewById(R.id.txtEditTimeSelected);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String strTime = sdf.format(calendarEdited.getTime());
        timeViewEdited.setText(strTime);


        ImageButton btnSetDateExpiration = rootView.findViewById(R.id.btnEditDateExpiration);
        btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),
                        myDateListenerEdited, yearEdited, monthEdited, dayEdited).show();
            }
        });

        ImageButton btnSetTimeExpiration = rootView.findViewById(R.id.btnEditTimeExpiration);
        btnSetTimeExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(),
                        myTimeListenerEdited, hourEdited, minuteEdited, true).show();
            }
        });

        return rootView;
    }
    private DatePickerDialog.OnDateSetListener myDateListenerEdited = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private TimePickerDialog.OnTimeSetListener myTimeListenerEdited = new
            TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    showTime(hour,minute);
                }
            };
    private void showDate(int year, int month, int day) {
        dateViewEdited.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    private void showTime(int hour, int minute){
        timeViewEdited.setText(new StringBuilder().append(hour).append(":").append(minute));
    }

    @Override
    public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        Log.i(TAG, "Place: " + place.getName());

        String placeDetailsStr = place.getName()+"";
              /*  + "\n"
                + place.getId() + "\n"
                + place.getLatLng().toString() + "\n"
                + place.getAddress() + "\n"
                + place.getAttributions();*/
    }

    @Override
    public void onError(Status status) {
        Log.i(TAG, "An error occurred: " + status);
    }
}
