package org.gammf.collabora_android.collabora_android_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] itemName = new String[]{
            "Item1",
            "Item2",
            "Item3",
            "Item4"
    };

    ArrayList<String> listItem = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView =(ListView) findViewById(R.id.listView);
        for(String s : itemName){
            listItem.add(s);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){
                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                final String titoloriga = (String) adattatore.getItemAtPosition(pos);
            /*    Toast.makeText(MainActivity.this, "Hai cliccato su: " + titoloriga,
                        Toast.LENGTH_SHORT).show();
*/
                Intent myIntent = new Intent(MainActivity.this, NotesActivity.class);
                myIntent.putExtra("position", pos); //Optional parameters
                myIntent.putExtra("title", titoloriga);
                MainActivity.this.startActivity(myIntent);

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddNotes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                listItem.add("New Item");
                adapter.notifyDataSetChanged();
                Snackbar.make(view, "Nota Aggiunta :P", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
/*
        Button btnApri = (Button) findViewById(R.id.btnNoteList);
        btnApri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, NotesActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }

        });
        */
    }
}
