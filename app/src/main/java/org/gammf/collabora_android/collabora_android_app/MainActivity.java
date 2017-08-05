package org.gammf.collabora_android.collabora_android_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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
    ArrayList<String> listItem = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView personallistView =(ListView) findViewById(R.id.personallistView);
        for(String s : personalitemName){
            personallistItem.add(s);
        }

        final ListView listView =(ListView) findViewById(R.id.listView);
        for(String s : itemName){
            listItem.add(s);
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem);
        listView.setAdapter(adapter);

        final CustomListAdapter personalAdapter=new CustomListAdapter(this, personallistItem);
        personallistView.setAdapter(personalAdapter);

        personallistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                final String titoloriga = (String) adattatore.getItemAtPosition(pos);
               Toast.makeText(MainActivity.this, "Hai cliccato su: " + titoloriga,
                        Toast.LENGTH_SHORT).show();
            }
        });

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

        FloatingActionButton personalfab = (FloatingActionButton) findViewById(R.id.btnAddPersonalNotes);
        personalfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                personallistItem.add("New List");
                personalAdapter.notifyDataSetChanged();
                Snackbar.make(view, "Lista Aggiunta :P", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

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

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }
}
