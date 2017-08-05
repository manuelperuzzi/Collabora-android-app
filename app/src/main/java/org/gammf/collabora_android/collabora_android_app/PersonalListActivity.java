package org.gammf.collabora_android.collabora_android_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonalListActivity extends AppCompatActivity {

    private static final String LIST_NAME = "listname";
    ArrayList<String> projectlistItem = new ArrayList<String>();
    CustomListAdapter noteListAdapter;
    String[] itemName = new String[]{
            "Nota 1",
            "Nota 2",
            "Nota 3",
            "Nota 4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_list);

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
                final String listName = (String) adattatore.getItemAtPosition(pos);
                Intent myIntent = new Intent(PersonalListActivity.this, PersonalListActivity.class);
                myIntent.putExtra(LIST_NAME, listName);
                PersonalListActivity.this.startActivity(myIntent);
            }
        });

    }
}
