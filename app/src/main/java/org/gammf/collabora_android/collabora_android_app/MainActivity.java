package org.gammf.collabora_android.collabora_android_app;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] personalitemName = new String[]{
            "Lista della spesa",
            "Lista verdura"
    };

    String[] itemName = new String[]{
            "Progetto 1",
            "Progetto 2",
            "Progetto 3",
            "Progetto 4"
    };

    ArrayList<String> personallistItem = new ArrayList<String>();
    ArrayList<String> projectlistItem = new ArrayList<String>();

    private static final int REQUEST_CODE = 1;
    private static final String LIST_TYPE = "listtype";
    private static final String LIST_NAME = "listname";
    private static final String LIST_DESCRIPTION = "listdescription";
    CustomListAdapter personalAdapter;
    CustomListAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView personallistView =(ListView) findViewById(R.id.personallistView);
        for(String s : personalitemName){
            personallistItem.add(s);
        }

        final ListView projectListView =(ListView) findViewById(R.id.listView);
        for(String s : itemName){
            projectlistItem.add(s);
        }

        projectAdapter = new CustomListAdapter(this, projectlistItem);
        projectListView.setAdapter(projectAdapter);

        personalAdapter=new CustomListAdapter(this, personallistItem);
        personallistView.setAdapter(personalAdapter);

        personallistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                final String listName = (String) adattatore.getItemAtPosition(pos);
                Intent myIntent = new Intent(MainActivity.this, SingleListActivity.class);
                myIntent.putExtra(LIST_NAME, listName);
                MainActivity.this.startActivity(myIntent);
            }
        });

        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){
                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                final String titoloriga = (String) adattatore.getItemAtPosition(pos);
            /*    Toast.makeText(MainActivity.this, "Hai cliccato su: " + titoloriga,
                        Toast.LENGTH_SHORT).show();
*/
                Intent myIntent = new Intent(MainActivity.this, SingleNoteActivity.class);
                myIntent.putExtra("position", pos); //Optional parameters
                myIntent.putExtra("title", titoloriga);
                MainActivity.this.startActivity(myIntent);

            }
        });

        FloatingActionButton personalfab = (FloatingActionButton) findViewById(R.id.btnAddPersonalNotes);
        personalfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, AddListActivity.class);
                i.putExtra("type", "personallist");
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddNotes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MainActivity.this, AddListActivity.class);
                myIntent.putExtra("type", "projectlist"); //Optional parameters
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
                    String listType = data.getStringExtra(LIST_TYPE);
                    String listName = data.getStringExtra(LIST_NAME);
                    String listDescription = data.getStringExtra(LIST_DESCRIPTION);
                    addNewList(listType, listName, listDescription);
                }
                break;
            }
        }
    }


    private void addNewList(String listType, String listName, String listDescription){
        if(listType.equals("personallist")) {
            personalAdapter.addList( listName, listDescription);
            personalAdapter.notifyDataSetChanged();
        }else if(listType.equals("projectlist")){
            projectAdapter.addList( listName, listDescription);
            projectAdapter.notifyDataSetChanged();
        }
    }
}
