package org.gammf.collabora_android.collabora_android_app;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SingleListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final String LIST_NAME = "listname";
    private static final String NOTE_NAME = "notename";
    private static final String NOTE_DESC = "notedesc";

    private static final String ADD_NOTENAME = "notenameadded";
    private static final String ADD_NOTEDESC = "notedescadded";

    ArrayList<String> projectlistItem = new ArrayList<String>();
    CustomListAdapter noteListAdapter;
    String[] itemName = new String[]{
            "Latte",
            "Farina 00",
            "Biscotti",
            "Insalata"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list);

        final Intent intent = getIntent();
        String listName = intent.getStringExtra(LIST_NAME);
        TextView moduleTitle = (TextView) findViewById(R.id.txtListTitle);
        moduleTitle.setText(listName);

        final ListView noteListView =(ListView) findViewById(R.id.noteList);
        for(String s : itemName){
            projectlistItem.add(s);
        }

        noteListAdapter = new CustomListAdapter(this, projectlistItem);
        noteListView.setAdapter(noteListAdapter);

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                Intent myIntent = new Intent(SingleListActivity.this, SingleNoteActivity.class);
                final String itemClicked = (String) adattatore.getItemAtPosition(pos);
                myIntent.putExtra(NOTE_NAME, itemClicked);
                myIntent.putExtra(NOTE_DESC, "Description "+itemClicked);
                SingleListActivity.this.startActivity(myIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddNewNotes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(SingleListActivity.this, AddListActivity.class);
                myIntent.putExtra("type", "newnote"); //Optional parameters
                startActivityForResult(myIntent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    String listName = data.getStringExtra(ADD_NOTENAME);
                    String listDescription = data.getStringExtra(ADD_NOTEDESC);
                    addNewList(listName, listDescription);
                }
                break;
            }
        }
    }

    private void addNewList(String listName, String listDescription){
        noteListAdapter.addList(listName, listDescription);
        noteListAdapter.notifyDataSetChanged();
    }
}
